package app.todo.core.presentation

import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V : BaseContract.View> : BaseContract.Presenter<V> {
    protected var mView: V? = null
    val compositeDisposable = CompositeDisposable()

    override fun attachView(view: V) {
        mView = view
    }

    override fun detachView() {
        mView = null
        compositeDisposable.dispose()
    }

}