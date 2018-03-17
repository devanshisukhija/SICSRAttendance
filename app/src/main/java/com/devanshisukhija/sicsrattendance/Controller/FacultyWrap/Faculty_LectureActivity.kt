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
import com.devanshisukhija.sicsrattendance.Utility.COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE
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
    override fun onRestart() {
        super.onRestart()
        setupAdapters()
    }

    override fun onResume() {
        super.onResume()
        setupAdapters()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.faculty_sidenav_home -> {
                startActivity(Intent(this, Faculty_HomeActivity :: class.java))
            }
            R.id.faculty_sidenav_lecture -> {
                faculty_lecture_drawer_layout.closeDrawers()
            }
        }
        faculty_lecture_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupAdapters() {
        println(FacultyCourseDataService.courses)

        FacultyCourseDataService.fetchCourseData { complete ->
            when(complete) {
                true -> {
                    println(TAG + ": " + FacultyCourseDataService.courses)
                    val adapter = FacultyCourseAdapter(this, FacultyCourseDataService.courses){course ->

                        val courseintent = Intent(this, Faculty_LectureActivity2 :: class.java)
                        courseintent.putExtra(COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE , course.course_code)
                        println(TAG+ "...."+ course.course_code)
                        startActivity(courseintent)
                    }
                    faculty_lecture_recyclerView.adapter = adapter
                    val layoutManager = LinearLayoutManager(this@Faculty_LectureActivity)
                    faculty_lecture_recyclerView.layoutManager = layoutManager
                    faculty_lecture_recyclerView.setHasFixedSize(true)
                }
                false -> {
                    Log.d(TAG, "fetch was not complete")
                }
            }
        }
    }//[End : Set Adapter]
}
