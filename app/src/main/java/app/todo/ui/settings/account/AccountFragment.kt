package app.todo.ui.settings.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import app.todo.R
import app.todo.core.presentation.BaseFragment

class AccountFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initInject() {

    }

    override val layout: Int
        get() = R.layout.fragment_account

    companion object {

        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }

}
