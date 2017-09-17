package app.todo.core.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import app.todo.R

class CustomFontEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CustomFontEditText)
            val fontName = a.getString(R.styleable.CustomFontEditText_fontText)

            if (fontName != null) {
                val myTypeface = Typeface.createFromAsset(context.assets, "fonts/" + fontName)
                typeface = myTypeface
            }

            a.recycle()
        }
    }


}
