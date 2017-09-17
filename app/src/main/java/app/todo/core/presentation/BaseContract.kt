package app.todo.core.presentation


interface BaseContract {
    interface View

    interface Interaction

    interface Presenter<in V : View> {
        fun attachView(view: V)
        fun detachView()
    }

    interface Router
}