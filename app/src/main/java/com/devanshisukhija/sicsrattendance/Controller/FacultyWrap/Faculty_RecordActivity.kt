package com.devanshisukhija.sicsrattendance.Controller.FacultyWrap

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.devanshisukhija.sicsrattendance.Model.LearningObjectives
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.FacultyDataService
import com.devanshisukhija.sicsrattendance.Utility.DatabaseHelper
import com.devanshisukhija.sicsrattendance.Utility.FACULTY_HOME_TO_RECORD_INTENT_START_RECORD
import com.devanshisukhija.sicsrattendance.Utility.FacultyRecordHelper
import com.devanshisukhija.sicsrattendance.Utility.PrefTimer
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_faculty__record.*
import kotlinx.android.synthetic.main.app_bar_faculty__record.*
import kotlinx.android.synthetic.main.content_faculty__record.*


class Faculty_RecordActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    //firebase Reference:
    private var mDatabase : FirebaseDatabase? = null

    enum class TimerState{
        Stopped, Running
    }
    val facultyRecordHelper = FacultyRecordHelper()
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var secondsRemaining: Long = 0
    private val TAG = "Faculty_RecordActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty__record)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, faculty_record_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        faculty_record_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        faculty_record_nav_view.setNavigationItemSelectedListener(this)

        mDatabase = DatabaseHelper.instance

        val previous_intent_val = intent.getBooleanExtra(FACULTY_HOME_TO_RECORD_INTENT_START_RECORD, false)
        println(previous_intent_val)
        when(previous_intent_val){
            true -> {
                facultyRecordHelper.check_for_lecture() {
                    when(it){
                        true -> {
                            if(facultyRecordHelper.count > 1){
                                val alertDilog = AlertDialog.Builder(this@Faculty_RecordActivity).create()
                                alertDilog.setTitle("More that 1 lecture assigned at this time.")
                                alertDilog.setMessage("It seems there are multple lectures at the moment. ")
                                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
                                    dialogInterface, i ->
                                    startActivity(Intent(this, Faculty_HomeActivity :: class.java))
                                })
                                alertDilog.show()
                                Log.d(TAG, "complete check_for_lecture")
                            }
                            else if(facultyRecordHelper.count == 1){
                                var course = facultyRecordHelper.lectureName
                                var classroom = facultyRecordHelper.lectureVenue
                                Log.d(TAG, "count is one" )
                                val alertDilog = AlertDialog.Builder(this@Faculty_RecordActivity).create()
                                alertDilog.setTitle("Confirm Course and Classroom.")
                                alertDilog.setMessage("Coures  : " + course)
                                alertDilog.setMessage("Classroom : " + classroom)
                                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "CONFIRM", {
                                    dialogInterface, i ->
                                    dialogInterface.dismiss()
                                })
                                alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", {
                                    dialog, which ->
                                    startActivity(Intent(this, Faculty_HomeActivity :: class.java))
                                    Toast.makeText(applicationContext, "Try again" , Toast.LENGTH_LONG).show()
                                })
                                alertDilog.show()
                                timerLengthSeconds = facultyRecordHelper.getDifference()
                                secondsRemaining = facultyRecordHelper.getDifference()
                                facutly_record_display_lectureInfo.text = facultyRecordHelper.lectureName
                                startTimer()
                                timerState = TimerState.Running
                            } else {
                                val alertDilog = AlertDialog.Builder(this@Faculty_RecordActivity).create()
                                alertDilog.setTitle("NO LECTURE.")
                                alertDilog.setMessage("It seems there are no lectures at the moment. For more details check your schedule.")

                                alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", {
                                    dialogInterface, i ->
                                    startActivity(Intent(this, Faculty_HomeActivity :: class.java))
                                })
                                alertDilog.show()
                                Log.d(TAG, "The count is not more or equal to 1" + facultyRecordHelper.count + "  " + facultyRecordHelper.lectureID)
                            }
                        }
                        false -> Log.d(TAG, "check_for_lecture failed")
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running){
            timer.cancel()
            //TODO: start background timer and show notification
        }

        PrefTimer.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefTimer.setSecondsRemaining(secondsRemaining, this)
        PrefTimer.setTimerState(timerState, this)
    }

    private fun initTimer(){
        timerState = PrefTimer.getTimerState(this)

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running)
            PrefTimer.getSecondsRemaining(this)
        else
            timerLengthSeconds

        //TODO: change secondsRemaining according to where the background timer stopped

        //resume where we left off
        if (timerState == TimerState.Running)
            startTimer()

        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        setNewTimerLength()

        PrefTimer.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateCountdownUI()
    }

    private fun startTimer(){
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000

                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = PrefTimer.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefTimer.getPreviousTimerLengthSeconds(this)

    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        faculty_timer.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
    }

    override fun onResume() {
        super.onResume()
        initTimer()
        //TODO: remove background timer, hide notification
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
        faculty_record_drawer_layout.closeDrawer(GravityCompat.START)
       return true
        }

    fun record_end_lectureBtn_clicked(view: View){
        timer.cancel()
        onTimerFinished()
    }

    fun faculty_record_save_LrnObjBtn_clicked(view: View){

        val inputString = faculty_record_LrnObj.text.toString()
        if(!inputString.isNotEmpty()){

        } else {
        var newLearningObj = LearningObjectives("learningObj" , inputString)
        mDatabase?.reference?.child("Users/"+FacultyDataService.uid+"/LecturesCoursesWise/"+facultyRecordHelper.lectureCode+"/"+facultyRecordHelper.lecturePushID)?.setValue(newLearningObj)
        Toast.makeText(this, "Learning Objectived is saved.", Toast.LENGTH_LONG).show()
        faculty_record_LrnObj.text.clear()
        }
    }
}

