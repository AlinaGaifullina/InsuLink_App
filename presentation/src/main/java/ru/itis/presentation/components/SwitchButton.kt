package ru.itis.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SwitchButton(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    backgroundColor: Color,
    textSelectedColor: Color,
    textBackgroundColor: Color,
    height: Dp,
    titleFirst: String,
    titleSecond: String,
    onSelected: (Boolean) -> Unit
) {
    var isFirstSelected by remember { mutableStateOf(true) }
    val indicatorOffset by animateDpAsState(
        targetValue = if (isFirstSelected) 0.dp else 142.dp,
        label = "Gender switch animation"
    )
    Box(
        modifier = modifier
            .width(300.dp)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(8.dp)
    ) {
        // Анимированный индикатор выбора (подсвеченная часть)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = indicatorOffset)
        ){
            Box(
                modifier = Modifier
                    .width(142.dp)
                    .height(height - 16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(selectedColor)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(height - 16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        isFirstSelected = true
                        onSelected(true)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = titleFirst,
                    color = if (isFirstSelected) textSelectedColor else textBackgroundColor,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(height - 16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        isFirstSelected = false
                        onSelected(false)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = titleSecond,
                    color = if (!isFirstSelected) textSelectedColor else textBackgroundColor,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}