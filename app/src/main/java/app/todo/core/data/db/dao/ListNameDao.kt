package app.todo.core.data.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import app.todo.core.data.db.entities.ListName
import io.reactivex.Flowable

@Dao
interface ListNameDao {
    @Query("SELECT COUNT(name) FROM list")
    fun getNumOfListName(): Int

    @Query("SELECT * FROM list")
    fun loadAllListWithRx(): Flowable<List<ListName>>

    @Query("SELECT * FROM list")
    fun loadAllListWithLiveData(): LiveData<List<ListName>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListName(listName: ListName)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listName: MutableList<ListName>)

    @Delete
    fun deleteListName(listName: ListName)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateListName(listName: ListName)
}