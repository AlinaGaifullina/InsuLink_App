package ru.itis.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import ru.itis.domain.model.HistorySizeType

@Composable
fun ThreeButtonsRow(
    text1: String,
    text2: String,
    text3: String,
    onButtonClick: (HistorySizeType) -> Unit,
) {
    var selectedButtonNumber by remember { mutableIntStateOf(2) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedButton(
            onClick = {
                onButtonClick(HistorySizeType.DAY)
                selectedButtonNumber = 1
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if(selectedButtonNumber == 1) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.primary // Фон кнопки
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (selectedButtonNumber == 1) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.surfaceTint
            )
        ){
            Text(
                text = text1,
                color = if(selectedButtonNumber == 1) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.surfaceTint,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        OutlinedButton(
            onClick = {
                onButtonClick(HistorySizeType.WEEK)
                selectedButtonNumber = 2
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if(selectedButtonNumber == 2) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.primary // Фон кнопки
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (selectedButtonNumber == 2) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.surfaceTint
            )
        ){
            Text(
                text = text2,
                color = if(selectedButtonNumber == 2) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.surfaceTint,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }

        OutlinedButton(
            onClick = {
                onButtonClick(HistorySizeType.MONTH1)
                selectedButtonNumber = 3
            },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if(selectedButtonNumber == 3) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.primary // Фон кнопки
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (selectedButtonNumber == 3) MaterialTheme.colorScheme.surfaceTint else MaterialTheme.colorScheme.surfaceTint
            )
        ){
            Text(
                text = text3,
                color = if(selectedButtonNumber == 3) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.surfaceTint,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}