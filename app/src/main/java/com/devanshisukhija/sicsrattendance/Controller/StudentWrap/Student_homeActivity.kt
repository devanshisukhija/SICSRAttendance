package com.devanshisukhija.sicsrattendance.Controller.StudentWrap

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Adapter.StudentScheduleAdapter
import com.devanshisukhija.sicsrattendance.Controller.LoginActivity
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.StudentDataService
import com.devanshisukhija.sicsrattendance.Services.UpdateScheduledLecturesService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_student_home.*
import kotlinx.android.synthetic.main.app_bar_faculty__home.*
import kotlinx.android.synthetic.main.app_bar_student_home.*
import kotlinx.android.synthetic.main.content_student_home.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Student_homeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //BarChart
//    val barChart : BarChart = student_BarChart as BarChart

    val TAG = "Student_homeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, student_drawer_layout, student_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
       student_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.setDrawerIndicatorEnabled(true);
        student_nav_view.setNavigationItemSelectedListener(this)
        // val socket = Socket()
       // socket.connect()


        //call to function to Set up Adapter.
        setupAdapters()
        //Setup UI
        setupStudentUI()

    }

    override fun onRestart() {
        super.onRestart()
        setupAdapters()
    }

    override fun onResume() {
        super.onResume()
        setupAdapters()
    }

    override fun onStart() {
        super.onStart()
        setupAdapters()
    }


//    fun Socket() : Socket{
//         val socket : Socket
//            try {
//                socket = IO.socket("192.168.1.103")
//            } catch (e: URISyntaxException) {
//                throw RuntimeException(e)
//            }
//        return socket
//    }

     override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.student_sidenav_home -> {
                student_drawer_layout.closeDrawers()
            }
            R.id.student_sidenav_lecture -> {
                startActivity(Intent(this, Student_LectureActivity :: class.java))
            }
            R.id.student_sidenav_report -> {
                startActivity(Intent(this, Student_ReportActivity :: class.java))
            }
        }

        student_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    fun student_nav_header_setting_clicked(view: View) {

    }

    fun student_nav_header_logout_clicked(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity :: class.java))
    }

    fun setupStudentUI() {
        student_home_dateStamp.text = getDate_of_month()
        student_home_dayStamp.text = getDay_of_week()

        val layout = View.inflate(this, R.layout.nav_header_student_home, null )
        (layout.findViewById<TextView>(R.id.student_nav_header_name)).text = StudentDataService.name
        (layout.findViewById<TextView>(R.id.student_nav_header_email)).text = StudentDataService.email
    }

    fun getDay_of_week() : String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE")
        val formatted = current.format(formatter).toString()

        return formatted
    }

    fun getDate_of_month() : String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd, MMM YYYY")
        val formatted = current.format(formatter).toString()

        return formatted
    }

    private fun setupAdapters() {
        UpdateScheduledLecturesService.getLectures { complete ->
            when(complete) {
                true -> { Log.d("TEXT : ", "Updated Lectues")
                    val adapter = StudentScheduleAdapter(this, UpdateScheduledLecturesService.student_lectures)
                    student_recyclerview.adapter = adapter
                    val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
                    student_recyclerview.layoutManager = layoutManager
                    student_recyclerview.setHasFixedSize(true)
                }
                false -> {
                    Log.d(TAG, "getLectures failed")
                }
            }
        }
    }//[End : Set Adapter]
}


