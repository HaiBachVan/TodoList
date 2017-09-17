package app.todo.core.data.db.repository

import android.arch.lifecycle.LiveData
import app.todo.core.data.db.dao.ListNameDao
import app.todo.core.data.db.entities.ListName
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListNameRepository @Inject constructor(val listNameDao: ListNameDao) {

    fun getNumOfListName(): Int = listNameDao.getNumOfListName()

    fun getAllListNameWithRx(): Single<List<ListName>>
            = listNameDao.loadAllListWithRx()
            .firstOrError()
            .doOnSuccess { if (it.isEmpty()) throw Exception() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getAllListNameWithLiveData(): LiveData<List<ListName>>
            = listNameDao.loadAllListWithLiveData()

    fun insertAllListName(list: List<ListName>) = listNameDao.insertAll(list.toMutableList())

    fun insertListName(listName: ListName) = listNameDao.insertListName(listName)
}