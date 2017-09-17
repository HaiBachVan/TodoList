package app.todo.util

import java.util.Locale

internal object LocaleUtils {
    private val defaultLanguage: String
        get() = Locale.getDefault().language

    val isJapanese: Boolean
        get() = defaultLanguage == Locale.JAPAN.language

    val isKorean: Boolean
        get() = defaultLanguage == Locale.KOREA.language

    val isUSEnglish: Boolean
        get() = Locale.getDefault().toString() == Locale.US.toString()

    // All of zh locale
    val isChinese: Boolean
        get() = defaultLanguage == Locale.CHINESE.language

    val isThai: Boolean
        get() = "th" == defaultLanguage

    val isArabic: Boolean
        get() = "ar" == defaultLanguage
}
