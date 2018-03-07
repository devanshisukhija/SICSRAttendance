package com.devanshisukhija.sicsrattendance.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by devanshi on 06/03/18.
 */
object UserRoleService {

    const val TAG = "UserRoleService"
    var role : String = ""
    var name : String = ""

    //[Firebase Reference]
    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var mValueEventListener : ValueEventListener



    //[Start : of Store User Data Function] --> func# 1
    fun getRole (complete:(Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser?.uid
        val ref = mDatabaseReference.child("Users").child(user)
        println(ref)
        if( user != null) {//[Start : of Null Check]
            //[Start : of ValueEventListener Object] --> func# 2
            mValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        role = dataSnapshot?.child("role")?.value.toString()
                        when(role){
                        "faculty" -> {
                            FacultyDataService.storeFacultyData(dataSnapshot) {
                                when(it){
                                    true -> complete(true)
                                    false -> Log.d(TAG, "FacultyDataService.storeFacultyData() failed")
                                }
                            }
                        }
                        "student" -> {
                            StudentDataService.storeStudentData(dataSnapshot) {
                                when(it){
                                    true -> complete(true)
                                    false -> Log.d(TAG, "StudentDataService.storeStudentData() failed")
                                }
                            }
                        }
                        else -> {
                            Log.d(TAG,"Role is neither student nor faculty.")
                        }
                        }
                }//[End : onDataChanged]
                override fun onCancelled(p0: DatabaseError?) {
                    Log.d(TAG, "VEL database error")
                    complete(false)
                }//[End : ^]
            }//[End : func# 2.]
        }//[End : of Null Check]
        else{
            Log.d(TAG, "authToken is null")
        }
        ref.addValueEventListener(mValueEventListener)
    }
}