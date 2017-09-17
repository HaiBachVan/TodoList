package app.todo.ui.task.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import app.todo.R
import app.todo.core.data.db.entities.SubTask
import app.todo.core.presentation.BaseActivity
import app.todo.core.presentation.BasePopupAdapter
import app.todo.core.view.CustomFontEditText
import app.todo.core.view.CustomFontTextView
import app.todo.item.ItemPopup
import app.todo.ui.task.edit.note.NoteActivity
import app.todo.util.AppConstants
import app.todo.util.DateFormatUtils
import app.todo.util.Utils
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_edit_task.*
import java.util.*

class EditTaskActivity : BaseActivity<EditTaskPresenter, EditTaskContract.View>(), EditTaskContract.View, View.OnClickListener {
    private var mTextContentNote = ""

    internal var mColorBlue: Int = 0
    private var mColorTextDefault: Int = 0
    internal var mColorGray: Int = 0
    private var mColorOrange: Int = 0

    private val mListSubTask = ArrayList<SubTask>()
    private var mAdapter: SubTaskAdapter? = null

    //+ START: Dialog set date and reminder
    private var mDialogLayoutReminderMe: LinearLayout? = null
    private var mPopupMenuReminderMe: ListPopupWindow? = null
    private var mPopupReminderMeAdapter: BasePopupAdapter? = null
    private var mArrayItemPopupReminderMe = ArrayList<ItemPopup>()
    private lateinit var textRemindMe: CustomFontTextView
    private lateinit var iconRemindMe: CustomFontTextView
    private lateinit var iconRemindMeCancel: CustomFontTextView

    private var mDialogLayoutRepeat: LinearLayout? = null
    private var mPopupMenuRepeat: ListPopupWindow? = null
    private var mPopupRepeatAdapter: BasePopupAdapter? = null
    private var mArrayItemPopupRepeat = ArrayList<ItemPopup>()
    private lateinit var iconRepeat: CustomFontTextView
    private lateinit var textRepeat: CustomFontTextView
    private lateinit var iconRepeatCancel: CustomFontTextView

    private lateinit var mCalendarView: CalendarView
    //- END: Dialog set date and reminder

    private var mDate: Long = 0
    private var mTime: Int = 0
    private var mHourValue: Int = 0
    private var mMinValue: Int = 0

