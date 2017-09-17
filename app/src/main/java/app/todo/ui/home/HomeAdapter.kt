package app.todo.ui.home

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import app.todo.R
import app.todo.core.data.db.entities.ListName
import app.todo.core.view.CustomFontTextView
import app.todo.ui.home.list.ListNameActivity
import app.todo.ui.task.TaskActivity
import app.todo.util.AppConstants

class HomeAdapter internal constructor(private val mActivity: Activity, private var mListNames: ArrayList<ListName>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_FOOTER = 1

    internal fun setListNames(listName: ArrayList<ListName>) {
        mListNames = listName
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        return if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_name, parent, false)
            ItemViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.item_create_list_name, parent, false)
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        if (viewType == VIEW_TYPE_ITEM) {
            val list = mListNames[position]
            val itemViewHolder = holder as ItemViewHolder

            itemViewHolder.bind(list)
            itemViewHolder.mItemLayout?.setOnClickListener {
                val intent = Intent(mActivity, TaskActivity::class.java)
                intent.putExtra(AppConstants.ARG_NAME_LIST, list.name)
                intent.putExtra(AppConstants.ARG_NAME_LIST_ID, list.id)
                mActivity.startActivity(intent)
            }
        } else {
            val footerViewHolder = holder as FooterViewHolder
            footerViewHolder.mLayout?.setOnClickListener {
                val intent = Intent(mActivity, ListNameActivity::class.java)
                mActivity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return mListNames.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mListNames.size) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    internal class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val mName by lazy { itemView?.findViewById<CustomFontTextView>(R.id.name) }
        val mItemLayout by lazy { itemView?.findViewById<LinearLayout>(R.id.item_layout_name) }

        fun bind(listName: ListName) {
            mName?.text = listName.name
        }
    }

    internal class FooterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val mLayout by lazy { itemView?.findViewById<LinearLayout>(R.id.item_layout_name) }
    }

}
