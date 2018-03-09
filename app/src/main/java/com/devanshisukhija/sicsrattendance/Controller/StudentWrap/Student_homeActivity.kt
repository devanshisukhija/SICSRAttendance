package com.devanshisukhija.sicsrattendance.Controller.StudentWrap

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.devanshisukhija.sicsrattendance.R
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_student_home.*
import kotlinx.android.synthetic.main.app_bar_faculty__home.*
import kotlinx.android.synthetic.main.app_bar_student_home.*
import java.net.URISyntaxException

class Student_homeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //BarChart
//    val barChart : BarChart = student_BarChart as BarChart



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        setSupportActionBar(student_toolbar)


        val toggle = ActionBarDrawerToggle(
                this, student_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        student_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        student_nav_view.setNavigationItemSelectedListener(this)
         val socket = Socket()

        socket.connect()

    }


    fun Socket() : Socket{
         val socket : Socket
            try {
                socket = IO.socket("localhost:4555")
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            }
        return socket
    }

    override fun onBackPressed() {
        if (student_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            student_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.student_sidenav_home -> {
//                // Handle the camera action
//            }
//            R.id.student_sidenav_lecture -> {
//
//            }
//            R.id.student_sidenav_schedule -> {
//
//            }
//            R.id.student_sidenav_report -> {
//
//            }
//        }

        student_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun student_nav_header_setting_clicked(view: View) {

    }

    fun student_nav_header_logout_clicked(view: View) {

    }
}


