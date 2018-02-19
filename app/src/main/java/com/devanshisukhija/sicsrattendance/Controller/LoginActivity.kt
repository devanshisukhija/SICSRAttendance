package com.devanshisukhija.sicsrattendance.Controller

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devanshisukhija.sicsrattendance.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val TAG = "LoginActivity"
    //private var mDatabaseReference: DatabaseReference? = null
    //private var mDatabase: FirebaseDatabase? = null

    // Firebase refferences for Authentication.
    private var mAuth: FirebaseAuth? = null
    private var user : FirebaseUser? = null
    private var mDatabase : DatabaseReference? = null

    //global variables
    private var emailString : String? = null
    private var passwordString : String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initializing firebase Auth and database Reference.
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
    }

    // function for when the login button is clicked.
    // TODO : add doc
     fun loginBtnClicked(view : View) {

         emailString = loginEmailTxt.text.toString()
         passwordString = loginPasswordtxt.text.toString()

         if(!emailString.isNullOrEmpty() && !passwordString.isNullOrEmpty()) {

             mAuth!!.signInWithEmailAndPassword(emailString!!, passwordString!!).addOnCompleteListener(this) { task ->

                 if(task.isSuccessful) {
                     // TODO : Remove

                     var token = user?.getIdToken(true)
                     Log.d(TAG, "signInWithEmail:success :" + token)

                 } else {
                     //TODO : Remove
                     Log.e(TAG, "signInWithEmail:failure", task.exception)
                     Toast.makeText(this@LoginActivity, "Authentication failed. Make sure email and password are correct",
                             Toast.LENGTH_SHORT).show()
                 }
             }

         } else {
             Toast.makeText(this, "Email or Password can not be empty.", Toast.LENGTH_LONG).show()
         }
    }

    // TODO : add doc
     fun getHelpImgClicked(view : View) {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.get_help_dialog, null)
        builder.setView(dialogView)
                .setNegativeButton("Close" ){ _, _ -> }.show()

        }


     fun UIUpdate(databaseRef : DatabaseReference) {

     }
}


