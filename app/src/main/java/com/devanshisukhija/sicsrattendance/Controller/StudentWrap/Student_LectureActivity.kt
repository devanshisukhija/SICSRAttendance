package com.devanshisukhija.sicsrattendance.Controller.StudentWrap

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.devanshisukhija.sicsrattendance.Adapter.StudentCourseAdapter
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.StudentCourseDataService
import com.devanshisukhija.sicsrattendance.Utility.STUDENT_COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE
import kotlinx.android.synthetic.main.activity_student__lecture.*
import kotlinx.android.synthetic.main.app_bar_student__lecture.*
import kotlinx.android.synthetic.main.content_student__lecture.*

class Student_LectureActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student__lecture)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, student_lecture_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        student_lecture_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        student_lecture_nav_view.setNavigationItemSelectedListener(this)
        setupAdapter()
    }

    override fun onRestart() {
        super.onRestart()
        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        setupAdapter()
    }

    override fun onStart(){
        super.onStart()
        setupAdapter()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.student_sidenav_home -> {
                startActivity(Intent(this, Student_homeActivity :: class.java))
            }
            R.id.student_sidenav_lecture -> {
                student_lecture_drawer_layout.closeDrawers()
            }
            R.id.student_sidenav_report -> {
                startActivity(Intent(this, Student_ReportActivity :: class.java))
            }
        }
        student_lecture_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupAdapter() {
        StudentCourseDataService.fetchCourseData { complete ->
            val adapter = StudentCourseAdapter(this, StudentCourseDataService.courses) {course ->

                val courseintent = Intent(this, Student_LectureActivity2 :: class.java)
                courseintent.putExtra(STUDENT_COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE, course.course_code)
                startActivity(courseintent)
            }
            student_lecture_recyclerView.adapter = adapter
            val layoutManager= LinearLayoutManager(this)
            student_lecture_recyclerView.layoutManager= layoutManager
            student_lecture_recyclerView.setHasFixedSize(true)
        }
    }
}
