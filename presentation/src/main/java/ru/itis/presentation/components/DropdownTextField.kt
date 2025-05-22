package ru.itis.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.itis.presentation.R


@Composable
fun DropdownTextField(
    selectedItem: String,
    itemList: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    textColor: Color,
    textStyle: TextStyle,
    iconColor: Color,
    textFieldModifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth()) {
        // Поле ввода с выпадающим списком
        OutlinedTextField(
            value = selectedItem ?: "Выберите инсулин",
            shape = if (!expanded) RoundedCornerShape(8.dp) else RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_down),
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true },
                    tint = iconColor
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.labelLarge,
            modifier = textFieldModifier
                .fillMaxWidth()
        )
        // Выпадающее меню
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(200.dp)
                .background(backgroundColor),
            shadowElevation = 0.dp,
        ) {
            itemList.forEach { insulin ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = insulin,
                                style = MaterialTheme.typography.labelLarge,
                                color = textColor
                            )
                        }
                    },
                    onClick = {
                        onSelect(insulin)
                        expanded = false
                    }
                )
            }
        }
    }
}