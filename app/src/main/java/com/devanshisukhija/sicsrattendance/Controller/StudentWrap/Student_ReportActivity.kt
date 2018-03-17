package com.devanshisukhija.sicsrattendance.Controller.StudentWrap

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.devanshisukhija.sicsrattendance.Adapter.StudentCourseAdapter
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.StudentCourseDataService
import kotlinx.android.synthetic.main.activity_student__report.*
import kotlinx.android.synthetic.main.app_bar_student__report.*
import kotlinx.android.synthetic.main.content_student__report.*

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

        setupAdapter()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.student_sidenav_home -> {
                startActivity(Intent(this, Student_homeActivity :: class.java))

            }
            R.id.student_sidenav_lecture -> {
                startActivity(Intent(this, Student_LectureActivity :: class.java))
            }
            R.id.student_sidenav_report -> {
                student_report_drawer_layout.closeDrawers()

            }
        }

        student_report_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun setupAdapter() {
        StudentCourseDataService.fetchCourseData { complete ->
            val adapter = StudentCourseAdapter(this, StudentCourseDataService.courses) { course ->
                println("clickedd")
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("You Attendance for course " + course.course_name + " is :")
                alertDialog.setMessage("75%")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", {
                    dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                alertDialog.show()
            }
            student_report_recyclerView.adapter = adapter
            val layoutManager= LinearLayoutManager(this)
            student_report_recyclerView.layoutManager= layoutManager
            student_report_recyclerView.setHasFixedSize(true)
        }
    }
}
