package app.todo.ui.task.edit

import app.todo.core.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class EditTaskModule {

    @Binds
    @ActivityScope
    abstract fun providesEditTaskPresenter(presenter: EditTaskPresenter): EditTaskContract.Presenter

    @Binds
    @ActivityScope
    abstract fun providesEditTaskInteraction(interaction: EditTaskInteraction): EditTaskContract.Interaction

}