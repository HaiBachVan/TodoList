package app.todo.ui.task.edit

import app.todo.core.presentation.BaseContract

interface EditTaskContract {
    interface View : BaseContract.View

    interface Interaction : BaseContract.Interaction

    interface Presenter : BaseContract.Presenter<View>

}