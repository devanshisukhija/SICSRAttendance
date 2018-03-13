package com.devanshisukhija.sicsrattendance.Controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.devanshisukhija.sicsrattendance.Controller.FacultyWrap.Faculty_HomeActivity
import com.devanshisukhija.sicsrattendance.Controller.StudentWrap.Student_homeActivity
import com.devanshisukhija.sicsrattendance.R
import com.devanshisukhija.sicsrattendance.Services.FacultyDataService
import com.devanshisukhija.sicsrattendance.Services.UserRoleService
import com.devanshisukhija.sicsrattendance.Utility.DatabaseHelper
import com.devanshisukhija.sicsrattendance.Utility.LOGIN_TO_FACULTY_INTENT_USER_EMAIL
import com.devanshisukhija.sicsrattendance.Utility.LOGIN_TO_FACULTY_INTENT_USER_NAME
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*


/**
 *  Comments template :-
 *  - //[START ]
 *  - //[End ]
 *
 */

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"
    //[Firebase initialing]
    private var mAuth: FirebaseAuth? = null
    private var mUser : FirebaseUser? = null
    private var mDatabase : FirebaseDatabase? = null
    private lateinit var mDatabaseReference : DatabaseReference
    //[Instance of DatabaseHelper object.]
    private var databaseHelper : DatabaseHelper = DatabaseHelper
    //[Instance of FacultyDataService class.]
    private val faculty_data_service: FacultyDataService = FacultyDataService
    //[Global variables]
    private var emailString : String? = null
    private var passwordString : String? = null
    //[ValueEventListen]
    private lateinit var mValueEventListener : ValueEventListener


    // [Start : of onCreate]  --> func#1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (!FirebaseApp.getApps(this@LoginActivity).isEmpty()) {
            databaseHelper.isPersistenceEnable = true
            databaseHelper.instance
        }
        //[Declaration]
        mAuth = FirebaseAuth.getInstance()
        mDatabase = DatabaseHelper.instance
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        //[Returns the currently logged in user.]
        mUser = mAuth?.currentUser

        //[Login Spinners]
        val loginSpinner = findViewById<ProgressBar>(R.id.loginSpinner)
        loginSpinner.visibility = View.INVISIBLE
        //[Clearing lectures to display]
        //UpdateScheduledLecturesService.lectures.clear()
    }//[End : of OnCreate]


    //[Start : Function for when the Login Btn is click.]  --> func# 2
      fun loginBtnClicked(view : View) {
        //[Unique ID of currently logged in user.]
        val currentUserAuthToken = mUser?.uid
         enableSpinner(true)
         //[Variable Declaration]
         emailString = loginEmailTxt.text.toString()
         passwordString = loginPasswordtxt.text.toString()

         //[Condition for Null or Empty email and password.]
         if(!emailString.isNullOrEmpty() && !passwordString.isNullOrEmpty()) {
             // [Start : signInWithEmailAndPassword] --> func# 3
             mAuth!!.signInWithEmailAndPassword(emailString!!, passwordString!!).addOnCompleteListener(this) { task ->
                 if(task.isSuccessful) {
                     if(mUser != null && currentUserAuthToken !=null) { //[Null Check for current user and it uid.]
                         //[Call to UserRoleService to determine the role of the user. --> trigger# 1.]
                         UserRoleService.getRole() { complete ->
                             when (complete) {
                                 true -> {Log.d("DONE : ","complete")
                                    when(UserRoleService.role) {
                                        "faculty" -> {
                                            val role_intent = Intent(this@LoginActivity, Faculty_HomeActivity::class.java)
                                            role_intent.putExtra(LOGIN_TO_FACULTY_INTENT_USER_EMAIL, FacultyDataService.email)
                                            role_intent.putExtra(LOGIN_TO_FACULTY_INTENT_USER_NAME, FacultyDataService.name)

                                            finish()
                                            startActivity(role_intent)
                                        }
                                        "student" -> {
                                            val role_intent = Intent(this, Student_homeActivity::class.java)
                                            finish()
                                            startActivity(role_intent)
                                        }
                                    }
                                 }
                                 false -> {
                                     enableSpinner(false)
                                     Log.d(TAG, "Failed UserRoleService.getrole()")
                                     Toast.makeText(this@LoginActivity, "Could not recognize email, Try again!", Toast.LENGTH_LONG).show()
                                 }//[End : false]
                             }//[End : when(complete)]
                         }//[End : of trigger# 1]
                     }//[End : of inner Null check.]
                     else{
                         Log.d(TAG, "either mUser or currentAuthToken is null.")
                     }
                 } else {
                     enableSpinner(false)
                     Log.e(TAG, "signInWithEmail:failure", task.exception)
                     Toast.makeText(this@LoginActivity, "Authentication failed. Make sure email and password are correct",Toast.LENGTH_LONG).show()
                 }
             } //[End of signInWithEmailAndPassword func]
         }else { //[End > Start : Email and Password Null check condition.]
             Toast.makeText(this, "Email or Password can not be empty.", Toast.LENGTH_LONG).show()
             Log.d(TAG, "Password or email can not be empty")
             enableSpinner(false)
         }
    } //[End : of Login Button clicked]

    //[Start : of function for AlertDialog Box inflation at 'getHellp' image.] --> func# 4.
     fun getHelpImgClicked(view : View) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.get_help_dialog, null)
        builder.setView(dialogView)
                .setNegativeButton("Close") { _, _ -> }.show()
    }//[End : func# 4.]

    //[Start : of Enable Spinner Function] --> func# 6
    fun enableSpinner (enable  : Boolean) {
        val loginSpinner = findViewById<ProgressBar>(R.id.loginSpinner)
        if(enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        //[Enabling Spinner on Login Btn.]
        loginLoginBtn.isEnabled = true
    }//[End : func# 6]
}


