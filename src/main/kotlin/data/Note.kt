package data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.FullText
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import utils.FontWeightsEnum
import utils.NoteColor

class Note() : RealmObject {
    @PrimaryKey
    var _id:ObjectId = ObjectId()
    var fontSize:Int = 14
    var fontStyle:String = FontWeightsEnum.NORMAL.name
    var fontsFamily: String = ""
    var italicFont:Boolean = false
    @FullText
    var noteColor:String = NoteColor.DEFAULT.name
    var date:String = ""
    @FullText
    var noteText:String =""
}