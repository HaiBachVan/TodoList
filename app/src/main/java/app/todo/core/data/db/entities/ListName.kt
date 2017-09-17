package app.todo.core.data.db.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey


@Entity(
        tableName = "list",
        indices = arrayOf(
                Index(
                        value = *arrayOf("name"),
                        unique = true
                )
        )
)
data class ListName(
        var name: String? = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(id: Int, name: String) : this() {
        this.id = id
        this.name = name
    }

}
