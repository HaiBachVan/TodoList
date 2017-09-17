package app.todo.ui.home.list

import app.todo.core.data.db.entities.ListName
import app.todo.core.data.db.repository.ListNameRepository
import javax.inject.Inject

class ListNameInteraction @Inject constructor(val repository: ListNameRepository) : ListNameContract.Interaction {
    override fun insertListName(listName: ListName) {
        repository.insertListName(listName)
    }
}