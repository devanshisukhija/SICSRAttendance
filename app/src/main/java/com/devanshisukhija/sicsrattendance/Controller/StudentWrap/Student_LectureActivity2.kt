package com.devanshisukhija.sicsrattendance.Controller.StudentWrap

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.devanshisukhija.sicsrattendance.Adapter.StudentLectureAdapter
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.StudentLectureDataService
import com.devanshisukhija.sicsrattendance.Utility.STUDENT_COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE
import kotlinx.android.synthetic.main.activity_student__lecture2.*
import kotlinx.android.synthetic.main.app_bar_student__lecture2.*
import kotlinx.android.synthetic.main.content_student__lecture2.*

class Student_LectureActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student__lecture2)
        setSupportActionBar(student_lecture2_toolbar)
        val toggle = ActionBarDrawerToggle(
                this, student_lecture2_drawer_layout, student_lecture2_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        student_lecture2_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        student_lecture2_nav_view.setNavigationItemSelectedListener(this)
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
                student_lecture2_drawer_layout.closeDrawers()
            }
            R.id.student_sidenav_report -> {
                startActivity(Intent(this, Student_ReportActivity :: class.java))
            }

        }

        student_lecture2_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupAdapter() {
        val coursecode = intent.getStringExtra(STUDENT_COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE)
        StudentLectureDataService().fetchlectures(coursecode) { complete ->
            when(complete){

                true -> {
                    Log.d("StudentLectureact 2" , "true adapter"+ "lecture : "+ StudentLectureDataService().lectures )
                    val adapter = StudentLectureAdapter(this , StudentLectureDataService().lectures)
                    student_lecture_recyclerView2.adapter = adapter
                    val layoutManager = LinearLayoutManager(this)
                    student_lecture_recyclerView2.layoutManager = layoutManager
                    student_lecture_recyclerView2.setHasFixedSize(true)
                }
                false -> {

                }
            }
        }
    }
}
