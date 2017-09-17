package app.todo.ui.home.list

import app.todo.core.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class ListNameModule {

    @Binds
    @ActivityScope
    abstract fun providesListNamePresenter(presenter: ListNamePresenter): ListNameContract.Presenter

    @Binds
    @ActivityScope
    abstract fun providesListNameInteraction(interaction: ListNameInteraction): ListNameContract.Interaction

}