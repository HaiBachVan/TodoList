package app.todo.ui.home.notification

import app.todo.core.presentation.BasePresenter
import javax.inject.Inject

class NotificationPresenter @Inject constructor(val mActivity: NotificationActivity, val interactor: NotificationContract.Interaction)
    : BasePresenter<NotificationContract.View>(), NotificationContract.Presenter {

}