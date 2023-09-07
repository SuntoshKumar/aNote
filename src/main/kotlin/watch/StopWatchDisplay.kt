import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Note
import io.realm.kotlin.ext.query
import screens.SAVE_WATCH
import screens.pref
import utils.RealmDatabase
import utils.insertNote
import utils.updateNote
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StopWatchDisplay(
    formattedTime: String,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResetClick: () -> Unit
) {
    var watchState by remember { mutableStateOf(false) }
    val autoSaveState by remember { mutableStateOf(pref.getBoolean(SAVE_WATCH,false)) }

    Box(contentAlignment = Alignment.TopEnd) {

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {

            if (autoSaveState){
                Surface(color = Color(0xffe8f5e9), shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)) {
                    Text("Auto save enabled", color = Color(0xff00c853), modifier = Modifier.padding(all = 8.dp))
                }
            }
        }


        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Surface(shape = CircleShape) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(400.dp)
                        .background(
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0x80f3e5f5),
                                    Color(0x80e3f2fd),
                                    Color(0xffe0f2f1),
                                    Color(0x99fbe9e7)
                                )
                            ), shape = CircleShape
                        )
                ) {

                    Text(
                        text = formattedTime,
                        fontWeight = FontWeight.Bold,
                        fontSize = 46.sp,
                        color = Color(0xff212121)
                    )
                }
            }


            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {


                OutlinedButton(onClick = {
                    watchState = if (watchState) {
                        onPauseClick()
                        if (autoSaveState){
                            saveToNote(formattedTime)
                        }
                        false
                    } else {
                        onStartClick()
                        true
                    }

                }, modifier = Modifier.size(76.dp), shape = CircleShape) {
                    Image(
                        imageVector = if (watchState) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(32.dp))

                OutlinedButton(onClick = { onResetClick() }, modifier = Modifier.size(76.dp), shape = CircleShape) {
                    Image(imageVector = Icons.Default.Refresh, contentDescription = null)
                }
            }


        }
    }


}

fun saveToNote(time:String){

    val date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).format(Date())
    val day = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    val timeNote = RealmDatabase.realm.query<Note>("date =='$day'").first().find()

    val currentTime = "$date     ==>      $time \n"


    if (timeNote == null){
        insertNote(Note().apply {
            this.date = day
            this.noteText = "StopWatch Records \n\n$currentTime"
        })
    }else{
        val timeText = "${timeNote.noteText} \n$currentTime"
        updateNote(Note().apply {
            this.date = day
            this.noteText = timeText
        })

    }


}