package app.todo.core.di.module

import app.todo.core.di.scope.ActivityScope
import app.todo.ui.home.HomeActivity
import app.todo.ui.home.HomeModule
import app.todo.ui.home.list.ListNameActivity
import app.todo.ui.home.list.ListNameModule
import app.todo.ui.home.notification.NotificationActivity
import app.todo.ui.home.notification.NotificationModule
import app.todo.ui.settings.SettingActivity
import app.todo.ui.settings.SettingModule
import app.todo.ui.task.TaskActivity
import app.todo.ui.task.TaskModule
import app.todo.ui.task.edit.EditTaskActivity
import app.todo.ui.task.edit.EditTaskModule
import app.todo.ui.task.edit.note.NoteActivity
import app.todo.ui.task.edit.note.NoteModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = arrayOf(AndroidSupportInjectionModule::class))
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
    abstract fun homeActivityInjector(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(ListNameModule::class))
    abstract fun listNameActivityInjector(): ListNameActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(NotificationModule::class))
    abstract fun notificationActivityInjector(): NotificationActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(SettingModule::class))
    abstract fun SettingActivityInjector(): SettingActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(NoteModule::class))
    abstract fun noteActivityInjector(): NoteActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(EditTaskModule::class))
    abstract fun editTaskActivityInjector(): EditTaskActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(TaskModule::class))
    abstract fun taskActivityInjector(): TaskActivity

}