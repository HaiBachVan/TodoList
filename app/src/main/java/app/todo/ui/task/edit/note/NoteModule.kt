package app.todo.ui.task.edit.note

import app.todo.core.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class NoteModule {

    @Binds
    @ActivityScope
    abstract fun providesNotePresenter(presenter: NotePresenter): NoteContract.Presenter

    @Binds
    @ActivityScope
    abstract fun providesNoteInteraction(interaction: NoteInteraction): NoteContract.Interaction

}