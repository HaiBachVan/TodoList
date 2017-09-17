package app.todo.ui.home

import app.todo.core.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class HomeModule {

    @Binds
    @ActivityScope
    abstract fun providesHomePresenter(presenter: HomePresenter): HomeContract.Presenter

    @Binds
    @ActivityScope
    abstract fun providesHomeInteraction(interaction: HomeInteraction): HomeContract.Interaction

}