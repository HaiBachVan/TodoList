package app.todo.core.di.module

import android.arch.persistence.room.Room
import android.content.Context
import app.todo.core.data.db.DatabaseManager
import app.todo.core.data.db.dao.ListNameDao
import app.todo.core.data.db.dao.TaskDao
import app.todo.core.data.db.repository.TaskRepository
import app.todo.core.data.db.util.DatabaseMigration
import app.todo.core.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): DatabaseManager =
            Room.databaseBuilder(
                    context,
                    DatabaseManager::class.java,
                    DatabaseManager.DATABASE_NAME
            ).allowMainThreadQueries()
                    .addMigrations(DatabaseMigration.MIGRATION)
                    .build()

    @Provides
    fun providesListNameDao(manager: DatabaseManager): ListNameDao = manager.listNameDao()

    @Provides
    fun providesTaskDao(manager: DatabaseManager): TaskDao = manager.taskDao()

    @Provides
    fun providesTaskRepository(taskDao: TaskDao): TaskRepository = TaskRepository(taskDao)

}