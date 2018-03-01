package com.devanshisukhija.sicsrattendance.Controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.devanshisukhija.sicsrattendance.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*



/**
 *  Comments template :-
 *  - //[START ]
 *  - //[End ]
 *
 */

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"

    // Firebase references for Authentication.
    private var mAuth: FirebaseAuth? = null
    private var mUser : FirebaseUser? = null
    private var mDatabase : FirebaseDatabase? = null
    private lateinit var mDatabaseReference : DatabaseReference

    //global variables
    private var emailString : String? = null
    private var passwordString : String? = null

    private lateinit var mValueEventListener : ValueEventListener
    private var currentUserAuthToken : String? = null


    // ActivityState : ONCREATE.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (!FirebaseApp.getApps(this@LoginActivity).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
        //Initializing firebase Auth and database Reference.
        mAuth = FirebaseAuth.getInstance()
        mDatabase = DatabaseHelper.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        // Getting the currently logged in user.
        mUser = mAuth?.currentUser
        //Login Spinners
        var loginSpinner = findViewById<ProgressBar>(R.id.loginSpinner)
        loginSpinner.visibility = View.INVISIBLE
    }

    // function for when the login button is clicked.
    // TODO : Login Activity : Function# 1.
     fun loginBtnClicked(view : View) {
         enableSpinner(true)
         emailString = loginEmailTxt.text.toString()
         passwordString = loginPasswordtxt.text.toString()

         if(!emailString.isNullOrEmpty() && !passwordString.isNullOrEmpty()) {
             // Checking if the login cridentials are correct. and then changing the Auth State to logged in.
             //TODO : Login Activity : Function# 3.
             mAuth!!.signInWithEmailAndPassword(emailString!!, passwordString!!).addOnCompleteListener(this) { task ->
                 if(task.isSuccessful) {
                     if(mUser != null) {
                         val currentUserAuthToken = mUser!!.uid
                         val ref = mDatabaseReference.child("Users").child("authTokenCheck")
                         // initializing "mValueEventListener"
                         mValueEventListener = object : ValueEventListener {

                             override fun onCancelled(mDatabaseError: DatabaseError?) {
                                 val err = mDatabaseError.toString()
                                 enableSpinner(false)
                                 Toast.makeText(this@LoginActivity, "Sever issue, Re-login please!", Toast.LENGTH_LONG).show()
                                 Log.d("VEL:Error : ", err)
                             }

                             override fun onDataChange(mDataSnapshot: DataSnapshot?) {

                                 mDataSnapshot?.children?.forEach {
                                     val role : String? = it.child("role").value.toString()
                                     if (it.key.toString() == currentUserAuthToken) {
                                         Log.d("VEL:success" , "passing role :"+ role + "to function for intent")
                                         if(role != null) {
                                             enableSpinner(false)
                                             intent_to_roleActivity(role)
                                         }
                                     } else {
                                         enableSpinner(false)
                                         Toast.makeText(this@LoginActivity, "Authorization error", Toast.LENGTH_LONG).show()
                                         //TODO : add code
                                     }
                                 }
                             }
                         }
                         ref.addValueEventListener(mValueEventListener)
                     }
                 } else {
                     enableSpinner(false)
                     Log.e(TAG, "signInWithEmail:failure", task.exception)
                     Toast.makeText(this@LoginActivity, "Authentication failed. Make sure email and password are correct",Toast.LENGTH_SHORT).show()
                 }
             } // [End of SignInwithEmailandPassword func]
         }else {
             Toast.makeText(this, "Email or Password can not be empty.", Toast.LENGTH_LONG).show()
             enableSpinner(false)
         } //[End of isOrnot email and password Empty condition]
    } //[End of Login Button clicked]

    // TODO : Login Activity : Function#2
     fun getHelpImgClicked(view : View) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.get_help_dialog, null)
        builder.setView(dialogView)
                .setNegativeButton("Close" ){ _, _ -> }.show()
        }

    //TODO : Login Activity : Function#3
    fun checkUsertype() {
    }

    fun intent_to_roleActivity( role : String){
            val role = role
            if (role == "student") {
                val role_intent = Intent(this, Student_homeActivity::class.java)
                finish()
                startActivity(role_intent)
            } else if (role == "faculty") {
                val role_intent = Intent(this, Faculty_HomeActivity::class.java)
                finish()
                startActivity(role_intent)
            } else {
                //TODO : dont know what to put here
            }
    }

    fun enableSpinner (enable  : Boolean) {
        var loginSpinner = findViewById<ProgressBar>(R.id.loginSpinner)

        if(enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }

        loginLoginBtn.isEnabled = true
    }



}


