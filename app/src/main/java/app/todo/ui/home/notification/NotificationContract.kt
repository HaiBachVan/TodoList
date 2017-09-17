package app.todo.ui.home.notification

import app.todo.core.presentation.BaseContract

interface NotificationContract {
    interface View : BaseContract.View

    interface Interaction : BaseContract.Interaction

    interface Presenter : BaseContract.Presenter<View>

}