package com.fiap.myapp.recycling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.myapp.recycling.models.RecyclingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para carregar o histórico de reciclagem do usuário.
 */
class HistoryViewModel(
    private val repository: RecyclingRepository = FirebaseRecyclingRepository()
) : ViewModel() {

    private val _historyState = MutableStateFlow<Result<List<RecyclingItem>>>(Result.Loading)
    val historyState: StateFlow<Result<List<RecyclingItem>>> = _historyState.asStateFlow()

    fun loadHistory(userId: String) {
        viewModelScope.launch {
            try {
                repository.getUserRecyclingHistory(userId).collect { result ->
                    _historyState.value = result
                }
            } catch (e: Exception) {
                _historyState.value = Result.Error(e)
            }
        }
    }
}
