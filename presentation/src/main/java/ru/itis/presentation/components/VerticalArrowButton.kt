package ru.itis.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.itis.presentation.R

@Composable
fun VerticalArrowButton(
    onUpClick: () -> Unit,
    onDownClick: () -> Unit,
    text: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier,
            onClick = onUpClick
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_up),
                contentDescription = "icon_edit",
                modifier = Modifier
                    .size(22.dp),
                MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
        )
        IconButton(
            modifier = Modifier,
            onClick = onDownClick
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_down),
                contentDescription = "icon_edit",
                modifier = Modifier
                    .size(22.dp),
                MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}