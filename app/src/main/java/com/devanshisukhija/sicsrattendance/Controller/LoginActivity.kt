package com.devanshisukhija.sicsrattendance.Controller

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
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
    //private var mDatabaseReference: DatabaseReference? = null
    //private var mDatabase: FirebaseDatabase? = null

    // Firebase refferences for Authentication.
    private var mAuth: FirebaseAuth? = null
    private var mUser : FirebaseUser? = null
    private var mDatabase : FirebaseDatabase? = null
    private lateinit var mDatabaseReference : DatabaseReference
   // private var mAuthListener : FirebaseAuth.AuthStateListener? = null

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

        // initializing firebase Auth and database Reference.
        mAuth = FirebaseAuth.getInstance()
        mDatabase = DatabaseHelper.getInstance()
        mDatabaseReference = FirebaseDatabase.getInstance().reference
        // getting the currently logined user.
        mUser = mAuth?.currentUser



    }
    //ActivityState : ONSTART.
    override fun onStart() {
        super.onStart()

        //[START auth_state_listener]
       // mAuth?.addAuthStateListener(mAuthListener!!)
    }
    //ActivityState : ONPAUSE.
    override fun onPause() {
        super.onPause()


    }

    // function for when the login button is clicked.
    // TODO : Login Activity : Function# 1.
     fun loginBtnClicked(view : View) {

         emailString = loginEmailTxt.text.toString()
         passwordString = loginPasswordtxt.text.toString()

         if(!emailString.isNullOrEmpty() && !passwordString.isNullOrEmpty()) {

             // Checking if the login cridentials are correct. and then changing the Auth State to logged in.
             //TODO : Login Activity : Function# 3.
             mAuth!!.signInWithEmailAndPassword(emailString!!, passwordString!!).addOnCompleteListener(this) { task ->

                 if(task.isSuccessful) {
                     if(mUser != null) {
                         val currentUserAuthToken = mUser!!.uid
                         // function call for checking the user.
                         val ref = mDatabaseReference.child("Users").child("authTokenCheck")

                         //Here below, trying retrive a key by its value.
                         mValueEventListener = object : ValueEventListener {
                             override fun onCancelled(mDatabaseError: DatabaseError?) {
                                 val err = mDatabaseError.toString()
                                 Log.d("NONONONONONO", err)

                             }

                             override fun onDataChange(mDataSnapshot: DataSnapshot?) {

                                 mDataSnapshot?.children?.forEach {
                                     if (it.child(currentUserAuthToken).exists()) {
                                         val role = it.child(currentUserAuthToken).child("role").value.toString()
                                         Log.d("YOYOYOYOOYOYOY", role)
                                     } else {
                                         Log.d("NONONOO", "try again : " + it.child(currentUserAuthToken).child("role").value)
                                     }
                                 }
                             }

                         }
                         ref.addValueEventListener(mValueEventListener)


                     }
                 } else {

                     Log.e(TAG, "signInWithEmail:failure", task.exception)
                     Toast.makeText(this@LoginActivity, "Authentication failed. Make sure email and password are correct",Toast.LENGTH_SHORT).show()
                 }
             } // <-----------------End of SignInwithEmailandPassword func.-------------------------->

         }else {
             Toast.makeText(this, "Email or Password can not be empty.", Toast.LENGTH_LONG).show()
         } // <----------------End of isOrnot email and password Empty condition.------------------------->
    } //<----------------------End of Login Button clicked.------------>


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




}


