package app.todo.ui.settings.extras

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import app.todo.R
import app.todo.core.presentation.BaseFragment

class ExtrasFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initInject() {

    }

    override val layout: Int
        get() = R.layout.fragment_extras

    companion object {

        fun newInstance(): ExtrasFragment {
            return ExtrasFragment()
        }
    }

}
