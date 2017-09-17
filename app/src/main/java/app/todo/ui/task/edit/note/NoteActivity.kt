package app.todo.ui.task.edit.note

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import app.todo.R
import app.todo.core.presentation.BaseActivity
import app.todo.util.AppConstants
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : BaseActivity<NotePresenter, NoteContract.View>(), NoteContract.View, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initInjection() {
        AndroidInjection.inject(this)
    }

    override val layout: Int
        get() = R.layout.activity_note

    private fun initView() {
        // show keyboard
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        val contentNote = intent?.getStringExtra(AppConstants.ARG_NOTE)
        val taskName = intent?.getStringExtra(AppConstants.ARG_NAME_TASK)

        title_activity?.text = taskName

        if (contentNote != "") {
            content_note?.setText(contentNote)
        }

        buttonSave?.setOnClickListener(this)
        btnBackLayout?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val id = view?.id

        if (id == R.id.buttonSave) {
            onClickSaveNote()
        } else if (id == R.id.btnBackLayout) {
            super.onBackPressed()
        }
    }

    private fun onClickSaveNote() {
        var contentNote: String? = content_note!!.text.toString().trim { it <= ' ' }

        if (contentNote!!.isEmpty()) {
            contentNote = null
        }

        mPresenter.onClickSaveNote(contentNote)
    }

}
