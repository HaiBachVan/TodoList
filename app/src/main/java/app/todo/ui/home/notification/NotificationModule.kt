package app.todo.ui.home.notification

import app.todo.core.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class NotificationModule {

    @Binds
    @ActivityScope
    abstract fun providesNotificationPresenter(presenter: NotificationPresenter): NotificationContract.Presenter

    @Binds
    @ActivityScope
    abstract fun providesNotificationInteraction(interaction: NotificationInteraction): NotificationContract.Interaction

}