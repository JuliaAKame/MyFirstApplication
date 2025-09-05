package com.fiap.myapp.recycling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.myapp.recycling.models.MaterialCategory
import com.fiap.myapp.recycling.models.RecyclingItem
import com.fiap.myapp.recycling.models.WeightRange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing recycling feature state and operations.
 * Follows Clean Architecture principles with dependency injection.
 */
class RecyclingViewModel(
    private val repository: RecyclingRepository = FirebaseRecyclingRepository()
) : ViewModel() {
    
    // State management
    private val _uiState = MutableStateFlow<RecyclingState>(RecyclingState.Idle)
    val uiState: StateFlow<RecyclingState> = _uiState.asStateFlow()
    
    // Form state
    private val _photoUri = MutableStateFlow<String?>(null)
    val photoUri: StateFlow<String?> = _photoUri.asStateFlow()
    
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<MaterialCategory?>(null)
    val selectedCategory: StateFlow<MaterialCategory?> = _selectedCategory.asStateFlow()
    
    private val _selectedWeight = MutableStateFlow<WeightRange?>(null)
    val selectedWeight: StateFlow<WeightRange?> = _selectedWeight.asStateFlow()
    
    private val _totalPoints = MutableStateFlow(0)
    val totalPoints: StateFlow<Int> = _totalPoints.asStateFlow()
    
    /**
     * Handles photo capture result.
     * @param uri URI of the captured photo
     */
    fun onPhotoCaptured(uri: String) {
        _photoUri.value = uri
        _uiState.value = RecyclingState.PhotoCaptured(uri)
    }
    
    /**
     * Updates the description text.
     * @param text New description text
     */
    fun onDescriptionChanged(text: String) {
        _description.value = text
        
        // Auto-suggest category based on description
        if (text.length > 5 && _selectedCategory.value == null) {
            val suggestedCategory = MaterialCategory.suggestCategory(text)
            suggestedCategory?.let { category ->
                _selectedCategory.value = category
                _selectedWeight.value = WeightRange.getDefaultForCategory(category)
            }
        }
    }
    
    /**
     * Sets the selected material category.
     * @param category Selected material category
     */
    fun onCategorySelected(category: MaterialCategory) {
        _selectedCategory.value = category
        
        // Auto-select default weight for category if not set
        if (_selectedWeight.value == null) {
            _selectedWeight.value = WeightRange.getDefaultForCategory(category)
        }
    }
    
    /**
     * Sets the selected weight range.
     * @param weight Selected weight range
     */
    fun onWeightSelected(weight: WeightRange) {
        _selectedWeight.value = weight
    }
    
    /**
     * Calculates estimated points for current selection.
     * @return Estimated points or 0 if incomplete
     */
    fun getEstimatedPoints(): Int {
        val category = _selectedCategory.value ?: return 0
        val weight = _selectedWeight.value ?: return 0
        return weight.calculatePoints(category)
    }
    
    /**
     * Submits the recycling item for validation and points.
     * @param userId Current user ID
     */
    fun submitRecyclingItem(userId: String) {
        viewModelScope.launch {
            val photo = _photoUri.value
            val desc = _description.value
            val category = _selectedCategory.value
            val weight = _selectedWeight.value
            
            // Validate form completion
            if (!isFormValid()) {
                _uiState.value = RecyclingState.ValidationFailed(
                    message = getValidationMessage(),
                    canRetry = true
                )
                return@launch
            }
            
            _uiState.value = RecyclingState.Loading
            
            try {
                // Check daily limit
                when (val limitResult = repository.canSubmitToday(userId)) {
                    is Result.Success -> {
                        if (!limitResult.data) {
                            _uiState.value = RecyclingState.ValidationFailed(
                                message = "Limite diário de 5 registros atingido. Tente novamente amanhã!",
                                canRetry = false
                            )
                            return@launch
                        }
                    }
                    is Result.Error -> {
                        _uiState.value = RecyclingState.Error(
                            message = "Erro ao verificar limite diário",
                            exception = limitResult.exception
                        )
                        return@launch
                    }
                    Result.Loading -> { /* Continue */ }
                }
                
                // Upload photo first
                val uploadResult = repository.uploadPhoto(photo!!, userId)
                when (uploadResult) {
                    is Result.Success -> {
                        val remotePhotoUri = uploadResult.data
                        
                        // Create recycling item
                        val item = RecyclingItem.create(
                            userId = userId,
                            photoUri = remotePhotoUri,
                            description = desc,
                            category = category!!,
                            weightRange = weight!!
                        )
                        
                        // Submit for validation
                        repository.submitRecyclingItem(item).collect { result ->
                            when (result) {
                                is Result.Loading -> {
                                    _uiState.value = RecyclingState.Loading
                                }
                                is Result.Success -> {
                                    val validatedItem = result.data
                                    if (validatedItem.isValidated) {
                                        _uiState.value = RecyclingState.Success(
                                            item = validatedItem,
                                            pointsEarned = validatedItem.pointsEarned
                                        )
                                        // Update total points
                                        _totalPoints.value += validatedItem.pointsEarned
                                        // Reset form
                                        resetForm()
                                    } else {
                                        _uiState.value = RecyclingState.ValidationFailed(
                                            message = validatedItem.validationMessage ?: "Validação falhou",
                                            canRetry = true
                                        )
                                    }
                                }
                                is Result.Error -> {
                                    _uiState.value = RecyclingState.Error(
                                        message = "Erro ao submeter item: ${result.exception.message}",
                                        exception = result.exception
                                    )
                                }
                            }
                        }
                    }
                    is Result.Error -> {
                        _uiState.value = RecyclingState.Error(
                            message = "Erro ao carregar foto: ${uploadResult.exception.message}",
                            exception = uploadResult.exception
                        )
                    }
                    Result.Loading -> {
                        // Should not happen in upload, but handle gracefully
                        _uiState.value = RecyclingState.Loading
                    }
                }
                
            } catch (e: Exception) {
                _uiState.value = RecyclingState.Error(
                    message = "Erro inesperado: ${e.message}",
                    exception = e
                )
            }
        }
    }
    
    /**
     * Loads user's total points.
     * @param userId User ID to load points for
     */
    fun loadUserPoints(userId: String) {
        viewModelScope.launch {
            repository.getUserTotalPoints(userId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _totalPoints.value = result.data
                    }
                    is Result.Error -> {
                        // Silently fail for points loading
                        _totalPoints.value = 0
                    }
                    Result.Loading -> { /* Show loading if needed */ }
                }
            }
        }
    }
    
    /**
     * Resets the current UI state to idle.
     */
    fun resetState() {
        _uiState.value = RecyclingState.Idle
    }
    
    /**
     * Resets the form to initial state.
     */
    private fun resetForm() {
        _photoUri.value = null
        _description.value = ""
        _selectedCategory.value = null
        _selectedWeight.value = null
    }
    
    /**
     * Validates if the form is complete.
     * @return true if all required fields are filled
     */
    private fun isFormValid(): Boolean {
        return !_photoUri.value.isNullOrBlank() &&
                _description.value.length >= 10 &&
                _selectedCategory.value != null &&
                _selectedWeight.value != null
    }
    
    /**
     * Gets validation message for current form state.
     * @return Human-readable validation message
     */
    private fun getValidationMessage(): String {
        return when {
            _photoUri.value.isNullOrBlank() -> "Por favor, tire uma foto do item"
            _description.value.length < 10 -> "Descrição deve ter pelo menos 10 caracteres"
            _selectedCategory.value == null -> "Selecione a categoria do material"
            _selectedWeight.value == null -> "Selecione o peso aproximado"
            else -> "Preencha todos os campos obrigatórios"
        }
    }
}
