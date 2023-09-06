package utils

import data.Note
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.delete
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


object RealmDatabase {

    private val config = RealmConfiguration.create(schema = setOf(Note::class))
    val realm = Realm.open(config)
}

fun insertNote(fontSize: Int = 0, fontStyle: Int = 0, fontColor: Int = 0, text: String) {

    if (text.isEmpty()) return
    val currentDate = SimpleDateFormat("dd/mm/yyyy_hh:mm:ss:SSS", Locale.US).format(Date())

    RealmDatabase.realm.writeBlocking {
        copyToRealm(Note().apply {
            this.fontStyle = fontSize
            this.fontStyle = fontStyle
            this.color = fontColor
            this.date = currentDate
            this.noteText = text
        })
    }
}

fun updateNote(note: Note) {

    RealmDatabase.realm.writeBlocking {
        query<Note>(query = "date == '${note.date}'").first().find()?.apply {
            this.fontStyle = note.fontStyle
            this.fontSize = note.fontSize
            this.color = note.color
            this.noteText = note.noteText
        }
    }
}

fun deleteNote(note: Note){
    RealmDatabase.realm.writeBlocking {
        val nn = query<Note>(query = "date == '${note.date}").find()
        delete(nn)
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun getNoteList(): List<Note> {

    return RealmDatabase.realm.query(Note::class).find().toList()


}
