package app.todo.ui.home.list

import app.todo.core.data.db.entities.ListName
import app.todo.core.presentation.BasePresenter
import javax.inject.Inject

class ListNamePresenter @Inject constructor(val activity: ListNameActivity, val interaction: ListNameContract.Interaction)
    : BasePresenter<ListNameContract.View>(), ListNameContract.Presenter {

    override fun onClickSaveNameGroup(nameGroup: String?) {
        // save to database
        val listName = ListName(nameGroup)
        interaction.insertListName(listName)

        activity.finish()
    }

}