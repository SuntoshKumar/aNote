package utils


import data.Note
import data.ToDo
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.text.SimpleDateFormat
import java.util.*


object RealmDatabase {

    private val config = RealmConfiguration
        .Builder(schema = setOf(Note::class, ToDo::class)).schemaVersion(1).build()//.create(schema = setOf(Note::class, ToDo::class))
    val realm = Realm.open(config)

}


fun insertToDo() {
    RealmDatabase.realm.writeBlocking {
        val date = SimpleDateFormat("dd/MM/yyyy_hh:mm:ss:SSS", Locale.getDefault()).format(Date())
        copyToRealm(ToDo().apply {
            this.date = date
        })
    }
}

fun updateToDoText(date: String, todoTexts: String) {
    RealmDatabase.realm.writeBlocking {
        query<ToDo>("date == '${date}'").first().find()?.apply {
            this.todoText = todoTexts
        }
    }
}

fun todoDone(date: String, isDone: Boolean) {
    RealmDatabase.realm.writeBlocking {
        query<ToDo>("date == '${date}'").first().find()?.apply {
            this.done = isDone
        }
    }

}

fun deleteToDo(date: String) {
    RealmDatabase.realm.writeBlocking {
        val toDo = query<ToDo>("date =='$date'").first().find()
        toDo?.let {
            delete(it)
        }
    }
}

fun getAllToDoList(): Flow<ResultsChange<ToDo>> {
    return RealmDatabase.realm.query<ToDo>().find().asFlow()
}


fun insertNote(note: Note) {
    if (note.noteText.isNotEmpty()) {
        RealmDatabase.realm.writeBlocking {
            copyToRealm(note)
        }
    }
}

fun updateNote(note: Note) {

    RealmDatabase.realm.writeBlocking {
        query<Note>(query = "date == '${note.date}'").first().find()?.apply {
            this.fontStyle = note.fontStyle
            this.fontSize = note.fontSize
            this.noteColor = note.noteColor
            this.italicFont = note.italicFont
            this.fontsFamily = note.fontsFamily
            this.noteText = note.noteText
        }
    }
}

fun deleteNote(note: Note) {
    RealmDatabase.realm.writeBlocking {
        val nn = query<Note>(query = "date == '${note.date}'").find()
        delete(nn)
    }
}

/***
 *  Search function using note text and date
 *  ***/
fun searchNote(searchText: String): List<Note> {
    val nList = mutableListOf<Note>()
    val list = RealmDatabase.realm.query<Note>().find().toList()
    list.forEach {
        if (it.noteText.lowercase().contains(searchText.lowercase()) || it.date.contains(searchText)) {
            nList.add(it)
        }
    }

    return nList
}

fun searchNoteByColorCode(noteColor: String = NoteColor.DEFAULT.name): List<Note> {
    return RealmDatabase.realm.query<Note>("noteColor TEXT $0", noteColor).find().toList()
}

//fun deleteAllNote() {
//    RealmDatabase.realm.writeBlocking {
//        deleteAll()
//    }
//}

fun getNoteLists(): Flow<ResultsChange<Note>> {

    return try {

        RealmDatabase.realm.query(Note::class).find().asFlow()
    } catch (e: Exception) {
        emptyList<ResultsChange<Note>>().asFlow()
    }
    // return RealmDatabase.realm.query(Note::class).find().asFlow()
}

fun getAllNoteList(): List<Note> {

    return RealmDatabase.realm.query(Note::class).find().toList()
}
