package app.todo.ui.home.notification

import android.view.View
import app.todo.R
import app.todo.core.presentation.BaseAdapter
import app.todo.core.presentation.BaseViewHolder
import app.todo.core.view.CustomFontTextView
import app.todo.item.ItemNotification

class NotificationAdapter : BaseAdapter<ItemNotification, NotificationAdapter.NotificationViewHolder>() {

    override fun instantiateViewHolder(view: View?): NotificationViewHolder = NotificationViewHolder(view)

    override fun getItemViewId(): Int = R.layout.item_notification

    class NotificationViewHolder(itemView: View?) : BaseViewHolder<ItemNotification>(itemView) {
        private val mTaskName by lazy { itemView?.findViewById<CustomFontTextView>(R.id.taskName) }
        private val mTimeAgo by lazy { itemView?.findViewById<CustomFontTextView>(R.id.timeAgo) }
        private val mLine by lazy { itemView?.findViewById<View>(R.id.divider) }

        override fun onBind(item: ItemNotification, isLastPos: Boolean) {
            mTaskName?.text = item.task
            mTimeAgo?.text = item.timeAgo

            if (isLastPos) {
                mLine?.visibility = View.GONE
            }
        }

    }
}
