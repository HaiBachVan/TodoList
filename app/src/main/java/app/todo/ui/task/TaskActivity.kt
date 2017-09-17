package app.todo.ui.task

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import app.todo.R
import app.todo.core.data.db.entities.Task
import app.todo.core.presentation.BaseActivity
import app.todo.util.AppConstants
import app.todo.util.Utils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_task.*
import javax.inject.Inject

class TaskActivity : BaseActivity<TaskPresenter, TaskContract.View>(), TaskContract.View, View.OnClickListener {

    @Inject
    lateinit var mTaskAdapter: TaskAdapter

    private var mListAllReminder = ArrayList<Task>()
    private lateinit var mListAllWithLiveData: LiveData<List<Task>>

    private var mId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initInjection() {
        AndroidInjection.inject(this)
    }

    private fun initView() {
        val intent = intent ?: return

        title_activity?.text = intent.getStringExtra(AppConstants.ARG_NAME_LIST)
        mId = intent.getIntExtra(AppConstants.ARG_NAME_LIST_ID, -1)

        picture?.setImageResource(R.drawable.background)
        Utils.applyBlur(picture)

        edToDo?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString() == "") {
                    menu_add?.visibility = View.GONE
                    menu_sort?.visibility = View.VISIBLE
                } else {
                    menu_add?.visibility = View.VISIBLE
                    menu_sort?.visibility = View.GONE
                }
            }
        })

        edToDo?.setOnEditorActionListener { _, actionId, _ ->
            var needHiddenKeyboard = false

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                needHiddenKeyboard = if (edToDo?.text.toString() == "") {
                    false
                } else {
                    onClickAddToDo()
                    true
                }
            }

            needHiddenKeyboard
        }

        mListAllWithLiveData = mPresenter.onViewInitialized(mId)
        updateListView()
        onObserveDatabase()

        listReminder?.setGroupIndicator(null)
        listReminder?.itemsCanFocus = true

        if (listReminder.adapter == null) {
            mTaskAdapter.setListView(listReminder!!)
            listReminder?.setAdapter(mTaskAdapter)
        }

        btnBack.setOnClickListener(this)
        menu_add.setOnClickListener(this)
    }

    private fun updateListView() {
        mTaskAdapter.setListAllReminder(mListAllReminder)
        mTaskAdapter.buildAllReminder()
        mTaskAdapter.notifyDataSetChanged()
    }

    private fun onObserveDatabase() {
        mListAllWithLiveData.observe(this, Observer<List<Task>> { list ->
            run {
                mListAllReminder = list as ArrayList<Task>
                updateListView()
            }
        })
    }

    override val layout: Int
        get() = R.layout.activity_task

    private fun onClickAddToDo() {
        val task = Task(mId, edToDo?.text.toString(), false)
        mListAllReminder.add(task)

        val isExpanded = mTaskAdapter.isExpanded

        mTaskAdapter.setListAllReminder(mListAllReminder)
        mTaskAdapter.buildAllReminder()

        val groupDonePos = mTaskAdapter.groupDonePos
        mTaskAdapter.notifyDataSetChanged()
        mTaskAdapter.setExpanded(isExpanded, groupDonePos)

        edToDo?.setText("")
        mPresenter.onClickAddToDo(task)
    }

    override fun onClick(view: View?) {
        val id = view?.id

        if (id == R.id.menu_add) {
            onClickAddToDo()
        } else if (id == R.id.btnBack) {
            super.onBackPressed()
        }
    }

}
