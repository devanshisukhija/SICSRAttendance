package com.devanshisukhija.sicsrattendance.Controller.FacultyWrap

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.devanshisukhija.sicsrattendance.R
import kotlinx.android.synthetic.main.activity_faculty__schedule.*
import kotlinx.android.synthetic.main.app_bar_faculty__schedule.*

class Faculty_ScheduleActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty__schedule)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.faculty_sidenav_home -> {
                startActivity(Intent(this, this :: class.java))
            }
            R.id.faculty_sidenav_schedule -> {
                startActivity(Intent(this, this :: class.java))
            }
            R.id.faculty_sidenav_lecture -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
