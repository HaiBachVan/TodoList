package app.todo.core.data.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(
        tableName = "task",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = ListName::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("listNameId"),
                        onDelete = ForeignKey.CASCADE
                )
        )
)
data class Task(
        var isComplete: Boolean = false,
        var name: String? = null) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var listNameId: Int = 0

    constructor(id: Int, isComplete: Boolean, name: String) : this() {
        this.id = id
        this.isComplete = isComplete
        this.name = name
    }

    constructor(name: String, isComplete: Boolean) : this() {
        this.isComplete = isComplete
        this.name = name
    }

    constructor(listNameId: Int, name: String, isComplete: Boolean) : this() {
        this.isComplete = isComplete
        this.name = name
        this.listNameId = listNameId
    }

}
