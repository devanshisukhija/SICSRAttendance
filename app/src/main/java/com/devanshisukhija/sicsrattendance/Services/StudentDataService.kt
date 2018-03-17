package com.devanshisukhija.sicsrattendance.Services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by devanshi on 06/03/18.
 */
object StudentDataService {

    var name = ""
    var uid = ""
    var prn= ""
    var program = ""
    var division = ""
    var role= ""
    var semester = ""
    var email= ""
    var batch = ""

    //[Firebase Reference]
    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    val mUserUId = mAuth.currentUser?.uid

    //[Start : of Store Student Data Function] --> func# 1
    fun storeStudentData (dataSnapshot: DataSnapshot?, complete:(Boolean) -> Unit) {
        println(dataSnapshot.toString())
        val ref = mDatabaseReference.child("Users").child(mUserUId)
        if(mUserUId != null) {//[Start : of Null Check]
            //[Start : of ValueEventListener Object] --> func# 2
                    FacultyDataService
                    name = dataSnapshot?.child("name")?.value.toString()
                    prn = dataSnapshot?.child("prn")?.value.toString()
                    email = dataSnapshot?.child("email")?.value.toString()
                    batch = dataSnapshot?.child("batch")?.value.toString()
                    program = dataSnapshot?.child("program")?.value.toString()
                    division = dataSnapshot?.child("division")?.value.toString()
                    semester = dataSnapshot?.child("semester")?.value.toString()
                    role = dataSnapshot?.child("role")?.value.toString()
                    uid= mUserUId
                    complete(true)
            }//[End : func# 2.]
        }//[End : of Null Check]
    }
