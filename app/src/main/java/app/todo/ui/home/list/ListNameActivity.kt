package app.todo.ui.home.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import app.todo.R
import app.todo.core.presentation.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_create_list_name_task.*

class ListNameActivity : BaseActivity<ListNamePresenter, ListNameContract.View>(), ListNameContract.View, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initInjection() {
        AndroidInjection.inject(this)
    }

    private fun initView() {
        // show keyboard
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        name_group_reminder?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().trim { it <= ' ' } == "") {
                    buttonSave?.isEnabled = false
                    buttonSave?.alpha = 0.5f
                } else {
                    buttonSave?.isEnabled = true
                    buttonSave?.alpha = 1.0f
                }
            }
        })

        name_group_reminder?.setOnEditorActionListener { _, actionId, _ ->
            var needHiddenKeyboard = false

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (name_group_reminder!!.text.toString() == "") {
                    needHiddenKeyboard = true
                } else {
                    onClickSaveNameGroup()
                    name_group_reminder!!.setText("")
                }
            }

            needHiddenKeyboard
        }

        buttonSave.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    override val layout: Int
        get() = R.layout.activity_create_list_name_task

    private fun onClickSaveNameGroup() {
        val name = name_group_reminder?.text.toString()
        mPresenter.onClickSaveNameGroup(name)
    }

    override fun onClick(view: View?) {
        val id = view?.id

        if (id == R.id.buttonSave) {
            onClickSaveNameGroup()
        } else if (id == R.id.btnBack) {
            super.onBackPressed()
        }
    }

}
