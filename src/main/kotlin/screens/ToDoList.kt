package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.ToDo
import navigation.NavController
import ui.ToDoListCard
import utils.displayTray
import utils.getAllToDoList
import utils.insertToDo


@Composable
fun ToDoList(navController: NavController) {

    var toDoList by remember { mutableStateOf(listOf<ToDo>()) }

    Column(modifier = Modifier.fillMaxSize()){


        LazyColumn {

            items(toDoList){
                ToDoListCard(it)
            }

            item {
                FloatingActionButton(onClick = {
                    insertToDo()
                },
                    shape = RoundedCornerShape(20.dp),
                    backgroundColor = Color(0xff00e676),
                    modifier = Modifier.padding(start = 86.dp, bottom = 24.dp)
                ){
                    Image(imageVector = Icons.Rounded.AddTask, contentDescription = null, alpha = 0.8f)
                }
            }
        }


    }

    LaunchedEffect(Unit){
        getAllToDoList().collect {
           toDoList = it.list.toList()
        }
    }

}


