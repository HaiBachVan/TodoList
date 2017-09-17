package app.todo.core.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import app.todo.R
import app.todo.core.view.CustomFontTextView
import app.todo.item.ItemPopup
import java.util.*

class BasePopupAdapter(context: Context, private val mLayout: Int, private val mObjects: ArrayList<ItemPopup>) : ArrayAdapter<ItemPopup>(context, mLayout, mObjects) {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View? {
        val mConvertView = mInflater.inflate(mLayout, parent, false)

        val textItem = mConvertView?.findViewById<CustomFontTextView>(R.id.title)
        val imageView = mConvertView?.findViewById<ImageView>(R.id.image)

        if (position < mObjects.size) {
            val itemPopup = mObjects[position]
            val title = itemPopup.title
            textItem?.text = title

            if (itemPopup.image != -1000) {
                imageView?.setImageResource(itemPopup.image)
            } else {
                imageView?.visibility = View.INVISIBLE
            }
        }

        return mConvertView
    }

}
