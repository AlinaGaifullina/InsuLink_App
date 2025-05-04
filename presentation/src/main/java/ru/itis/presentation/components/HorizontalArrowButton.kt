package ru.itis.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.itis.presentation.R


@Composable
fun HorizontalArrowButton(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onTextValueChange: (String) -> Unit,
    textValue: String,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevatedButton(
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            onClick = onLeftClick,
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 4.dp / 2
            ),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_left),
                contentDescription = "icon_edit",
                modifier = Modifier
                    .size(22.dp),
                MaterialTheme.colorScheme.onPrimary
            )
        }
        FloatNumberTextField(
            modifier = Modifier.width(200.dp).padding(horizontal = 16.dp),
            value = textValue,
            onChange = {onTextValueChange(it)}
        )
        ElevatedButton(
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            onClick = onRightClick,
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 4.dp / 2
            ),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_right),
                contentDescription = "icon_edit",
                modifier = Modifier
                    .size(22.dp),
                MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}