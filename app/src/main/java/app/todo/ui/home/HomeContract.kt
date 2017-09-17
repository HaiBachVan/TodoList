package app.todo.ui.home

import android.arch.lifecycle.LiveData
import app.todo.core.data.db.entities.ListName
import app.todo.core.presentation.BaseContract

interface HomeContract {
    interface View : BaseContract.View {
        fun showTodayLayout(numOfTaskToday: Int)
    }

    interface Interaction : BaseContract.Interaction {
        fun initDb()
        fun getAllListNameWithLiveData(): LiveData<List<ListName>>
        fun getNumOfListNameFromDb(): Int
        fun getNumOfTaskTodayFromDb(): Int
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun getAllListNameWithLiveData(): LiveData<List<ListName>>
        fun onViewInitialized()
        fun updateTodayLayout()

        fun onClickAddTask()
        fun onClickAvatar()
        fun onClickOpenNotification()
        fun onClickSearch()
    }

    interface Router : BaseContract.Router
}