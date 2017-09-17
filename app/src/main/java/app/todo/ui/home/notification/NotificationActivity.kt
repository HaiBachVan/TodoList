package app.todo.ui.home.notification

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import app.todo.R
import app.todo.core.presentation.BaseActivity
import app.todo.item.ItemNotification
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_notification.*
import java.util.*

class NotificationActivity : BaseActivity<NotificationPresenter, NotificationContract.View>()
        , NotificationContract.View, View.OnClickListener {

    private var mAdapter: NotificationAdapter? = null
    private var mList: ArrayList<ItemNotification> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initInjection() {
        AndroidInjection.inject(this)
    }

    private fun initView() {
        mList = ArrayList()

        // TODO - DEMO
        mList.add(ItemNotification("Go to school", "3 days ago"))
        mList.add(ItemNotification("Listen to music", "4 days ago"))
        mList.add(ItemNotification("Say you do", "5 days ago"))
        mList.add(ItemNotification("Proud of you", "6 days ago"))

        val layoutManager = LinearLayoutManager(this)
        listNotification!!.layoutManager = layoutManager

        mAdapter = NotificationAdapter()
        mAdapter?.dataSource = mList
        listNotification!!.adapter = mAdapter

        btnBack.setOnClickListener(this)
    }

    override val layout: Int
        get() = R.layout.activity_notification

    override fun onClick(view: View?) {
        if (view?.id == R.id.btnBack) {
            super.onBackPressed()
        }
    }

}