    private var mTaskName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }

    override fun initInjection() {
        AndroidInjection.inject(this)
    }

    private fun initView() {
        mColorBlue = getColor(R.color.colorBlue)
        mColorTextDefault = getColor(R.color.text_icon_image_default)
        mColorGray = getColor(R.color.colorGray)
        mColorOrange = getColor(R.color.colorPrimaryDark)

        //        mListSubTask = new ArrayList<>();
        val intent = intent ?: return

        mTaskName = intent.getStringExtra(AppConstants.ARG_NAME_TASK)
        title_edit_activity?.text = mTaskName

        val isTaskComplete = intent.getBooleanExtra(AppConstants.ARG_STATE_TASK, false)
        if (isTaskComplete) {
            title_edit_activity!!.paintFlags = title_edit_activity!!.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            title_edit_activity!!.paintFlags = title_edit_activity!!.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        mAdapter = SubTaskAdapter()

        val linearLayoutManager = LinearLayoutManager(this)
        list_sub_task?.layoutManager = linearLayoutManager

        list_sub_task?.visibility = View.VISIBLE
        list_sub_task?.adapter = mAdapter

        mTextContentNote = ""

        layout_add_note.setOnClickListener(this)
        btnBackLayout.setOnClickListener(this)
        layout_set_date_reminder.setOnClickListener(this)
        layout_add_sub_task.setOnClickListener(this)
        icon_cancel_set_date.setOnClickListener(this)
        icon_cancel_remind_me.setOnClickListener(this)
        icon_cancel_repeat.setOnClickListener(this)
    }

    override val layout: Int
        get() = R.layout.activity_edit_task

    private fun onClickAddSubTask() {
        val builder = AlertDialog.Builder(this)

        val mDialog = builder.create()
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCanceledOnTouchOutside(false)

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_dialog_edit_text, null)

        mDialog.setView(dialogView)
        val mEdSubTask = dialogView.findViewById<CustomFontEditText>(R.id.ed_alert_dialog)

        val window = mDialog.window
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)


        val buttonCancel = dialogView.findViewById<CustomFontTextView>(R.id.buttonCancel)
        val buttonAccept = dialogView.findViewById<CustomFontTextView>(R.id.buttonAccept)

        buttonCancel.text = resources.getString(R.string.button_cancel)
        buttonAccept.text = resources.getString(R.string.button_accept)

        buttonCancel.setOnClickListener { mDialog.dismiss() }

        buttonAccept.setOnClickListener {
            saveSubTask(mEdSubTask.text.toString())
            mDialog.dismiss()
        }

        mEdSubTask.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().trim { it <= ' ' } == "") {
                    buttonAccept.isEnabled = false
                    buttonAccept.setTextColor(mColorGray)
                } else {
                    buttonAccept.isEnabled = true
                    buttonAccept.setTextColor(mColorBlue)
                }
            }
        })

        mEdSubTask.setOnEditorActionListener { _, actionId, _ ->
            var needHiddenKeyboard = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (mEdSubTask.text.toString() == "") {
                    needHiddenKeyboard = true
                } else {
                    saveSubTask(mEdSubTask.text.toString())
                    mDialog.dismiss()
                }
            }

            needHiddenKeyboard
        }

        mDialog.show()
    }

    private fun saveSubTask(nameSubTask: String) {
        val subTask = SubTask(nameSubTask, false)
        mListSubTask.add(subTask)
        mAdapter?.dataSource = mListSubTask
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
            val date = DateFormatUtils.getFormatDateTime(this@EditTaskActivity, mDate, true, false)

            mTime = 900
            val time = Utils.convertToFormatTime(this@EditTaskActivity, mTime)

            iconRemindMe.setTextColor(mColorBlue)
            textRemindMe.setTextColor(mColorBlue)

            val text = getString(R.string.reminder_me_format, date, time)
            textRemindMe.text = text
            iconRemindMeCancel.visibility = View.VISIBLE

            title.text = getString(R.string.due_date, date)
        }

        // init view
        iconRemindMe.setTextColor(mColorBlue)
        textRemindMe.setTextColor(mColorBlue)

        val text = getString(R.string.reminder_me_format, "Today", Utils.convertToFormatTime(this, mTime))
        textRemindMe.text = text
        iconRemindMeCancel.visibility = View.VISIBLE

        //+ START: Popup remind me
        mPopupMenuReminderMe = ListPopupWindow(this@EditTaskActivity, null, android.R.attr.actionDropDownStyle)
        mDialogLayoutReminderMe?.setOnClickListener {
            if (mPopupMenuReminderMe != null) {
                Utils.calcSpinnerPopupHeight(this@EditTaskActivity, mPopupMenuReminderMe!!)
                mPopupMenuReminderMe!!.show()
            }
        }

        mArrayItemPopupReminderMe = ArrayList()
        mArrayItemPopupReminderMe.add(ItemPopup(getString(R.string.text_morning, Utils.convertToFormatTime(this, 900)), R.drawable.wl_icon_reminder_morning))
        mArrayItemPopupReminderMe.add(ItemPopup(getString(R.string.text_noon, Utils.convertToFormatTime(this, 1200)), R.drawable.wl_icon_reminder_noon))
        mArrayItemPopupReminderMe.add(ItemPopup(getString(R.string.text_afternoon, Utils.convertToFormatTime(this, 1500)), R.drawable.wl_icon_reminder_afternoon))
        mArrayItemPopupReminderMe.add(ItemPopup(getString(R.string.text_evening, Utils.convertToFormatTime(this, 1800)), R.drawable.wl_icon_reminder_evening))
        mArrayItemPopupReminderMe.add(ItemPopup(getString(R.string.text_night, Utils.convertToFormatTime(this, 2100)), R.drawable.wl_icon_reminder_night))
        mArrayItemPopupReminderMe.add(ItemPopup(getString(R.string.text_custom), -1000))

        mPopupReminderMeAdapter = BasePopupAdapter(this@EditTaskActivity, R.layout.item_popup_menu_type_icon, mArrayItemPopupReminderMe)
        showRemindMePopup(View.VISIBLE)

        mPopupMenuReminderMe!!.setContentWidth(650)
        setRemindMePopupMenuListener(mPopupMenuReminderMeListener)
        setRemindMePopupMenuAdapter(mPopupReminderMeAdapter!!, textRemindMe)
        //- END: Popup remind me

        //+ START: Popup repeat
        mPopupMenuRepeat = ListPopupWindow(this@EditTaskActivity, null, android.R.attr.actionDropDownStyle)
        mDialogLayoutRepeat!!.setOnClickListener {
            if (mPopupMenuRepeat != null) {
                Utils.calcSpinnerPopupHeight(this@EditTaskActivity, mPopupMenuRepeat!!)
                mPopupMenuRepeat!!.show()
            }
        }

        mArrayItemPopupRepeat = ArrayList()
        mArrayItemPopupRepeat.add(ItemPopup(getString(R.string.never_repeat)))
        mArrayItemPopupRepeat.add(ItemPopup(getString(R.string.every_day)))
        mArrayItemPopupRepeat.add(ItemPopup(getString(R.string.every_week)))
        mArrayItemPopupRepeat.add(ItemPopup(getString(R.string.every_month)))
        mArrayItemPopupRepeat.add(ItemPopup(getString(R.string.every_year)))
        mArrayItemPopupRepeat.add(ItemPopup(getString(R.string.text_custom)))

        mPopupRepeatAdapter = BasePopupAdapter(this@EditTaskActivity, R.layout.item_popup_menu_type_text, mArrayItemPopupRepeat)
        showRepeatPopup(View.VISIBLE)

        mPopupMenuRepeat!!.setContentWidth(650)
        setRepeatPopupMenuListener(mPopupMenuRepeatListener)
        setRepeatPopupMenuAdapter(mPopupRepeatAdapter!!, textRepeat)
        //- END: Popup repeat

        iconRemindMeCancel.setOnClickListener { onClickCancelReminder() }

        iconRepeatCancel.setOnClickListener {
            iconRepeat.setTextColor(mColorTextDefault)
            textRepeat.setTextColor(mColorTextDefault)
            textRepeat.text = getString(R.string.repeat_every)
            iconRepeatCancel.visibility = View.GONE
        }

        buttonRemove.setOnClickListener {
            layout_remind_me!!.visibility = View.GONE
            layout_repeat!!.visibility = View.GONE

            icon_set_date_reminder!!.setTextColor(mColorTextDefault)
            text_set_date_reminder!!.setTextColor(mColorTextDefault)
            text_set_date_reminder!!.text = getString(R.string.set_date_reminder)
            icon_cancel_set_date!!.visibility = View.GONE

            mDialog.dismiss()
        }

        buttonSave.setOnClickListener {
            layout_repeat!!.visibility = View.VISIBLE
            layout_remind_me!!.visibility = View.VISIBLE

            val isVisibleCancelRemindMe = iconRemindMeCancel.visibility == View.VISIBLE
            val isVisibleCancelRepeat = iconRepeatCancel.visibility == View.VISIBLE

            val date = DateFormatUtils.getFormatDateTime(this@EditTaskActivity, mDate, true, false)
            val time = Utils.convertToFormatTime(this@EditTaskActivity, mTime)

            val reminderMe = getString(R.string.reminder_me_format, date, time)

            if (isVisibleCancelRemindMe && isVisibleCancelRepeat) {
                text_set_date_reminder!!.text = getString(R.string.due_date, date)
                text_remind_me!!.text = reminderMe

                text_repeate!!.text = textRepeat.text

                setColorLayoutSetDateReminder(mColorBlue, mColorBlue, mColorBlue)
                setStateButtonCancel(View.VISIBLE, View.VISIBLE, View.VISIBLE)
            } else if (isVisibleCancelRemindMe) {
                text_set_date_reminder!!.text = getString(R.string.due_date, date)
                text_remind_me!!.text = reminderMe

                setColorLayoutSetDateReminder(mColorBlue, mColorBlue, mColorTextDefault)
                setStateButtonCancel(View.VISIBLE, View.VISIBLE, View.GONE)
            } else if (isVisibleCancelRepeat) {
                text_set_date_reminder!!.text = getString(R.string.due_date, date)
                text_repeate!!.text = textRepeat.text

                setColorLayoutSetDateReminder(mColorBlue, mColorTextDefault, mColorBlue)
                setStateButtonCancel(View.VISIBLE, View.GONE, View.VISIBLE)
            } else {
                text_set_date_reminder!!.text = getString(R.string.due_date, date)
                setColorLayoutSetDateReminder(mColorBlue, mColorTextDefault, mColorTextDefault)
                setStateButtonCancel(View.VISIBLE, View.GONE, View.GONE)
            }

            mDialog.dismiss()
        }

        mDialog.show()
    }

    private fun setColorLayoutSetDateReminder(colorSetDate: Int, colorRemindMe: Int, colorRepeat: Int) {
        icon_set_date_reminder!!.setTextColor(colorSetDate)
        text_set_date_reminder!!.setTextColor(colorSetDate)

        icon_remind_me!!.setTextColor(colorRemindMe)
        text_remind_me!!.setTextColor(colorRemindMe)

        icon_repeat!!.setTextColor(colorRepeat)
        text_repeate!!.setTextColor(colorRepeat)
    }

    private fun setStateButtonCancel(visibleSetDate: Int, visibleRemindMe: Int, visibleRepeat: Int) {
        icon_cancel_set_date!!.visibility = visibleSetDate
        icon_cancel_remind_me!!.visibility = visibleRemindMe
        icon_cancel_repeat!!.visibility = visibleRepeat
    }

    //+ START: Method popup remind me
    private var mPopupMenuReminderMeListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        //String[] arr = {"9:00", "12:00", "15:00", "18:00", "21:00"};
        val arrTime = intArrayOf(900, 1200, 1500, 1800, 2100)

        if (position < arrTime.size) {
            iconRemindMe.setTextColor(mColorBlue)
            textRemindMe.setTextColor(mColorBlue)

            val date = DateFormatUtils.getFormatDateTime(this@EditTaskActivity, mDate, true, false)

            mTime = arrTime[position]
            val time = Utils.convertToFormatTime(this@EditTaskActivity, mTime)

            val text = getString(R.string.reminder_me_format, date, time)
            textRemindMe.text = text
            iconRemindMeCancel.visibility = View.VISIBLE
        } else {
            showTimeDialog()
        }

        setDismissRemindMePopup()
    }

    private fun setDismissRemindMePopup() {
        if (mPopupMenuReminderMe != null) {
            mPopupMenuReminderMe!!.dismiss()
        }
    }

    private fun showRemindMePopup(visible: Int) {
        mDialogLayoutReminderMe!!.visibility = visible
    }

    private fun setRemindMePopupMenuAdapter(adapter: BasePopupAdapter, anchorView: View) {
        mPopupReminderMeAdapter = adapter
        mPopupMenuReminderMe!!.anchorView = anchorView
        mPopupMenuReminderMe!!.setAdapter(mPopupReminderMeAdapter)
        mPopupMenuReminderMe!!.isModal = true
        mPopupMenuReminderMe!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@EditTaskActivity, R.color.white_milk)))
        Utils.adjustDropDownPopupOffset(this@EditTaskActivity, mPopupMenuReminderMe)
    }

    private fun setRemindMePopupMenuListener(listener: AdapterView.OnItemClickListener) {
        mPopupMenuReminderMeListener = listener
        mPopupMenuReminderMe!!.setOnItemClickListener(mPopupMenuReminderMeListener)
    }
    //- END: Method popup remind me

    //+ START: Method popup repeat
    private var mPopupMenuRepeatListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        val arr = arrayOf("Never repeat", "Every day", "Every week", "Every month", "Every year")

        if (position < arr.size) {
            if (arr[position] != "Never repeat") {
                setStateRepeat(true)
                textRepeat.text = arr[position]
            } else {
                setStateRepeat(false)
                textRepeat.text = arr[position]
            }
        } else {
            showRepeatDialog()
        }

        setDismissRepeatPopup()
    }

    private fun setDismissRepeatPopup() {
        if (mPopupMenuRepeat != null) {
            mPopupMenuRepeat!!.dismiss()
        }
    }

    private fun setStateRepeat(isRepeat: Boolean) {
        if (isRepeat) {
            iconRepeat.setTextColor(mColorBlue)
            textRepeat.setTextColor(mColorBlue)

            iconRepeatCancel.visibility = View.VISIBLE
        } else {
            iconRepeat.setTextColor(mColorTextDefault)
            textRepeat.setTextColor(mColorTextDefault)

            iconRepeatCancel.visibility = View.GONE
        }
    }

    private fun showRepeatPopup(visible: Int) {
        mDialogLayoutRepeat!!.visibility = visible
    }

    private fun setRepeatPopupMenuAdapter(adapter: BasePopupAdapter, anchorView: View) {
        mPopupRepeatAdapter = adapter
        mPopupMenuRepeat!!.anchorView = anchorView
        mPopupMenuRepeat!!.setAdapter(mPopupRepeatAdapter)
        mPopupMenuRepeat!!.isModal = true
        mPopupMenuRepeat!!.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@EditTaskActivity, R.color.white_milk)))
        Utils.adjustDropDownPopupOffset(this@EditTaskActivity, mPopupMenuRepeat)
    }

    private fun setRepeatPopupMenuListener(listener: AdapterView.OnItemClickListener) {
        mPopupMenuRepeatListener = listener
        mPopupMenuRepeat?.setOnItemClickListener(mPopupMenuRepeatListener)
    }

    private fun showRepeatDialog() {
        val builder = AlertDialog.Builder(this)

        val mDialog = builder.create()
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCanceledOnTouchOutside(false)

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_set_repeat_reminder, null)

        mDialog.setView(dialogView)
        val buttonCancel = dialogView.findViewById<CustomFontTextView>(R.id.buttonCancel)
        val buttonOK = dialogView.findViewById<CustomFontTextView>(R.id.buttonOK)

        val mNumOfRepeat = dialogView.findViewById<Spinner>(R.id.numOfRepeat)
        val mTypeOfRepeat = dialogView.findViewById<Spinner>(R.id.typeOfRepeat)
        val contentDialog = dialogView.findViewById<CustomFontTextView>(R.id.dialog_text_content)

        val spinnerArray = (1..30).mapTo(ArrayList()) { it.toString() }

        val spinnerArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mNumOfRepeat.adapter = spinnerArrayAdapter

        val typeOfRepeatArray = ArrayList<String>()
        typeOfRepeatArray.add(getString(R.string.text_days))
        typeOfRepeatArray.add(getString(R.string.text_weeks))
        typeOfRepeatArray.add(getString(R.string.text_months))
        typeOfRepeatArray.add(getString(R.string.text_years))

        val typeOfRepeatAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typeOfRepeatArray)
        typeOfRepeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mTypeOfRepeat.adapter = typeOfRepeatAdapter

        val text = mNumOfRepeat.selectedItem.toString() + " " + mTypeOfRepeat.selectedItem.toString()
        contentDialog.text = getString(R.string.dialog_repeat_every, text)

        mNumOfRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                val text1 = mNumOfRepeat.selectedItem.toString() + " " + mTypeOfRepeat.selectedItem.toString()
                contentDialog.text = getString(R.string.dialog_repeat_every, text1)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        mTypeOfRepeat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                val text2 = mNumOfRepeat.selectedItem.toString() + " " + mTypeOfRepeat.selectedItem.toString()
                contentDialog.text = getString(R.string.dialog_repeat_every, text2)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        buttonCancel.setOnClickListener {
            setStateRepeat(false)
            textRepeat.text = getString(R.string.never_repeat)
            mDialog.dismiss()
        }

        buttonOK.setOnClickListener {
            setStateRepeat(true)
            textRepeat.text = contentDialog.text
            mDialog.dismiss()
        }

        mDialog.show()
    }
    //- END: Method popup repeat

    private fun showTimeDialog() {
        val builder = AlertDialog.Builder(this)

        val mDialog = builder.create()
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCanceledOnTouchOutside(false)

        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_set_time_reminder, null)

        mDialog.setView(dialogView)
        val buttonRemove = dialogView.findViewById<CustomFontTextView>(R.id.buttonRemove)
        val buttonSave = dialogView.findViewById<CustomFontTextView>(R.id.buttonSave)

        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePickerView)
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            mHourValue = hour
            mMinValue = minute
            mTime = hour * 100 + minute
        }

        buttonRemove.setOnClickListener {
            onClickCancelReminder()
            mDialog.dismiss()
        }

        buttonSave.setOnClickListener {
            val date = DateFormatUtils.getFormatDateTime(this@EditTaskActivity, mDate, true, false)
            val time = Utils.convertToFormatTime(this@EditTaskActivity, mTime)

            iconRemindMe.setTextColor(mColorBlue)
            textRemindMe.setTextColor(mColorBlue)

            val text = getString(R.string.reminder_me_format, date, time)
            textRemindMe.text = text
            iconRemindMeCancel.visibility = View.VISIBLE

            mDialog.dismiss()
        }

        mDialog.show()
    }

    private fun onClickCancelReminder() {
        iconRemindMe.setTextColor(mColorTextDefault)
        textRemindMe.setTextColor(mColorTextDefault)
        textRemindMe.text = getString(R.string.reminder_me)
        iconRemindMeCancel.visibility = View.GONE
    }

    override fun onClick(view: View?) {
        val id = view?.id

        when (id) {
            R.id.layout_add_note -> onClickAddNote()

            R.id.btnBackLayout -> {
                super.onBackPressed()
            }

            R.id.layout_set_date_reminder -> onClickSetDateReminder()

            R.id.layout_add_sub_task -> onClickAddSubTask()

            R.id.icon_cancel_set_date -> {
                setColorLayoutSetDateReminder(mColorTextDefault, mColorTextDefault, mColorTextDefault)
                icon_cancel_set_date!!.visibility = View.GONE
                layout_remind_me!!.visibility = View.GONE
                layout_repeat!!.visibility = View.GONE
                text_set_date_reminder!!.text = getString(R.string.set_date_reminder)
            }

            R.id.icon_cancel_remind_me -> {
                icon_cancel_remind_me!!.visibility = View.GONE
                icon_remind_me!!.setTextColor(mColorTextDefault)
                text_remind_me!!.text = getString(R.string.reminder_me)
                text_remind_me!!.setTextColor(mColorTextDefault)
            }

            R.id.icon_cancel_repeat -> {
                icon_cancel_repeat!!.visibility = View.GONE
                icon_repeat!!.setTextColor(mColorTextDefault)
                text_repeate!!.text = getString(R.string.repeat_every)
                text_repeate!!.setTextColor(mColorTextDefault)
            }
        }
    }

    private fun onClickAddNote() {
        val intent = Intent(this@EditTaskActivity, NoteActivity::class.java)
        intent.putExtra(AppConstants.ARG_NOTE, mTextContentNote)
        intent.putExtra(AppConstants.ARG_NAME_TASK, mTaskName)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) {
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            val contentNote = data.getStringExtra(AppConstants.ARG_NOTE)
            if (contentNote == null) {
                mTextContentNote = ""
                content_note!!.text = getString(R.string.add_a_note)
                content_note!!.setTextColor(mColorTextDefault)
                icon_add_note!!.setTextColor(mColorTextDefault)
            } else {
                mTextContentNote = contentNote
                content_note!!.text = contentNote
                content_note!!.setTextColor(ContextCompat.getColor(this@EditTaskActivity, R.color.text_color_common))
                icon_add_note!!.setTextColor(mColorOrange)
            }
        }
    }
}
