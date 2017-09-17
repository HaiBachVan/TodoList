package app.todo.ui.task

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.ExpandableListView
import android.widget.TextView
import app.todo.R
import app.todo.core.data.db.entities.Task
import app.todo.core.data.db.repository.TaskRepository
import app.todo.core.view.CustomFontTextView
import app.todo.ui.task.edit.EditTaskActivity
import app.todo.util.AppConstants
import java.util.*
import javax.inject.Inject

class TaskAdapter @Inject constructor(val mContext: Context, val repository: TaskRepository) : BaseExpandableListAdapter() {
    private val TAG = "TaskAdapter"

    private var mListAllReminder = ArrayList<Task>()
    private val mListDoneReminder = ArrayList<Task>()
    private val mListUnDoneReminder = ArrayList<Task>()
    private var mGroupDonePos: Int = 0

    private val mInflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mExpandableListView: ExpandableListView? = null

    init {
        mListAllReminder = ArrayList()
    }

    fun setListView(expandableListView: ExpandableListView) {
        mExpandableListView = expandableListView
        mExpandableListView!!.setOnGroupClickListener { _, _, _, _ ->
            val mIsExpanded = mExpandableListView!!.isGroupExpanded(groupDonePos)
            Log.d(TAG, "setListView: mIsExpanded = " + mIsExpanded)

            false
        }
    }

    fun setListAllReminder(listAllReminder: ArrayList<Task>) {
        mListAllReminder.clear()
        mListAllReminder.addAll(listAllReminder)
    }

    fun buildAllReminder() {
        mListDoneReminder.clear()
        mListUnDoneReminder.clear()

        if (mListAllReminder.size > 0) {
            for (task in mListAllReminder) {
                if (task.isComplete) {
                    mListDoneReminder.add(task)
                } else {
                    mListUnDoneReminder.add(task)
                }
            }

            Log.d(TAG, "Size done = " + mListDoneReminder.size + ", size undone = " + mListUnDoneReminder.size)
            mGroupDonePos = if (mListDoneReminder.size > 0) mListUnDoneReminder.size else -1
        }
    }

    override fun getGroupCount(): Int {
        return mListUnDoneReminder.size + if (mListDoneReminder.size > 0) 1 else 0
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return when {
            groupPosition < mListUnDoneReminder.size -> 0
            groupPosition == mGroupDonePos -> mListDoneReminder.size
            else -> -1
        }
    }

    override fun getGroup(groupPosition: Int): Any? {
        return when {
            groupPosition < mListUnDoneReminder.size -> mListUnDoneReminder[groupPosition]
            groupPosition == mGroupDonePos -> mGroupDonePos
            else -> null
        }
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return when {
            groupPosition < mListUnDoneReminder.size -> null
            groupPosition == mGroupDonePos -> mListDoneReminder[childPosition]
            else -> null
        }
    }

    override fun getGroupId(groupId: Int): Long {
        return groupId.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View? {
        var view: View? = null
        val viewHolder: ViewHolder

        if (groupPosition < mListUnDoneReminder.size) {
            if (convertView?.findViewById<View>(R.id.check_todo) != null) {
                view = convertView
            } else {
                view = mInflater.inflate(R.layout.item_task_list, parent, false)
                viewHolder = ViewHolder(view)
                view!!.tag = viewHolder
            }

            bindView(groupPosition, -1, view)
        } else if (groupPosition == mGroupDonePos) {
            view = if (convertView?.findViewById<View>(R.id.expand_button) != null) {
                convertView
            } else {
                mInflater.inflate(R.layout.item_header_task_list, parent, false)
            }

            val textState = view!!.findViewById<TextView>(R.id.expand_button)
            if (isExpanded) {
                textState.text = mContext.getString(R.string.completed_to_dos)
            } else {
                textState.text = mContext.getString(R.string.show_completed_to_dos)
            }
        } else {
            Log.d(TAG, "Wrong group position (getGroupView) : " + groupPosition)
        }

        return view
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView != null && convertView.tag != null) {
            view = convertView
        } else {
            view = mInflater.inflate(R.layout.item_task_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }

        bindView(groupPosition, childPosition, view)
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    private fun bindView(groupPosition: Int, childPosition: Int, view: View) {
        val mTask: Task = when {
            groupPosition < mListUnDoneReminder.size -> mListUnDoneReminder[groupPosition]
            groupPosition == mGroupDonePos -> mListDoneReminder[childPosition]
            else -> return
        }

        val isToDoComplete = mTask.isComplete
        val viewHolder = view.tag as ViewHolder
        viewHolder.mNameTodo.text = mTask.name
        viewHolder.mCheckTodo.isChecked = isToDoComplete

        if (isToDoComplete) {
            viewHolder.mNameTodo.paintFlags = viewHolder.mNameTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.mItemLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_background_reminder_done_blur))
        } else {
            viewHolder.mNameTodo.paintFlags = viewHolder.mNameTodo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            viewHolder.mItemLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite))
        }

        viewHolder.mCheckTodo.setOnClickListener {
            val isExpanded = isExpanded
            Log.d(TAG, "before notifyDataSetChanged: isExpanded = " + isExpanded)

            val isCheck = viewHolder.mCheckTodo.isChecked

            if (!isCheck) {
                mListDoneReminder.remove(mTask)

                mTask.isComplete = false
                mListUnDoneReminder.add(mTask)

                // update to database
                repository.updateTask(mTask)
            } else {
                mListUnDoneReminder.remove(mTask)

                mTask.isComplete = true
                mListDoneReminder.add(mTask)

                // update to database
                repository.updateTask(mTask)
            }

            mGroupDonePos = groupDonePos
            notifyDataSetChanged()

            setExpanded(isExpanded, mGroupDonePos)
        }

        view.setOnClickListener {
            val intent = Intent(mContext, EditTaskActivity::class.java)
            intent.putExtra(AppConstants.ARG_NAME_TASK, mTask.name)
            intent.putExtra(AppConstants.ARG_STATE_TASK, mTask.isComplete)
            mContext.startActivity(intent)
        }
    }

    val groupDonePos: Int
        get() = if (mListDoneReminder.size > 0) mListUnDoneReminder.size else -1

    val isExpanded: Boolean
        get() = mExpandableListView!!.isGroupExpanded(groupDonePos)

    fun setExpanded(isExpanded: Boolean, groupDonePos: Int) {
        if (mListDoneReminder.size > 0) {
            if (isExpanded) {
                mExpandableListView!!.expandGroup(groupDonePos)
            } else {
                mExpandableListView!!.collapseGroup(groupDonePos)
            }
        }
    }

    private class ViewHolder internal constructor(itemView: View) {
        internal var mItemLayout: View = itemView.findViewById(R.id.item_layout_name)
        internal var mCheckTodo: CheckBox = itemView.findViewById(R.id.check_todo)
        internal var mNameTodo: CustomFontTextView = itemView.findViewById(R.id.name_todo)
    }

}
