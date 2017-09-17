package app.todo.core.presentation

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<in D>(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(item: D, isLastPos: Boolean = false)

}