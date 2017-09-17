package app.todo.ui.settings

import app.todo.core.presentation.BasePresenter
import javax.inject.Inject

class SettingPresenter @Inject constructor(val mActivity: SettingActivity, val interactor: SettingContract.Interaction)
    : BasePresenter<SettingContract.View>(), SettingContract.Presenter {

}