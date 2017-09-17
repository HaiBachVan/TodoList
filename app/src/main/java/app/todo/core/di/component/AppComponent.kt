package app.todo.core.di.component

import app.todo.core.app.TodoListApplication
import app.todo.core.di.module.AppModule
import app.todo.core.di.module.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, DatabaseModule::class))
interface AppComponent: AndroidInjector<TodoListApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: TodoListApplication): Builder

        fun build(): AppComponent
    }

    override fun inject(app: TodoListApplication)

}