package app.todo.ui.home

import android.arch.lifecycle.LiveData
import android.content.Intent
import app.todo.core.data.db.entities.ListName
import app.todo.core.presentation.BasePresenter
import app.todo.ui.home.notification.NotificationActivity
import app.todo.ui.settings.SettingActivity
import javax.inject.Inject

class HomePresenter @Inject constructor(val activity: HomeActivity, val interaction: HomeContract.Interaction)
    : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun onClickAddTask() {
        // TODO
    }

    override fun onClickAvatar() {
        val intent = Intent(activity, SettingActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onClickOpenNotification() {
        val intent = Intent(activity, NotificationActivity::class.java)
        activity.startActivity(intent)
    }

    override fun onClickSearch() {

    }

    override fun getAllListNameWithLiveData(): LiveData<List<ListName>> = interaction.getAllListNameWithLiveData()

    override fun onViewInitialized() {
        if (interaction.getNumOfListNameFromDb() == 0) {
            interaction.initDb()
        }

        updateTodayLayout()
    }

    override fun updateTodayLayout() {
        val numOfTaskToday = interaction.getNumOfTaskTodayFromDb()
        (mView as HomeContract.View).showTodayLayout(numOfTaskToday)
    }

}