package app.todo.ui.settings

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import app.todo.R
import app.todo.ui.settings.account.AccountFragment
import app.todo.ui.settings.extras.ExtrasFragment
import app.todo.ui.settings.general.GeneralFragment

class SettingFragmentPagerAdapter internal constructor(fragmentManager: FragmentManager, private val mContext: Context) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            TAB_ACCOUNT -> AccountFragment.newInstance()
            TAB_GENERAL -> GeneralFragment.newInstance()
            TAB_EXTRAS -> ExtrasFragment.newInstance()
            else -> null
        }
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null

        when (position) {
            TAB_ACCOUNT -> title = mContext.getString(R.string.text_account)
            TAB_GENERAL -> title = mContext.getString(R.string.text_general)
            TAB_EXTRAS -> title = mContext.getString(R.string.text_extras)
        }

        return title
    }

    companion object {
        private val PAGE_COUNT = 3

        private val TAB_ACCOUNT = 0
        private val TAB_GENERAL = 1
        private val TAB_EXTRAS = 2
    }

}
