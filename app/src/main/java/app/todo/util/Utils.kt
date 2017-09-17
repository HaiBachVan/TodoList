package app.todo.util

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ListPopupWindow
import app.todo.R
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @SuppressLint("SimpleDateFormat")
    private var mDateFormat = SimpleDateFormat()
    private var mLocale = Locale.getDefault()

    fun applyBlur(view: View) {
        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)
                view.buildDrawingCache()

                return true
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAMPMString(amPm: Int): String? {
        if (!(amPm == Calendar.AM || amPm == Calendar.PM)) {
            return null
        }
        val cal = Calendar.getInstance()
        if (mLocale != Locale.getDefault()) {
            mLocale = Locale.getDefault()
            mDateFormat = SimpleDateFormat()
        }
        cal.set(Calendar.AM_PM, amPm)
        mDateFormat.applyPattern("a")
        return mDateFormat.format(cal.time)
    }

    fun convertToFormatTime(context: Context, time: Int): String {
        val str: String

        val AM = getAMPMString(Calendar.AM)
        val PM = getAMPMString(Calendar.PM)

        var hour = time / 100
        val minute = time % 100

        if (get24HourMode(context)) {
            str = formatTwoDigitTime(hour) + ":" + formatTwoDigitTime(minute)
        } else {
            if (hour >= 12) {
                str = if (hour == 12) {
                    String.format("%s:%s %s", 12.toString() + "", formatTwoDigitTime(minute), PM)
                } else {
                    String.format("%s:%s %s", (hour - 12).toString() + "", formatTwoDigitTime(minute), PM)
                }
            } else {
                if (hour == 0) {
                    hour = 12
                }
                str = String.format("%s:%s %s", hour.toString() + "", formatTwoDigitTime(minute), AM)
            }
        }

        return str
    }

    private fun get24HourMode(context: Context): Boolean {
        return DateFormat.is24HourFormat(context)
    }

    @SuppressLint("DefaultLocale")
    private fun formatTwoDigitTime(time: Int): String {
        // format: 1 -> 01; 10 -> 10
        return String.format("%02d", time)
    }

    //+ START: ListName popup menu window
    fun calcSpinnerPopupHeight(context: Context, mPopupMenu: ListPopupWindow) {
        try {
            val displayMetrics = DisplayMetrics()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
            val height = getSpinnerHorizontalHeight(context, displayMetrics.heightPixels)
            mPopupMenu.height = height
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun adjustDropDownPopupOffset(context: Context, mPopupMenu: ListPopupWindow?) {
        if (mPopupMenu == null) {
            return
        }

        val res = context.resources
        mPopupMenu.horizontalOffset = res.getDimensionPixelSize(R.dimen.actionbar_header_spinner_vector_offset)
        if (mPopupMenu.isShowing) {
            mPopupMenu.show()
        }
    }

    private fun getSpinnerHorizontalHeight(context: Context, height: Int): Int {
        return height + 2 * context.resources.getDimensionPixelSize(R.dimen.actionbar_header_spinner_vector_offset)
    }
    //- END: ListName popup menu window

}
