package com.devanshisukhija.sicsrattendance.Controller

import FacultyScheduleAdapter
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.UpdateScheduledLecturesService
import kotlinx.android.synthetic.main.activity_faculty__home.*
import kotlinx.android.synthetic.main.activity_student_home.*
import kotlinx.android.synthetic.main.app_bar_faculty__home.*
import kotlinx.android.synthetic.main.content_faculty__home.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Faculty_HomeActivity : AppCompatActivity() {
        //[Start : onCreat] --> func #1
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty__home)
            setSupportActionBar(toolbar)

            val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer_layout.addDrawerListener(toggle)
            toggle.syncState()
            //[Function call to set the Schedule Adapters]
            setupAdapters()
            //[Function call to setup UI]
            setupUI()
    }//[End : func# 1]

    //[Start : func#2]
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }//[End : func#2]

     override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.faculty_sidenav_home -> {
                // Handle the camera action
            }
            R.id.faculty_sidenav_home -> {

            }
            R.id.faculty_sidenav_lecture -> {

            }
            R.id.faculty_sidenav_schedule -> {

            }
      }

        drawer_layout.closeDrawer(GravityCompat.START)
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

    fun setupUI(){
        faculty_home_dateStamp.text = getDate_of_month()
        faculty_home_dayStamp.text = getDay_of_week()
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
