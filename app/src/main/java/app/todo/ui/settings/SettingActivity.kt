package app.todo.ui.settings

import android.os.Bundle
import android.support.design.widget.TabLayout
import app.todo.R
import app.todo.core.presentation.BaseActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_settings.*

class SettingActivity : BaseActivity<SettingPresenter, SettingContract.View>(), SettingContract.View {

    private var mListener: TabLayout.OnTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            viewpager.currentItem = tab.position
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initInjection() {
        AndroidInjection.inject(this)
    }

    private fun initView() {
        viewpager.adapter = SettingFragmentPagerAdapter(supportFragmentManager, this@SettingActivity)

        tabLayout!!.setupWithViewPager(viewpager)
        tabLayout!!.addOnTabSelectedListener(mListener)

        btnBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    override val layout: Int
        get() = R.layout.activity_settings

}
