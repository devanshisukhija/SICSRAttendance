package com.devanshisukhija.sicsrattendance.Controller.FacultyWrap

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.devanshisukhija.sicsrattendance.Adapter.FacultyLectureAdapter
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.FacultyLectureDataService
import com.devanshisukhija.sicsrattendance.Utility.COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE
import kotlinx.android.synthetic.main.activity_faculty__lecture2.*
import kotlinx.android.synthetic.main.app_bar_faculty__lecture2.*
import kotlinx.android.synthetic.main.content_faculty__lecture2.view.*


class Faculty_LectureActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty__lecture2)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, faculty_lecture2_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        faculty_lecture2_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        faculty_lecture2_nav_view.setNavigationItemSelectedListener(this)

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


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.faculty_sidenav_home -> {
                startActivity(Intent(this, Faculty_HomeActivity :: class.java))
            }
            R.id.faculty_sidenav_lecture -> {
                startActivity(Intent(this, Faculty_LectureActivity:: class.java))
            }
        }
        faculty_lecture2_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupAdapter() {
        val layout = View.inflate(this, R.layout.content_faculty__lecture2, null)
        val coursecode = intent.getStringExtra(COURSE_TO_LECTURE_INTENT_EXTRA_COURSE_CODE)
        FacultyLectureDataService.fetchlectures(coursecode) {complete ->
            when(complete){
                true ->{
                    val adapter = FacultyLectureAdapter(this, FacultyLectureDataService.lectures)
                    (layout.faculty_lecture_recyclerView2).adapter = adapter
                    val layoutManager = LinearLayoutManager(this)
                    (layout.faculty_lecture_recyclerView2).layoutManager = layoutManager
                    (layout.faculty_lecture_recyclerView2).setHasFixedSize(true)
                }
                false -> {

                }
            }
        }
    }
}
