package ru.itis.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
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
import ru.itis.domain.model.CarbCoef
import ru.itis.presentation.R

@Composable
fun HeaderView(
    headerText: String,
    onClickItem: () -> Unit,
    isExpanded: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = if(isExpanded) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) else RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = headerText,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
            )

            IconButton(
                modifier = Modifier,
                onClick = {
                    onClickItem()
                }
            ) {
                if(isExpanded){
                    Icon(
                        painterResource(R.drawable.ic_arrow_up),
                        contentDescription = "icon_trash",
                        modifier = Modifier
                            .size(22.dp),
                        MaterialTheme.colorScheme.onSecondary
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.ic_arrow_down),
                        contentDescription = "icon_trash",
                        modifier = Modifier
                            .size(22.dp),
                        MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableViewCarbCoefs(
    listOfCarbCoef: List<CarbCoef>,
    isExpanded: Boolean,
    onEditClick: (Int) -> Unit,
    onSaveClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    newStartTime: String,
    newEndTime: String,
    newUnitPerHourValue: String,
    newOnUpClick: (Int) -> Unit,
    newOnDownClick: (Int) -> Unit,
    newOnCarbCoefValueChange: (String) -> Unit,

    itemStartTime: String,
    itemEndTime: String,
    itemUnitPerHourValue: String,
    itemOnUpClick: (Int) -> Unit,
    itemOnDownClick: (Int) -> Unit,
    itemOnCarbCoefValueChange: (String) -> Unit,
//    isBreadUnits: Boolean,
//    isMmolLiter: Boolean,
    expandedItem: Int?
) {
//    val measurementUnit = if(is)
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
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                .padding(top = 8.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            listOfCarbCoef.forEachIndexed { index, coef ->
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
                            text = "${coef.coef} " + stringResource(R.string.mmol_l_short),
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
                ExpandableCarbCoefItem(
                    isExpanded = index == expandedItem,
                    onSaveClick = onSaveClick,
                    onDeleteClick = onDeleteClick,
                    expandedItem = expandedItem,
                    itemStartTime = itemStartTime,
                    itemEndTime = itemEndTime,
                    itemUnitPerHourValue = itemUnitPerHourValue,
                    onUpItemClick = itemOnUpClick,
                    onDownItemClick = itemOnDownClick,
                    onCarbCoefValueChange = itemOnCarbCoefValueChange,
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
                    onAddClick = onAddClick,
                    startTime = newStartTime,
                    endTime = newEndTime,
                    unitPerHourValue = newUnitPerHourValue,
                    onUpClick = newOnUpClick,
                    onDownClick = newOnDownClick,
                    onCarbCoefValueChange = newOnCarbCoefValueChange
                )
            }
        }
    }
}

@Composable
fun ExpandableCarbCoefItem(
    isExpanded: Boolean,
    onSaveClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    expandedItem: Int?,
    itemStartTime: String,
    itemEndTime: String,
    itemUnitPerHourValue: String,
    onUpItemClick: (Int) -> Unit,
    onDownItemClick: (Int) -> Unit,
    onCarbCoefValueChange: (String) -> Unit,
) {
//    val measurementUnit = if(is)
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
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )
            Column(
                modifier = Modifier.fillMaxWidth()
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
                    NumberTextField(
                        modifier = Modifier.width(80.dp),
                        value = itemUnitPerHourValue,
                        onChange = {onCarbCoefValueChange(it)}
                    )
                    Text(
                        text = stringResource(R.string.unit_hour),
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
            }
        }
    }
}

@Composable
fun AddIntervalCard(
    onCloseClick: () -> Unit,
    startTime: String,
    endTime: String,
    unitPerHourValue: String,
    onUpClick: (Int) -> Unit,
    onDownClick: (Int) -> Unit,
    onCarbCoefValueChange: (String) -> Unit,
    onAddClick: () -> Unit
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
                    value = unitPerHourValue,
                    onChange = {onCarbCoefValueChange(it)}
                )
                Text(
                    text = stringResource(R.string.unit_hour),
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
