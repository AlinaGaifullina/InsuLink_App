package ru.itis.presentation.components.pump_settings.glucose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.components.NumberTextField
import ru.itis.presentation.components.VerticalArrowButton


@Composable
fun AddGlucoseIntervalCard(
    onCloseClick: () -> Unit,
    startTime: String,
    endTime: String,
    startValue: String,
    endValue: String,
    onUpClick: (Int) -> Unit,
    onDownClick: (Int) -> Unit,
    onStartValueChange: (String) -> Unit,
    onEndValueChange: (String) -> Unit,
    onAddClick: () -> Unit,
    measurementUnit: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.add_new_interval),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onCloseClick
                ) {
                    Icon(
                        painterResource(R.drawable.ic_close),
                        contentDescription = "icon_edit",
                        modifier = Modifier
                            .size(22.dp),
                        MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                VerticalArrowButton(
                    onUpClick = {onUpClick(0)},
                    onDownClick = {onDownClick(0)},
                    text = startTime,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text ="-",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                VerticalArrowButton(
                    onUpClick = {onUpClick(1)},
                    onDownClick = {onDownClick(1)},
                    text = endTime,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                NumberTextField(
                    modifier = Modifier.width(100.dp),
                    value = startValue,
                    onChange = {onStartValueChange(it)}
                )
                Text(
                    text = "-",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                NumberTextField(
                    modifier = Modifier.width(100.dp),
                    value = endValue,
                    onChange = {onEndValueChange(it)}
                )
                Text(
                    text = measurementUnit,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            BaseButton(
                text = stringResource(R.string.add),
                textColor = MaterialTheme.colorScheme.onTertiary,
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                onClick = onAddClick
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}