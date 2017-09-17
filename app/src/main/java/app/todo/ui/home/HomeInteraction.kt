package app.todo.ui.home

import android.arch.lifecycle.LiveData
import app.todo.R
import app.todo.core.data.db.entities.ListName
import app.todo.core.data.db.repository.ListNameRepository
import app.todo.core.data.db.repository.TaskRepository
import javax.inject.Inject

class HomeInteraction @Inject constructor(
        val context: HomeActivity,
        val repository: ListNameRepository,
        val taskRepository: TaskRepository
) : HomeContract.Interaction {

    override fun initDb() {
        val list = ArrayList<ListName>()
        list.add(ListName(context.getString(R.string.text_work)))
        list.add(ListName(context.getString(R.string.text_travel)))
        list.add(ListName(context.getString(R.string.text_family)))
        list.add(ListName(context.getString(R.string.text_movies_to_watch)))
        list.add(ListName(context.getString(R.string.text_private)))

        repository.insertAllListName(list)
    }

    override fun getNumOfListNameFromDb(): Int = repository.getNumOfListName()

    override fun getAllListNameWithLiveData(): LiveData<List<ListName>> = repository.getAllListNameWithLiveData()

    override fun getNumOfTaskTodayFromDb(): Int {
        // TODO: demo
        return 1
    }
}