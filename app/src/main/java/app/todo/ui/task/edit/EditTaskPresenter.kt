package app.todo.ui.task.edit

import app.todo.core.presentation.BasePresenter
import javax.inject.Inject

class EditTaskPresenter @Inject constructor(val mActivity: EditTaskActivity, val interactor: EditTaskContract.Interaction)
    : BasePresenter<EditTaskContract.View>(), EditTaskContract.Presenter {

}