package com.devanshisukhija.sicsrattendance.Controller

import FacultyScheduleAdapter
import android.os.Bundle
import android.support.design.widget.NavigationView
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
import kotlinx.android.synthetic.main.app_bar_faculty__home.*
import kotlinx.android.synthetic.main.content_faculty__home.*

class Faculty_HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var adapter : FacultyScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty__home)
         setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        faculty_nav_view.setNavigationItemSelectedListener(this)

        setupAdapters()


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupAdapters() {
        if(UpdateScheduledLecturesService.lectures.isNotEmpty()){
            UpdateScheduledLecturesService.lectures.clear()
            UpdateScheduledLecturesService.getLectures { complete ->
                when (complete) {
                    true -> {
                        adapter = FacultyScheduleAdapter(this ,UpdateScheduledLecturesService.lectures)
                        faculty_recyclerview.adapter = adapter
                        val layoutManager = LinearLayoutManager(this)
                        faculty_recyclerview.layoutManager = layoutManager
                        faculty_recyclerview.setHasFixedSize(true)
                    }
                    false -> Log.d("Fail" , "58,Faculty_home")
                }
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.nav_camera -> {
//                // Handle the camera action
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {
//
//            }
//        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun nav_header_logout_clicked(view : View) {

    }

    fun nav_header_setting_clicked(view : View) {

    }
}
