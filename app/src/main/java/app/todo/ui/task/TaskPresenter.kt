package app.todo.ui.task

import android.arch.lifecycle.LiveData
import app.todo.core.data.db.entities.Task
import app.todo.core.presentation.BasePresenter
import javax.inject.Inject

class TaskPresenter @Inject constructor(val activity: TaskActivity, val interaction: TaskContract.Interaction)
    : BasePresenter<TaskContract.View>(), TaskContract.Presenter {

    override fun onClickAddToDo(task: Task) {
        interaction.insertTask(task)
    }

    override fun onViewInitialized(id: Int): LiveData<List<Task>> = interaction.loadAllTaskWithId(id)
}