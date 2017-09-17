package app.todo.ui.settings

import app.todo.core.presentation.BaseContract

interface SettingContract {
    interface View : BaseContract.View

    interface Interaction : BaseContract.Interaction

    interface Presenter : BaseContract.Presenter<View>

}