package ru.itis.presentation.components.pump_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
