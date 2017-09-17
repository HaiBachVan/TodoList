package app.todo.ui.task.edit

import android.view.View
import android.widget.CheckBox
import app.todo.R
import app.todo.core.data.db.entities.SubTask
import app.todo.core.presentation.BaseAdapter
import app.todo.core.presentation.BaseViewHolder
import app.todo.core.view.CustomFontTextView

class SubTaskAdapter : BaseAdapter<SubTask, SubTaskAdapter.SubTaskViewHolder>() {

    override fun instantiateViewHolder(view: View?): SubTaskViewHolder = SubTaskViewHolder(view)

    override fun getItemViewId(): Int = R.layout.item_sub_task

    class SubTaskViewHolder(itemView: View?) : BaseViewHolder<SubTask>(itemView) {
        private val mCheckBox by lazy { itemView?.findViewById<CheckBox>(R.id.state_sub_task) }
        private val mSubTask by lazy { itemView?.findViewById<CustomFontTextView>(R.id.name_sub_task) }

        override fun onBind(item: SubTask, isLastPos: Boolean) {
            mCheckBox?.isChecked = item.isComplete
            mSubTask?.text = item.name
        }
    }

}