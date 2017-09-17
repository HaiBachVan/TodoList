package app.todo.ui.task.edit.note

import android.app.Activity
import android.content.Intent
import app.todo.core.presentation.BasePresenter
import app.todo.ui.task.edit.EditTaskActivity
import app.todo.util.AppConstants
import javax.inject.Inject

class NotePresenter @Inject constructor(val mActivity: NoteActivity, val mInteractor: NoteContract.Interaction)
    : BasePresenter<NoteContract.View>(), NoteContract.Presenter {

    override fun onClickSaveNote(contentNote: String?) {
        val intent = Intent(mActivity, EditTaskActivity::class.java)

        intent.putExtra(AppConstants.ARG_NOTE, contentNote)
        mActivity.setResult(Activity.RESULT_OK, intent)

        mActivity.finish()
    }
}