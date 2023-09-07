package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cNote
import data.Note
import navigation.NavController
import screens.currentNote
import screens.databaseStatus
import utils.*
import java.util.prefs.Preferences


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
@Preview
fun NoteCard(navController: NavController, note: Note, preferences: Preferences, firstVisibleItemIndex: Int) {

    var expanded by remember { mutableStateOf(false) }
    var visibleDelDialog by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current


    Surface(
        modifier = Modifier.width(250.dp).height(160.dp).padding(all = 8.dp)
            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = {
                    // ...
                    expanded = true
                }
            )
            .clickable {
                databaseStatus = DatabaseStatus.UPDATE
                cNote = note
                currentNote = note
                navController.navigate("EditNoteView")
                preferences.putInt("scroll_position", firstVisibleItemIndex)
            },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        color = applyNoteColor(note.noteColor).first
    ) {
        Box {
            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(all = 10.dp),
                    text = note.noteText, maxLines = 6,
                    color = applyNoteColor(note.noteColor).second,
                    fontSize = 14.sp,
                    fontFamily = applyFontFamily(note.fontsFamily)
                )

                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                    text = note.date.substringBefore("_"),
                    fontSize = 12.sp,
                    color = applyNoteColor(note.noteColor).second
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(
                    color = applyNoteColor(note.noteColor).first
                )
            ) {

                DropdownMenuItem(onClick = {
                    expanded = false
                    clipboardManager.setText(AnnotatedString(note.noteText))
                }
                ) {
                    Text("Copy")

                }

                DropdownMenuItem(onClick = {
                    expanded = false

                    saveAsText(note)

                }) {
                    Text("Save as text file")

                }

                DropdownMenuItem(onClick = {
                    expanded = false

                    saveSingleNoteAsPdf(note)

                }) {
                    Text("Save as pdf")

                }

                DropdownMenuItem(onClick = {
                    expanded = false
                    visibleDelDialog = true
                }) {
                    Text("Delete")

                }

            }
        }


    }

    if (visibleDelDialog) {
        AlertDialog(onDismissRequest = { visibleDelDialog = false },
            confirmButton = {

                OutlinedButton(onClick = {
                    deleteNote(note)
                    visibleDelDialog = false
                }) {
                    Text("Delete")
                }

            },
            dismissButton = {
                TextButton(onClick = { visibleDelDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete") },
            text = { Text("Are you sure you want to delete permanently?") },
            shape = RoundedCornerShape(20.dp)
        )
    }

}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
@Preview
fun LinearNoteCard(navController: NavController, note: Note, preferences: Preferences, firstVisibleItemIndex: Int) {

    var expanded by remember { mutableStateOf(false) }
    var visibleDelDialog by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current


    Surface(
        modifier = Modifier.fillMaxWidth().height(160.dp).padding(all = 8.dp)
            .onClick(
                matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = {
                    // ...
                    expanded = true
                }
            )
            .clickable {
                databaseStatus = DatabaseStatus.UPDATE
                cNote = note
                currentNote = note
                navController.navigate("EditNoteView")
                preferences.putInt("scroll_position", firstVisibleItemIndex)
            },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        color = applyNoteColor(note.noteColor).first
    ) {
        Box {
            Column(modifier = Modifier.fillMaxSize()) {

                Text(
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(all = 10.dp),
                    text = note.noteText, maxLines = 6,
                    color = applyNoteColor(note.noteColor).second,
                    fontSize = 14.sp,
                    fontFamily = applyFontFamily(note.fontsFamily)
                )

                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
                    text = note.date.substringBefore("_"),
                    fontSize = 12.sp,
                    color = applyNoteColor(note.noteColor).second
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(
                    color = applyNoteColor(note.noteColor).first
                )
            ) {

                DropdownMenuItem(onClick = {
                    expanded = false
                    clipboardManager.setText(AnnotatedString(note.noteText))
                }
                ) {
                    Text("Copy")

                }

                DropdownMenuItem(onClick = {
                    expanded = false

                    saveAsText(note)

                }) {
                    Text("Save as text file")

                }

                DropdownMenuItem(onClick = {
                    expanded = false

                    saveSingleNoteAsPdf(note)

                }) {
                    Text("Save as pdf")

                }

                DropdownMenuItem(onClick = {
                    expanded = false
                    visibleDelDialog = true
                }) {
                    Text("Delete")

                }

            }
        }


    }

    if (visibleDelDialog) {
        AlertDialog(onDismissRequest = { visibleDelDialog = false },
            confirmButton = {

                OutlinedButton(onClick = {
                    deleteNote(note)
                    visibleDelDialog = false
                }) {
                    Text("Delete")
                }

            },
            dismissButton = {
                TextButton(onClick = { visibleDelDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete") },
            text = { Text("Are you sure you want to delete permanently?") },
            shape = RoundedCornerShape(20.dp)
        )
    }

}
