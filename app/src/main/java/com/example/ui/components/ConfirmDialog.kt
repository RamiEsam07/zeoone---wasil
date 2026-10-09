package com.example.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.Slate600
import com.example.ui.theme.StatusDanger

@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    isDestructive: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Slate600
            )
        },
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirm,
                colors = if (isDestructive) {
                    ButtonDefaults.filledTonalButtonColors(
                        containerColor = StatusDanger,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                } else ButtonDefaults.filledTonalButtonColors(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(end = 4.dp)
                    .testTag("dialog_confirm_button")
            ) {
                Text(text = confirmText, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("dialog_cancel_button")
            ) {
                Text(text = cancelText)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}
