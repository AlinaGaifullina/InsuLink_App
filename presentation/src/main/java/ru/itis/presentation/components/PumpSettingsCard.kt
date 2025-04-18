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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
                .padding(horizontal = 20.dp, vertical = 8.dp)
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

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                .padding(16.dp),
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
                        text = "${coef.startHour}:${coef.startMinute}-${coef.endHour}:${coef.endMinute}",
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
                                    .size(22.dp),
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

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandTransition,
        exit = collapseTransition
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("развернут")
        }
    }
}