package app.todo.ui.settings

import app.todo.core.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class SettingModule {

    @Binds
    @ActivityScope
    abstract fun providesSettingPresenter(presenter: SettingPresenter): SettingContract.Presenter

    @Binds
    @ActivityScope
    abstract fun providesSettingInteraction(interaction: SettingInteraction): SettingContract.Interaction

}