package com.devanshisukhija.sicsrattendance.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot

/**
 * Created by devanshi on 01/03/18.
 */
object FacultyDataService {

    lateinit var name : String
    lateinit var uid : String
    lateinit var role: String
    lateinit var faculty_code : String
    lateinit var email: String

    private const val TAG = "FacultyDataService"

    val mAuth = FirebaseAuth.getInstance()
    val mUserUId = mAuth.currentUser?.uid

    //[Start : of Store User Data Function] --> func# 1
    fun storeFacultyData (dataSnapshot: DataSnapshot?,complete:(Boolean) -> Unit) {
        println(dataSnapshot.toString())
        if(mUserUId != null) {//[Start : of Null Check]
                    if(dataSnapshot !=null) {
                        name = dataSnapshot.child("name").value.toString()
                        faculty_code = dataSnapshot.child("Faculty-code").value.toString()
                        email = dataSnapshot.child("email").value.toString()
                        role = dataSnapshot.child("role").value.toString()
                        uid = mUserUId
                        complete(true)
                    } else {
                        Log.d(TAG , "dataSnapShot is empty.")
                    }
        }//[End : of Null Check]
        else{
            Log.d(TAG, "MUserUId is empty")
        }
    }//[End : func# 1.]

}