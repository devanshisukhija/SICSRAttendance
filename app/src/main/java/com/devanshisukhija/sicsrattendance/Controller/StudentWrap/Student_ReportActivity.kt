package com.devanshisukhija.sicsrattendance.Controller.StudentWrap

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.devanshisukhija.sicsrattendance.R
import kotlinx.android.synthetic.main.activity_student__report.*
import kotlinx.android.synthetic.main.app_bar_student__report.*

class Student_ReportActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student__report)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, student_report_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        student_report_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        student_report_nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

        }

        student_report_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
