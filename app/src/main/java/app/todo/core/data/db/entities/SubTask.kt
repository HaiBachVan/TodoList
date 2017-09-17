package app.todo.core.data.db.entities

class SubTask {

    var id: Int = 0
    var isComplete: Boolean = false
    var name: String? = null

    constructor(id: Int, isComplete: Boolean, name: String) {
        this.id = id
        this.isComplete = isComplete
        this.name = name
    }

    constructor(name: String, isComplete: Boolean) {
        this.isComplete = isComplete
        this.name = name
    }

}
