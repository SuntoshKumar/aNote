package data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ToDo : RealmObject {

    @PrimaryKey
    var _id:ObjectId = ObjectId()
    var date:String = ""
    var todoText:String = ""
    var done:Boolean = false
}