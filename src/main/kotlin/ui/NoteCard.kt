package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Note



@Composable
@Preview
fun NoteCard(note: Note, index: Int) {

    Surface(
        modifier = Modifier.width(200.dp).height(160.dp).padding(all = 8.dp)
            .clickable {
                UpdateNoteWindow.updateNoteWindowState.value = true
                UpdateNoteWindow.note = note
                println("Clicked")
            },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            Text(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(all = 8.dp),
                text = note.noteText, maxLines = 4
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                text = note.date,
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }


    }



}