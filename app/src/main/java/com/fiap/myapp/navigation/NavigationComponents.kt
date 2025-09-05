package com.fiap.myapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.myapp.ui.theme.BLUE
import com.fiap.myapp.ui.theme.GREEN
import com.fiap.myapp.ui.theme.WHITE

// UI Constants
private val LOADING_INDICATOR_SIZE = 48.dp
private val CONTENT_SPACING = 16.dp

/**
 * Loading screen component displayed during authentication state checks.
 * Maintains visual consistency with the app's branding.
 */
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = listOf(GREEN, BLUE))),
        contentAlignment = Alignment.Center
    ) {
        LoadingContent()
    }
}

@Composable
private fun LoadingContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = WHITE,
            modifier = Modifier.size(LOADING_INDICATOR_SIZE)
        )
        
        Spacer(modifier = Modifier.height(CONTENT_SPACING))
        
        BrandTitle()
        
        StatusMessage(
            message = "Checking authentication...",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun BrandTitle() {
    Text(
        text = "CLEANWORLD",
        color = WHITE,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun StatusMessage(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        color = WHITE.copy(alpha = 0.8f),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

/**
 * Error dialog component for displaying authentication errors.
 * Provides clear error messaging and optional retry functionality.
 */
@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit,
    onRetry: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Authentication Error",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        dismissButton = onRetry?.let { retry ->
            {
                TextButton(
                    onClick = {
                        retry()
                        onDismiss()
                    }
                ) {
                    Text("Try Again")
                }
            }
        }
    )
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen()
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    ErrorDialog(
        message = "Failed to authenticate user",
        onDismiss = {},
        onRetry = {}
    )
}
