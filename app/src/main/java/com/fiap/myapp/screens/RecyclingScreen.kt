package com.fiap.myapp.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.fiap.myapp.R
import com.fiap.myapp.recycling.RecyclingState
import com.fiap.myapp.recycling.RecyclingViewModel
import com.fiap.myapp.recycling.models.MaterialCategory
import com.fiap.myapp.recycling.models.WeightRange
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File

private val Righteous = FontFamily(Font(R.font.righteous_regular))

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecyclingScreen(
    onNavigateToHistory: () -> Unit = {},
    onNavigateToPartners: () -> Unit = {},
    viewModel: RecyclingViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val photoUri by viewModel.photoUri.collectAsState()
    val description by viewModel.description.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedWeight by viewModel.selectedWeight.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()
    
    // Camera permissions
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )
    
    // Photo capture launcher
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            // In a real implementation, save bitmap to file and get URI
            // For now, use a mock URI
            val mockUri = "content://mock_photo_${System.currentTimeMillis()}"
            viewModel.onPhotoCaptured(mockUri)
        }
    }
    
    // Load user points on screen start
    LaunchedEffect(Unit) {
        viewModel.loadUserPoints("current_user_id") // In real app, get from auth
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(colors = listOf(WHITE, GREEN, BLUE))
            ),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            HeaderSection(
                totalPoints = totalPoints,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToPartners = onNavigateToPartners
            )
        }
        
        // Photo capture section
        item {
            PhotoCaptureSection(
                photoUri = photoUri,
                onCapturePhoto = {
                    if (cameraPermissionState.status.isGranted) {
                        photoLauncher.launch(null)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
                permissionState = cameraPermissionState
            )
        }
        
        // Description input
        item {
            DescriptionSection(
                description = description,
                onDescriptionChange = viewModel::onDescriptionChanged
            )
        }
        
        // Category selection
        item {
            CategorySection(
                selectedCategory = selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )
        }
        
        // Weight selection
        if (selectedCategory != null) {
            item {
                WeightSection(
                    selectedWeight = selectedWeight,
                    onWeightSelected = viewModel::onWeightSelected
                )
            }
        }
        
        // Points estimation
        if (selectedCategory != null && selectedWeight != null) {
            item {
                PointsEstimationSection(
                    estimatedPoints = viewModel.getEstimatedPoints()
                )
            }
        }
        
        // Submit button
        item {
            SubmitSection(
                uiState = uiState,
                onSubmit = {
                    viewModel.submitRecyclingItem("current_user_id") // In real app, get from auth
                },
                onResetState = viewModel::resetState
            )
        }
    }
}

@Composable
private fun HeaderSection(
    totalPoints: Int,
    onNavigateToHistory: () -> Unit,
    onNavigateToPartners: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CLEANWORLD",
                fontFamily = Righteous,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = GREEN,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Registre seus materiais recicláveis",
                fontSize = 16.sp,
                color = BLUE,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Points display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PointsCard(
                    title = "Seus Pontos",
                    points = totalPoints,
                    backgroundColor = GREEN
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToHistory,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BLUE),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Histórico", color = WHITE, fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = onNavigateToPartners,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = GREEN),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Parceiros", color = WHITE, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PointsCard(
    title: String,
    points: Int,
    backgroundColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = backgroundColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = points.toString(),
                fontSize = 24.sp,
                color = backgroundColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhotoCaptureSection(
    photoUri: String?,
    onCapturePhoto: () -> Unit,
    permissionState: com.google.accompanist.permissions.PermissionState
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📸 Foto do Material",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BLUE
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (photoUri != null) {
                // Show captured photo
                Card(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    // In real implementation, use AsyncImage with actual URI
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GREEN.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Foto capturada",
                            tint = GREEN,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = onCapturePhoto,
                    colors = ButtonDefaults.buttonColors(containerColor = BLUE),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tirar Nova Foto", color = WHITE)
                }
            } else {
                // Show camera capture button
                Button(
                    onClick = onCapturePhoto,
                    modifier = Modifier
                        .size(120.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GREEN),
                    shape = CircleShape
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tirar foto",
                            tint = WHITE,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Tirar Foto",
                            color = WHITE,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                if (permissionState.status.shouldShowRationale) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Permissão de câmera necessária para capturar foto",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📝 Descrição do Material",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BLUE
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: Garrafa PET transparente de refrigerante") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GREEN,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
            
            Text(
                text = "${description.length}/200 caracteres (mínimo 10)",
                fontSize = 12.sp,
                color = if (description.length >= 10) GREEN else Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun CategorySection(
    selectedCategory: MaterialCategory?,
    onCategorySelected: (MaterialCategory) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "🗂️ Categoria do Material",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BLUE
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MaterialCategory.values()) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = selectedCategory == category,
                        onSelected = { onCategorySelected(category) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: MaterialCategory,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    FilterChip(
        onClick = onSelected,
        label = { 
            Text(
                text = category.displayName,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = GREEN,
            selectedLabelColor = WHITE
        ),
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun WeightSection(
    selectedWeight: WeightRange?,
    onWeightSelected: (WeightRange) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WHITE.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "⚖️ Peso Aproximado",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BLUE
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeightRange.values().forEach { weight ->
                    WeightRadioButton(
                        weight = weight,
                        isSelected = selectedWeight == weight,
                        onSelected = { onWeightSelected(weight) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WeightRadioButton(
    weight: WeightRange,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) GREEN.copy(alpha = 0.1f) else Color.Transparent
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(selectedColor = GREEN)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = weight.displayName,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) GREEN else Color.Black
        )
    }
}

@Composable
private fun PointsEstimationSection(
    estimatedPoints: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GREEN.copy(alpha = 0.1f)),
        border = BorderStroke(2.dp, GREEN)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎯 Pontos Estimados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GREEN
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = estimatedPoints.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = GREEN
            )
            
            Text(
                text = "pontos",
                fontSize = 14.sp,
                color = GREEN
            )
        }
    }
}

@Composable
private fun SubmitSection(
    uiState: RecyclingState,
    onSubmit: () -> Unit,
    onResetState: () -> Unit
) {
    Column {
        when (uiState) {
            is RecyclingState.Loading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BLUE.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = BLUE,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Processando seu registro...",
                            color = BLUE,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            is RecyclingState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GREEN.copy(alpha = 0.1f)),
                    border = BorderStroke(2.dp, GREEN)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sucesso",
                            tint = GREEN,
                            modifier = Modifier.size(48.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Parabéns!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = GREEN
                        )
                        
                        Text(
                            text = "Você ganhou ${uiState.pointsEarned} pontos!",
                            fontSize = 16.sp,
                            color = GREEN,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = onResetState,
                            colors = ButtonDefaults.buttonColors(containerColor = GREEN),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Registrar Outro Item", color = WHITE, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            is RecyclingState.Error, is RecyclingState.ValidationFailed -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)),
                    border = BorderStroke(2.dp, Color.Red)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Erro",
                            tint = Color.Red,
                            modifier = Modifier.size(32.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = when (uiState) {
                                is RecyclingState.Error -> uiState.message
                                is RecyclingState.ValidationFailed -> uiState.message
                                else -> "Erro desconhecido"
                            },
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        
                        if (uiState is RecyclingState.ValidationFailed && uiState.canRetry) {
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Button(
                                onClick = onResetState,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Tentar Novamente", color = WHITE)
                            }
                        }
                    }
                }
            }
            
            else -> {
                // Default submit button
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BLUE),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Registrar Material",
                        color = WHITE,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
