package com.devanshisukhija.sicsrattendance.Services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by devanshi on 01/03/18.
 */
object UserDataService {

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
    private lateinit var mValueEventListener : ValueEventListener

    //[Start : of Store User Data Function] --> func# 1
    fun storeUserData ( authToken : String? ,complete:(Boolean) -> Unit) {
        val ref = mDatabaseReference.child("Users").child(uid)
        if(authToken != null) {//[Start : of Null Check]
            //[Start : of ValueEventListener Object] --> func# 2
            mValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    dataSnapshot?.children?.forEach {
                        UserDataService
                        name = it.child("name").value.toString()
                        prn = it.child("prn").value.toString()
                        email = it.child("email").value.toString()
                        batch = it.child("batch").value.toString()
                        program = it.child("program").value.toString()
                        division = it.child("division").value.toString()
                        semester = it.child("semester").value.toString()
                        role = it.child("role").value.toString()
                        uid= authToken
                        complete(true)
                    }//[End : forEach.]
                }//[End : onDataChanged]

                override fun onCancelled(p0: DatabaseError?) {
                   complete(false)
                }//[End : ^]
            }//[End : func# 2.]
        }//[End : of Null Check]
        ref.addValueEventListener(mValueEventListener)
    }
}