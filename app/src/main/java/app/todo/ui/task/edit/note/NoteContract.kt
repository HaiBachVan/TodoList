package app.todo.ui.task.edit.note

import app.todo.core.presentation.BaseContract

interface NoteContract {
    interface View : BaseContract.View

    interface Interaction : BaseContract.Interaction

    interface Presenter : BaseContract.Presenter<View> {
        fun onClickSaveNote(contentNote: String?)
    }
}