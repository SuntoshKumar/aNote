package screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigation.NavController

@Composable
@Preview
fun EditNoteView(
     navController: NavController
) {

    Column(modifier = Modifier.fillMaxSize()) {

        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            IconButton(onClick = {}) {
                Image(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }

        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {

            BasicTextField(value = "", onValueChange = {})
        }
    }

}