package utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Note
import screens.RadioOptions
import screens.mUpdateNote
import java.lang.Math.cos
import java.lang.Math.sin

@Composable
fun CustomRadioButton(
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "",
            color = Color.White,
            modifier = Modifier
                .clip(
                    shape = CircleShape,
                )
                .clickable {
                    onClick()
                }
                .background(color = color)
                .border(
                    width = 2.dp,
                    brush = if (isSelected) Brush.linearGradient(
                        listOf(
                            Color(0xff6200ea),
                            Color(0xff2962ff)
                        )
                    ) else Brush.linearGradient(
                        listOf(Color(0xffc5cae9), Color.Transparent)
                    ),
                    shape = CircleShape
                )
                .size(24.dp),
        )

    }
}


@Composable
fun BoldButton(
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(onClick = {
        onClick()
    }) {

        Text(
            text = "B",
            style = TextStyle(fontSize = 18.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Light)
        )
    }

}

@Composable
@Preview
fun CircularProgressBar() {
    val stroke = 4.dp
    val diameter = 50.dp
    val radius = diameter / 2

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(diameter)
    ) {
        Canvas(
            modifier = Modifier.size(diameter)
        ) {
            val sweepAngle = 360f * 0.8f
            val startAngle = 0f

            val centerX = size.width / 2
            val centerY = size.height / 2

            val innerRadius = radius - (stroke.toPx() / 2).dp
            val outerRadius = radius + (stroke.toPx() / 2).dp

            val arcStart = Offset(centerX, centerY - innerRadius.toPx())
            val arcEnd = Offset(
                centerX + sin(Math.toRadians(sweepAngle.toDouble())).toFloat() * outerRadius.toPx(),
                centerY - cos(Math.toRadians(sweepAngle.toDouble())).toFloat() * outerRadius.toPx()
            )

            drawArc(
                color = Color.Blue,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = stroke.toPx())
            )

            drawLine(
                color = Color.Blue,
                start = arcStart,
                end = arcEnd,
                strokeWidth = stroke.toPx(),
                cap = StrokeCap.Round
            )
        }

        CircularProgressIndicator(
            color = Color.White,
            modifier = Modifier.size(diameter - stroke)
        )
    }
}


@Composable
@Preview
fun CustomSearchBar(noteList: MutableState<List<Note>>) {

    var showClearButton by remember { mutableStateOf(false) }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var searchText by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf(getSelectedItem(NoteColor.DEFAULT.name)) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.weight(1f).padding(start = 32.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            shape = CircleShape,
            color = Color(0x99ffffff)
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        showClearButton = (focusState.isFocused)
                    }
                    .focusRequester(focusRequester),
                value = searchText,
                onValueChange = {
                    searchText = it
                    noteList.value = searchNote(it)
                    println(noteList.value.size)
                },
                placeholder = {
                    Text(text = "Search note..")
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = showClearButton,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = {
                            searchText = ""
                            noteList.value = getAllNoteList()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null
                            )
                        }

                    }
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
//        keyboardActions = KeyboardActions(onDone = {
//            keyboardController?.hide()
//        }),
            )

        }


        // To filter note by its color by using top color list
        RadioOptions.values().forEach {

            CustomRadioButton(
                isSelected = selectedOption == it,
                color = getNoteColor(it.name).second,
                onClick = {
                    selectedOption = it
                    val colorListName = getNoteColor(selectedOption.name).first
                    if (colorListName == NoteColor.DEFAULT.name){
                        noteList.value = getAllNoteList()
                    }else{
                        noteList.value = searchNoteByColorCode(colorListName)
                    }

                },)
        }
    }

}