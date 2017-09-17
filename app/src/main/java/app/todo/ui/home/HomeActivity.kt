package app.todo.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.CalendarView
import android.widget.LinearLayout
import app.todo.R
import app.todo.core.data.db.entities.ListName
import app.todo.core.presentation.BaseActivity
import app.todo.core.view.CustomFontEditText
import app.todo.core.view.CustomFontTextView
import app.todo.util.DateFormatUtils
import app.todo.util.Utils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : BaseActivity<HomePresenter, HomeContract.View>(), HomeContract.View, View.OnClickListener {
    private val TAG = "HomeActivity"

    private var mAdapter: HomeAdapter? = null
    private var mLists = ArrayList<ListName>()
    private lateinit var mListLiveData: LiveData<List<ListName>>

    // TODO
    private lateinit var textRemindMe: CustomFontTextView
    private lateinit var iconRemindMe: CustomFontTextView
    private lateinit var iconRemindMeCancel: CustomFontTextView

    private lateinit var iconRepeat: CustomFontTextView
    private lateinit var textRepeat: CustomFontTextView
    private lateinit var iconRepeatCancel: CustomFontTextView

    private var mDialogLayoutReminderMe: LinearLayout? = null
    private var mDialogLayoutRepeat: LinearLayout? = null

    private var mDate: Long = 0
    private var mTime: Int = 0
    private var mHourValue: Int = 0
    private var mMinValue: Int = 0

    private lateinit var mCalendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initInjection() {
        AndroidInjection.inject(this)
    }

    override val layout: Int
        get() = R.layout.activity_home

    private fun initView() {
        mPresenter.onViewInitialized()

        picture.setImageResource(R.drawable.background)
        Utils.applyBlur(picture!!)

        val linearLayoutManager = LinearLayoutManager(this)
        list_item_name.layoutManager = linearLayoutManager

        mListLiveData = mPresenter.getAllListNameWithLiveData()
        onObserveDatabase()

        mAdapter = HomeAdapter(this@HomeActivity, mLists)
        mAdapter!!.setListNames(mLists)
        list_item_name.adapter = mAdapter

        addTodo.setOnClickListener(this)
        avatar.setOnClickListener(this)
        notification.setOnClickListener(this)
        search.setOnClickListener(this)
    }

    private fun onObserveDatabase() {
        mListLiveData.observe(this, Observer<List<ListName>> { list ->
            run {
                Log.d(TAG, "onObserveDatabase(): table ListName change data")
                mLists = list as ArrayList<ListName>
                mAdapter!!.setListNames(mLists)
            }
        })
    }

    override fun onClick(view: View?) {
        val id = view?.id

        when (id) {
            R.id.addTodo -> onClickAddTask()
            R.id.avatar -> mPresenter.onClickAvatar()
            R.id.notification -> mPresenter.onClickOpenNotification()
            R.id.search -> mPresenter.onClickSearch()
        }
    }

    private fun onClickAddTask() {
        val mDialog = Dialog(this, R.style.DialogFullScreenStyle)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val view = layoutInflater.inflate(R.layout.dialog_add_task, null)
        mDialog.setContentView(view)
        mDialog.setCanceledOnTouchOutside(true)

        val mCancelButton: CustomFontTextView = view.findViewById(R.id.buttonCancel)
        val mAddButton: CustomFontTextView = view.findViewById(R.id.buttonAdd)

        val mSetDate: CustomFontTextView = view.findViewById(R.id.icon_set_date_reminder)
        val mSelectIcon: CustomFontTextView = view.findViewById(R.id.icon_star)

        val mTaskEditText: CustomFontEditText = view.findViewById(R.id.edTask)

        mTaskEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                mAddButton.isEnabled = editable.toString() != ""
            }

            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })

        mAddButton.setOnClickListener {
            // TODO
            mDialog.dismiss()
        }

        mCancelButton.setOnClickListener {
            mDialog.dismiss()
        }

        mSelectIcon.setOnClickListener {
            mSelectIcon.setTextColor(getColor(R.color.colorBlue))
        }

        mSetDate.setOnClickListener {
            onClickSetDateReminder()
        }

        mDialog.show()
    }

    private fun onClickSetDateReminder() {
        val builder = AlertDialog.Builder(this)

        val mDialog = builder.create()
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCanceledOnTouchOutside(false)

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_set_date_reminder, null)

        mDialog.setView(dialogView)
        val buttonRemove = dialogView.findViewById<CustomFontTextView>(R.id.buttonRemove)
        val buttonSave = dialogView.findViewById<CustomFontTextView>(R.id.buttonSave)
        val title = dialogView.findViewById<CustomFontTextView>(R.id.dialog_title)
        title.text = getString(R.string.due_today)

        iconRemindMe = dialogView.findViewById(R.id.dialog_icon_remind_me)
        textRemindMe = dialogView.findViewById(R.id.dialog_remind_me)
        iconRemindMeCancel = dialogView.findViewById(R.id.dialog_remind_me_cancel)
        iconRepeat = dialogView.findViewById(R.id.dialog_icon_repeat)
        textRepeat = dialogView.findViewById(R.id.dialog_repeat)
        iconRepeatCancel = dialogView.findViewById(R.id.dialog_repeat_cancel)

        mDialogLayoutReminderMe = dialogView.findViewById(R.id.dialog_layout_reminder_me)
        mDialogLayoutRepeat = dialogView.findViewById(R.id.dialog_layout_repeat)

        mDate = System.currentTimeMillis()

        // init value time default: time default - current time = 1 hour
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)

        mHourValue = if (hour == 23) {
            0
        } else {
            hour + 1
        }

        mMinValue = cal.get(Calendar.MINUTE)
        mTime = mHourValue * 100 + mMinValue

        mCalendarView = dialogView.findViewById(R.id.calendarView)
        mCalendarView.minDate = System.currentTimeMillis()
        mCalendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            mDate = calendar.timeInMillis
            val date = DateFormatUtils.getFormatDateTime(this@HomeActivity, mDate, true, false)

            mTime = 900
            val time = Utils.convertToFormatTime(this@HomeActivity, mTime)

            val text = getString(R.string.reminder_me_format, date, time)
            textRemindMe.text = text
            iconRemindMeCancel.visibility = View.VISIBLE

            title.text = getString(R.string.due_date, date)
        }

        val text = getString(R.string.reminder_me_format, "Today", Utils.convertToFormatTime(this, mTime))
        textRemindMe.text = text
        iconRemindMeCancel.visibility = View.VISIBLE

        iconRemindMeCancel.setOnClickListener {}

        iconRepeatCancel.setOnClickListener {

        }

        buttonRemove.setOnClickListener {
            // TODO
            mDialog.dismiss()
        }

        buttonSave.setOnClickListener {
            val isVisibleCancelRemindMe = iconRemindMeCancel.visibility == View.VISIBLE
            val isVisibleCancelRepeat = iconRepeatCancel.visibility == View.VISIBLE

//            val date = DateFormatUtils.getFormatDateTime(this@HomeActivity, mDate, true, false)
//            val time = Utils.convertToFormatTime(this@HomeActivity, mTime)


            if (isVisibleCancelRemindMe && isVisibleCancelRepeat) {
                // TODO
            } else if (isVisibleCancelRemindMe) {
                // TODO
            } else if (isVisibleCancelRepeat) {
                // TODO
            } else {
                // TODO
            }

            mDialog.dismiss()
        }

        mDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }
    }

    override fun showTodayLayout(numOfTaskToday: Int) {
        if (numOfTaskToday > 0) {
            item_layout_today.visibility = View.VISIBLE
            count.text = numOfTaskToday.toString()

            val cal = Calendar.getInstance()
            val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
            textDate.text = dayOfMonth.toString()
        } else {
            item_layout_today.visibility = View.GONE
        }
    }

}
