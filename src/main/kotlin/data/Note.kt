package data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Note() : RealmObject {
    @PrimaryKey
    var _id:ObjectId = ObjectId()
    var fontSize:Int = 0
    var fontStyle:Int = 0
    var color:Int = 0
    var date:String = ""
    var noteText:String = ""
}