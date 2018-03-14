package com.devanshisukhija.sicsrattendance.Controller.FacultyWrap

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import com.devanshisukhija.sicsrattendance.Adapter.FacultyCourseAdapter
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.FacultyCourseDataService
import kotlinx.android.synthetic.main.activity_faculty__lecture.*
import kotlinx.android.synthetic.main.app_bar_faculty__lecture.*
import kotlinx.android.synthetic.main.content_faculty__lecture.*

class Faculty_LectureActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val TAG = "Faculty_LectureActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty__lecture)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, faculty_lecture_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        faculty_lecture_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        faculty_lecture_nav_view.setNavigationItemSelectedListener(this)

        setupAdapters()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.faculty_sidenav_home -> {
                startActivity(Intent(this, Faculty_HomeActivity :: class.java))
            }
            R.id.faculty_sidenav_schedule -> {
                startActivity(Intent(this, Faculty_ScheduleActivity :: class.java))
            }
            R.id.faculty_sidenav_lecture -> {
                startActivity(Intent(this, Faculty_LectureActivity:: class.java))
            }

        }

        faculty_lecture_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupAdapters() {
        println(FacultyCourseDataService.courses)

        FacultyCourseDataService.fetchcourseData { complete ->
            when(complete) {
                true -> {val adapter = FacultyCourseAdapter(this, FacultyCourseDataService.courses)
                    faculty_lecture_recyclerView.adapter = adapter
                    val layoutManager = LinearLayoutManager(this@Faculty_LectureActivity)
                    faculty_lecture_recyclerView.layoutManager = layoutManager
                    faculty_lecture_recyclerView . setHasFixedSize (true)
                }
                false -> {
                    Log.d(TAG, "fetch was not complete")
                }
            }
        }
    }//[End : Set Adapter]

}
