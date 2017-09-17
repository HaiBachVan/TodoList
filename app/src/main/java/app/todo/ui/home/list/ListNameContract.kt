package app.todo.ui.home.list

import app.todo.core.data.db.entities.ListName
import app.todo.core.presentation.BaseContract

interface ListNameContract {
    interface View : BaseContract.View

    interface Interaction : BaseContract.Interaction {
        fun insertListName(listName: ListName)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun onClickSaveNameGroup(nameGroup: String?)
    }

    interface Router : BaseContract.Router
}