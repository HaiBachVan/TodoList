package app.todo.util

import android.content.Context
import android.text.format.DateFormat
import app.todo.R
import java.util.*

object DateFormatUtils {
    private val DATE_FORMAT_YMD = "YMD"
    private val DATE_FORMAT_DMY = "DMY"

    fun getFormattedDate(time: Long, context: Context): String {
        if (LocaleUtils.isThai) {
            val mLocaleLanguage1 = DateFormat.format("E", time).toString() + ", "
            val mLocaleLanguage2 = DateFormat.format("dd/MM/yy", time).toString()
            return DateFormat.format(mLocaleLanguage1 + mLocaleLanguage2, time).toString()
        }
        return DateFormat.format(getDateFormatterString(context), time).toString()
    }

    private val dateFormatOrder: CharArray
        get() {
            val pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "yyyyMMMdd")
            return getDateFormatOrder(pattern)
        }

    private fun getDateFormatOrder(pattern: String): CharArray {
        val result = CharArray(3)
        var resultIndex = 0
        var sawDay = false
        var sawMonth = false
        var sawYear = false

        val length = pattern.length
        var i = 0
        while (i < length) {
            val ch = pattern[i]
            if (ch == 'd' || ch == 'L' || ch == 'M' || ch == 'y') {
                if (ch == 'd' && !sawDay) {
                    result[resultIndex++] = 'd'
                    sawDay = true
                } else if ((ch == 'L' || ch == 'M') && !sawMonth) {
                    result[resultIndex++] = 'M'
                    sawMonth = true
                } else if (ch == 'y' && !sawYear) {
                    result[resultIndex++] = 'y'
                    sawYear = true
                }
            } else if (ch == 'G') { // NOPMD
                // Ignore the era specifier, if present.
            } else if (ch in 'a'..'z' || ch in 'A'..'Z') {
                throw IllegalArgumentException("Bad pattern character '" + ch + "' in "
                        + pattern)
            } else if (ch == '\'') {
                if (i < length - 1 && pattern[i + 1] == '\'') {
                    ++i
                } else {
                    i = pattern.indexOf('\'', i + 1)
                    if (i == -1) {
                        throw IllegalArgumentException("Bad quoting in " + pattern)
                    }
                    ++i
                }
            }
            ++i
        }
        return result
    }

    private fun getDateFormatterString(context: Context): String {
        val systemDateFormat = String(dateFormatOrder)
        val resources = context.resources
        val dayPostfix = resources.getString(R.string.day_postfix)
        val yearPostfix = resources.getString(R.string.year_postfix)
        var formatterString: String

        when {
            LocaleUtils.isJapanese -> formatterString = when {
                systemDateFormat.equals("MDY", ignoreCase = true) -> "(EEE)MMMd" + dayPostfix + "yyyy" + yearPostfix
                systemDateFormat.equals(DATE_FORMAT_YMD, ignoreCase = true) -> "yyyy" + yearPostfix + "MMMd" + dayPostfix + "(EEE)"
                systemDateFormat.equals(DATE_FORMAT_DMY, ignoreCase = true) -> "(EEE)d" + dayPostfix + "MMMyyyy" + yearPostfix
                else -> "(EEE)MMMd" + dayPostfix + "yyyy" + yearPostfix
            }
            LocaleUtils.isChinese -> formatterString = when {
                systemDateFormat.equals(DATE_FORMAT_YMD, ignoreCase = true) -> "yyyy$yearPostfix MMM d$dayPostfix, EEE"
                systemDateFormat.equals(DATE_FORMAT_DMY, ignoreCase = true) -> "d$dayPostfix MMM yyyy$yearPostfix, EEE"
                else -> "MMM d$dayPostfix yyyy$yearPostfix, EEE"
            }
            LocaleUtils.isUSEnglish -> formatterString = "EEE, MMMM d$dayPostfix, yyyy$yearPostfix"
            LocaleUtils.isArabic -> formatterString = when {
                systemDateFormat.equals(DATE_FORMAT_YMD, ignoreCase = true) -> "yyyy$yearPostfix MMM d$dayPostfix EEE"
                systemDateFormat.equals(DATE_FORMAT_DMY, ignoreCase = true) -> "EEE d$dayPostfix MMM yyyy$yearPostfix"
                else -> "EEE MMM d$dayPostfix yyyy$yearPostfix"
            }
            LocaleUtils.isKorean -> formatterString = when {
                systemDateFormat.equals(DATE_FORMAT_YMD, ignoreCase = true) -> "yyyy$yearPostfix MMM d$dayPostfix EEEE"
                systemDateFormat.equals(DATE_FORMAT_DMY, ignoreCase = true) -> "(EEE) d$dayPostfix MMM yyyy$yearPostfix"
                else -> "(EEE) MMM d$dayPostfix yyyy$yearPostfix"
            }
            else -> formatterString = when {
                systemDateFormat.equals(DATE_FORMAT_YMD, ignoreCase = true) -> "yyyy$yearPostfix MMMM d$dayPostfix, EEE"
                systemDateFormat.equals(DATE_FORMAT_DMY, ignoreCase = true) -> "EEE, d$dayPostfix MMMM yyyy$yearPostfix"
                else -> "EEE, MMMM d$dayPostfix yyyy$yearPostfix"
            }
        }


        if (LocaleUtils.isArabic) {
            formatterString = formatterString.replace(",", resources.getString(R.string.arabic_comma))
        }

        return formatterString
    }

    fun getFormatDateTime(mContext: Context, settingTime: Long, isAbbrev: Boolean, isTTS: Boolean): String {
        if (Locale.getDefault().language == Locale.KOREA.language
                || Locale.getDefault().language == Locale.JAPAN.language
                || Locale.getDefault().language.equals("nb", ignoreCase = true)
                || Locale.getDefault().language.equals("da", ignoreCase = true)
                || Locale.getDefault().language.equals("fr", ignoreCase = true)
                || Locale.getDefault().language.equals("lt", ignoreCase = true)
                || Locale.getDefault().language.equals("hu", ignoreCase = true)
                || Locale.getDefault().language.equals("pa", ignoreCase = true)
                || Locale.getDefault().language.equals("sk", ignoreCase = true)
                || mContext.resources.configuration.locale.displayLanguage == "polski") {
            var flags = 0
            if (isAbbrev) {
                if (!isTTS) {
                    flags = flags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
                }
            } else { //[JPN] Date format for Alert Screen
                if (Locale.getDefault().language == Locale.JAPAN.language) {
                    var weekFlags = 0
                    weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
                    val selectDayOfWeek = android.text.format.DateUtils.formatDateTime(mContext,
                            settingTime, weekFlags)

                    val selectDate = android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                            flags)

                    return selectDate + ' ' + selectDayOfWeek
                }
            }
            flags = flags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
            flags = flags or android.text.format.DateUtils.FORMAT_SHOW_DATE

            return android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                    flags)
        } else if (Locale.getDefault().language.equals("ar", ignoreCase = true)) {
            var weekFlags = 0
            if (!isTTS) {
                weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
            val selectDayOfWeek = android.text.format.DateUtils.formatDateTime(mContext,
                    settingTime, weekFlags)

            var dateFlags = 0
            if (isAbbrev) {
                dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_SHOW_DATE
            val selectDate = android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                    dateFlags)
            return selectDayOfWeek + String.format("\u060C ", ",") + selectDate
        } else if (Locale.getDefault().language.equals("my", ignoreCase = true)) {
            val flags = 0
            var weekFlags = 0
            if (isAbbrev && !isTTS) {
                weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
            val selectDayOfWeek = android.text.format.DateUtils.formatDateTime(mContext,
                    settingTime, weekFlags)

            val selectDate = android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                    flags)

            return selectDayOfWeek + ", " + selectDate
        } else if (Locale.getDefault().language.equals("de", ignoreCase = true)) {
            var weekFlags = 0
            if (isAbbrev && !isTTS) {
                weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
            val selectDayOfWeek = android.text.format.DateUtils.formatDateTime(mContext,
                    settingTime, weekFlags)

            val dateFlags = 0
            val selectDate = android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                    dateFlags)

            return selectDayOfWeek + ", " + selectDate
        } else if (Locale.getDefault().language == Locale.CHINA.language) {
            var weekFlags = 0
            if (!isTTS) {
                weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
            val selectDayOfWeek = android.text.format.DateUtils.formatDateTime(mContext,
                    settingTime, weekFlags)

            var dateFlags = 0
            if (isAbbrev) {
                dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_SHOW_DATE
            val selectDate = android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                    dateFlags)

            return selectDate + ", " + selectDayOfWeek
        } else {
            var weekFlags = 0
            if (!isTTS) {
                weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
            val selectDayOfWeek = android.text.format.DateUtils.formatDateTime(mContext,
                    settingTime, weekFlags)

            var dateFlags = 0
            if (isAbbrev) {
                dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
            }
            dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_SHOW_DATE
            val selectDate = android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                    dateFlags)

            return selectDayOfWeek + ", " + selectDate
        }
    }

    fun getFormatDateTime(mContext: Context, settingTime: Long, isAbbrev: Boolean): String {
        var weekFlags = 0

        weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
        weekFlags = weekFlags or android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY
        val selectDayOfWeek = android.text.format.DateUtils.formatDateTime(mContext,
                settingTime, weekFlags)

        var dateFlags = 0
        if (isAbbrev) {
            dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_ABBREV_ALL
        }
        dateFlags = dateFlags or android.text.format.DateUtils.FORMAT_SHOW_DATE
        val selectDate = android.text.format.DateUtils.formatDateTime(mContext, settingTime,
                dateFlags)

        return selectDayOfWeek + " " + selectDate

    }

    fun formatDateString(context: Context, date: Long): String {
        val dateFormat = String(DateFormat.getDateFormatOrder(context))
        var format = context.getString(R.string.ddmmyy)
        if ("MDY".equals(dateFormat, ignoreCase = true)) {
            format = context.getString(R.string.mmddyy)
        } else if ("YMD".equals(dateFormat, ignoreCase = true) || LocaleUtils.isChinese || LocaleUtils.isArabic) {
            format = context.getString(R.string.yymmdd)
        }

        return DateFormat.format(format, date) as String
    }
}
