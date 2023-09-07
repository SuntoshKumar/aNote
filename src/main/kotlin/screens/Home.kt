package screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import data.Note
import navigation.NavController
import ui.LinearNoteCard
import ui.NoteCard
import utils.CustomSearchBar
import utils.DatabaseStatus
import utils.getNoteLists
import java.util.prefs.Preferences

@Composable
@Preview
fun HomeScreen(
    navController: NavController
) {
    val preferences = Preferences.userRoot().node("your_app_namespace")
    val savedScrollPosition = preferences.getInt("scroll_position", 0)
    //noteList = getNoteList()
    val useLinearLayout by remember { mutableStateOf(pref.getBoolean(USE_LINEAR_LAYOUT, false)) }
    val lazyGridState = rememberLazyGridState(initialFirstVisibleItemIndex = savedScrollPosition)

    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = savedScrollPosition)

    val noteList = remember { mutableStateOf(listOf<Note>()) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {

        Column(modifier = Modifier.fillMaxSize()) {



            if (useLinearLayout) {

                LazyColumn(modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    userScrollEnabled = true) {

                    item(contentType = "header") {
                        CustomSearchBar(noteList)
                    }

                    itemsIndexed(noteList.value) { _, item ->

                        LinearNoteCard(navController, item, preferences, lazyListState.firstVisibleItemIndex)
                    }
                }

            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    modifier = Modifier.fillMaxWidth(),
                    userScrollEnabled = true,
                    state = lazyGridState
                ) {

                    item(contentType = "header", span = { GridItemSpan(maxLineSpan) }) {
                        CustomSearchBar(noteList)
                    }

                    itemsIndexed(noteList.value) { _, item ->

                        NoteCard(navController, item, preferences, lazyGridState.firstVisibleItemIndex)
                    }

                }
            }


        }



        FloatingActionButton(
            onClick = {

                databaseStatus = DatabaseStatus.CREATE
                currentNote = Note()
                navController.navigate("EditNoteView")
            },
            backgroundColor = Color(0xff00e676),
            modifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
        ) {
            Image(
                imageVector = Icons.Default.Add,
                colorFilter = ColorFilter.tint(color = Color.White),
                contentDescription = null
            )
        }
    }

    LaunchedEffect(Unit) {
        getNoteLists().collect {
            noteList.value = it.list.toList()
        }
    }
}