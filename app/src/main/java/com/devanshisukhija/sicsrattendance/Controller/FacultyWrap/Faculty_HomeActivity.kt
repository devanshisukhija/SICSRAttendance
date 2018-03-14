package com.devanshisukhija.sicsrattendance.Controller.FacultyWrap

import FacultyScheduleAdapter
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.format.Formatter
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.FacultyDataService
import com.devanshisukhija.sicsrattendance.Services.UpdateScheduledLecturesService
import com.devanshisukhija.sicsrattendance.Utility.FACULTY_HOME_TO_RECORD_INTENT_START_RECORD
import com.devanshisukhija.sicsrattendance.Utility.LOGIN_TO_FACULTY_INTENT_USER_EMAIL
import com.devanshisukhija.sicsrattendance.Utility.LOGIN_TO_FACULTY_INTENT_USER_NAME
import kotlinx.android.synthetic.main.activity_faculty__home.*
import kotlinx.android.synthetic.main.app_bar_faculty__home.*
import kotlinx.android.synthetic.main.content_faculty__home.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




class Faculty_HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val TAG = "Faculty_homeActivity"
        //[Start : onCreat] --> func #1
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty__home)
            setSupportActionBar(toolbar)
            val toggle = ActionBarDrawerToggle(
                this, faculty_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            faculty_drawer_layout.addDrawerListener(toggle)
            toggle.syncState()

            //[Function call to set the Schedule Adapters]
            setupAdapters()
            //[Function call to setup UI]
            val facultyDataService  = FacultyDataService
            setupUI()
    }//[End : func# 1]

     override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.faculty_sidenav_home -> {
                startActivity(Intent(this, this :: class.java))
            }
            R.id.faculty_sidenav_schedule -> {
                startActivity(Intent(this, Faculty_ScheduleActivity :: class.java))
            }
            R.id.faculty_sidenav_lecture -> {
                startActivity(Intent(this@Faculty_HomeActivity, Faculty_LectureActivity:: class.java))
            }
      }
        faculty_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


        //[Start : Set Adapter]  --> func# 3
    private fun setupAdapters() {
        UpdateScheduledLecturesService.getLectures { complete ->
            when(complete) {
                true -> { Log.d("TEXT : ", "Updated Lectues")
                    val adapter = FacultyScheduleAdapter(this, UpdateScheduledLecturesService.lectures)
                    faculty_recyclerview.adapter = adapter
                    val layoutManager = LinearLayoutManager(this@Faculty_HomeActivity)
                    faculty_recyclerview.layoutManager = layoutManager
                    faculty_recyclerview.setHasFixedSize(true)
                }
                false -> {
                    //TODO : add code
                }
            }
        }
    }//[End : Set Adapter]

    fun nav_header_logout_clicked(view : View) {

    }

    fun nav_header_setting_clicked(view : View) {

    }

    fun faculty_record_attendanc_clicked(view : View) {
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wifiManager.connectionInfo.networkId)
        if(wifiManager.isWifiEnabled){
            Log.d("WIFI if ", ip)
            val record_activity_intent = Intent(this, Faculty_RecordActivity::class.java)
            val start_recored = true
            record_activity_intent.putExtra(FACULTY_HOME_TO_RECORD_INTENT_START_RECORD , start_recored)
            startActivity(record_activity_intent)
        } else {
            Log.d("WIFI else ", ip)
        }
    }

    fun setupUI(){
        faculty_home_dateStamp.text = getDate_of_month()
        faculty_home_dayStamp.text = getDay_of_week()
        val previousIntentValueName = intent.getCharSequenceExtra(LOGIN_TO_FACULTY_INTENT_USER_NAME)
        val previousIntentValueEmail = intent.getCharSequenceExtra(LOGIN_TO_FACULTY_INTENT_USER_EMAIL)
        println(TAG + "name : " + previousIntentValueName + ",  Email : "+ previousIntentValueEmail )
        //faculty_nav_header_name.text = previousIntentValueName
    }

    fun getDay_of_week() : String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE")
        val formatted = current.format(formatter).toString()

        return formatted
    }

    fun getDate_of_month() : String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd, MMM YYYY")
        val formatted = current.format(formatter).toString()

        return formatted
    }
}
