package ru.itis.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun FloatNumberTextField(modifier: Modifier, value: String, onChange: (String) -> Unit) {
    Column {
        val numberRegex = remember { Regex("^\\d*[.,]?\\d*$") }

        OutlinedTextField(
            value = value.replace(",", "."),  // используем точку для отображения
            onValueChange = { newValue ->
                when {
                    newValue.isEmpty() -> onChange("")
                    numberRegex.matches(newValue) -> {
                        val processedValue = when {
                            newValue == "." || newValue == "," -> "0."
                            newValue.startsWith("0") && newValue.length > 1 &&
                                    !newValue.startsWith("0.") && !newValue.startsWith("0,") ->
                                newValue.drop(1).takeIf { it.isNotEmpty() } ?: "0"
                            else -> newValue.replace(",", ".")
                        }
                        onChange(processedValue)
                    }
                }
            },
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                textAlign = TextAlign.Center
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}