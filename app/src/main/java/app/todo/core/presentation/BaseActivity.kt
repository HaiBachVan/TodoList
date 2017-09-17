package app.todo.core.presentation

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


@SuppressLint("Registered")
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<P : BaseContract.Presenter<V>, in V : BaseContract.View>
    : LifecycleActivity(), HasSupportFragmentInjector, BaseContract.View {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjection()
        super.onCreate(savedInstanceState)
        setContentView(layout)
        mPresenter.attachView(this as V)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    abstract val layout: Int
    abstract fun initInjection()

}
