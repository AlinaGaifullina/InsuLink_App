package ru.itis.presentation.components.pump_settings.ins_sensitivity

import ru.itis.presentation.components.pump_settings.AddIntervalCard
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itis.domain.model.InsulinSensitivity
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton


@Composable
fun ExpandableViewInsSens(
    listOfInsSens: List<InsulinSensitivity>,
    isExpanded: Boolean,
    onEditClick: (Int) -> Unit,
    onSaveClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    isMmolLiter: Boolean,
    newStartTime: String,
    newEndTime: String,
    newValue: String,
    newOnUpClick: (Int) -> Unit,
    newOnDownClick: (Int) -> Unit,
    newOnInsSensValueChange: (String) -> Unit,
    expandedItem: Int?,
    itemStartTime: String,
    itemEndTime: String,
    itemValue: String,
    itemOnUpClick: (Int) -> Unit,
    itemOnDownClick: (Int) -> Unit,
    itemOnInsSensValueChange: (String) -> Unit
) {
    val measurementUnit = if(isMmolLiter) stringResource(R.string.mmol_l_short) else stringResource(R.string.mg_dl)

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

    var isAddCardVisible by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOfInsSens.forEachIndexed { index, coef ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(if(expandedItem == index) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) else RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = "${coef.startTime}-${coef.endTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "${coef.value} " + measurementUnit,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        IconButton(
                            modifier = Modifier,
                            onClick = {
                                onEditClick(index)
                            }
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_edit),
                                contentDescription = "icon_edit",
                                modifier = Modifier
                                    .size(18.dp),
                                MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                ExpandableInsSensItem(
                    isExpanded = index == expandedItem,
                    onSaveClick = onSaveClick,
                    onDeleteClick = onDeleteClick,
                    expandedItem = expandedItem,
                    itemStartTime = itemStartTime,
                    itemEndTime = itemEndTime,
                    itemValue = itemValue,
                    onUpItemClick = itemOnUpClick,
                    onDownItemClick = itemOnDownClick,
                    onInsSensValueChange = itemOnInsSensValueChange,
                    measurementUnit = measurementUnit
                )
            }
            if(!isAddCardVisible){
                Spacer(modifier = Modifier.height(16.dp))
                BaseButton(
                    text = stringResource(R.string.add),
                    textColor = MaterialTheme.colorScheme.onTertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {isAddCardVisible = true}
                )
            } else {
                AddIntervalCard(
                    onCloseClick = {isAddCardVisible = false},
                    onAddClick = {
                        onAddClick()
                        isAddCardVisible = false
                    },
                    startTime = newStartTime,
                    endTime = newEndTime,
                    unitPerHourValue = newValue,
                    onUpClick = newOnUpClick,
                    onDownClick = newOnDownClick,
                    onCarbCoefValueChange = newOnInsSensValueChange,
                    measurementUnit = measurementUnit,
                    backgroundColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


