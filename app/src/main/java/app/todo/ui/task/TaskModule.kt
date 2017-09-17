package app.todo.ui.task

import app.todo.core.data.db.repository.TaskRepository
import app.todo.core.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module
class TaskModule {

    @Provides
    @ActivityScope
    fun providesTaskPresenter(presenter: TaskPresenter): TaskContract.Presenter = presenter

    @Provides
    @ActivityScope
    fun providesTaskInteraction(interaction: TaskInteraction): TaskContract.Interaction = interaction

    @Provides
    @ActivityScope
    fun providesTaskAdapter(context: TaskActivity, repository: TaskRepository): TaskAdapter = TaskAdapter(context, repository)

}