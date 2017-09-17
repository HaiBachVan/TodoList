package app.todo.core.di.module

import android.content.Context
import app.todo.core.app.TodoListApplication
import app.todo.core.di.qualifier.ApplicationContext
import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(ActivityBindingModule::class, DatabaseModule::class))
class AppModule {

    @Provides
    @ApplicationContext
    fun bindContext(application: TodoListApplication): Context = application

}