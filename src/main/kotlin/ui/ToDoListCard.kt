package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.ToDo
import utils.applyNoteColor
import utils.deleteToDo
import utils.todoDone
import utils.updateToDoText

@Composable
@Preview
fun ToDoListCard(toDo: ToDo) {

    var todoText by remember { mutableStateOf(toDo.todoText) }
    var isDone by remember { mutableStateOf(toDo.done) }

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CustomCheckBox(
            checked = isDone, onCheckedChange = {
                isDone = it
                todoDone(toDo.date, it)
            },
            modifier = Modifier.padding(start = 32.dp, end = 16.dp)
        )

        Surface(
            modifier = Modifier.padding(all = 8.dp).weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = if (isDone) Color(0xffc8e6c9) else MaterialTheme.colors.surface
        ) {

            TextField(
                value = todoText,
                onValueChange = {
                    todoText = it
                    updateToDoText(toDo.date, it)
                },
                placeholder = { Text("Write you TODO..") },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.Default,
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                ),

                modifier = Modifier.fillMaxWidth()
                    .background(Color.Transparent),

                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Gray,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        IconButton(
            onClick = {
                      deleteToDo(date = toDo.date)

            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Image(imageVector = Icons.Rounded.Clear, contentDescription = null, alpha = 0.6f)
        }


    }


}

@Composable
fun CustomCheckBox(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier) {

    var isChecked by remember { mutableStateOf(checked) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(
            color = if (isChecked) Color(0xff00c853) else Color.Transparent,
            shape = CircleShape
        )
            .border(width = 1.dp, color = Color(0xff00c853), shape = CircleShape)
            .clickable {
                isChecked = !isChecked
                onCheckedChange(isChecked)
            }
    ) {

        Image(
            imageVector = Icons.Rounded.Check, contentDescription = null,
            colorFilter = ColorFilter.tint(color = Color.White),
            alpha = if (isChecked) 1f else 0f,
            modifier = Modifier.size(28.dp).padding(all = 4.dp)
        )
    }

}