package ru.itis.presentation.components.pump_settings.glucose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.components.FloatNumberTextField
import ru.itis.presentation.components.VerticalArrowButton


@Composable
fun ExpandableGlucoseItem(
    isExpanded: Boolean,
    onSaveClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    expandedItem: Int?,
    itemStartTime: String,
    itemEndTime: String,
    itemStartValue: String,
    itemEndValue: String,
    onUpItemClick: (Int) -> Unit,
    onDownItemClick: (Int) -> Unit,
    onGlucoseStartValueChange: (String) -> Unit,
    onGlucoseEndValueChange: (String) -> Unit,
    measurementUnit: String,
) {
    // Opening Animation
    val expandTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(
            animationSpec = tween(300)
        )
    }

    // Closing Animation
    val collapseTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(
            animationSpec = tween(300)
        )
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    VerticalArrowButton(
                        onUpClick = {onUpItemClick(0)},
                        onDownClick = {onDownItemClick(0)},
                        text = itemStartTime,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text ="-",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    VerticalArrowButton(
                        onUpClick = {onUpItemClick(1)},
                        onDownClick = {onDownItemClick(1)},
                        text = itemEndTime,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    FloatNumberTextField(
                        modifier = Modifier.width(80.dp),
                        value = itemStartValue,
                        onChange = {onGlucoseStartValueChange(it)}
                    )
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    FloatNumberTextField(
                        modifier = Modifier.width(80.dp),
                        value = itemEndValue,
                        onChange = {onGlucoseEndValueChange(it)}
                    )
                    Text(
                        text = measurementUnit,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                }
                BaseButton(
                    text = stringResource(R.string.save),
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = {
                        if(expandedItem != null){
                            onSaveClick(expandedItem)
                        }
                    }
                )
                BaseButton(
                    text = stringResource(R.string.delete),
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = {
                        if(expandedItem != null){
                            onDeleteClick(expandedItem)
                        }
                    }
                )
            }
        }
    }
}