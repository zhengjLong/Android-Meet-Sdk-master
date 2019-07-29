package com.systec.android_my_sdk_develop

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.library.base.MeetingUtils
import com.library.base.utils.SdkUtil

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        SdkUtil.openWebActivity(this,"测试","https://www.baidu.com")


        val meetingUtil = MeetingUtils()

        meetingUtil.initSdk(this,object : MeetingUtils.MeetingCallBack{
            override fun meetingErrorMessage(errorCode: Int, message: String) {
            }

            override fun meetingDisconnected() {
            }

            override fun meetingSuccessCallBack() {
            }

            override fun hardwareMeetingState(success: Boolean) {
            }
        })

        Handler().postDelayed({meetingUtil.joinMeeting(this,"1234567890","","jerome",true)},5000)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
