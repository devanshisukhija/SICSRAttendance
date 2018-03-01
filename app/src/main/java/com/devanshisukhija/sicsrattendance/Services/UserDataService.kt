package com.devanshisukhija.sicsrattendance.Services

import com.devanshisukhija.sicsrattendance.Model.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by devanshi on 01/03/18.
 */
object UserDataService {

    //[Array to Store User Data at login.]
    val userdata = ArrayList<UserData>()
    //[Firebase Reference]
    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var mValueEventListener : ValueEventListener

    //[Start : of Store User Data Function] --> func# 1
    fun storeUserData ( uid : String? ,complete:(Boolean) -> Unit) {
        val ref = mDatabaseReference.child("Users").child(uid)
        if(uid != null) {//[Start : of Null Check]
            //[Start : of ValueEventListener Object] --> func# 2
            mValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    dataSnapshot?.children?.forEach {
                        val name = it.child("name").value.toString()
                        val prn = it.child("prn").value.toString()
                        val email = it.child("email").value.toString()
                        val batch = it.child("batch").value.toString()
                        val program = it.child("program").value.toString()
                        val division = it.child("division").value.toString()
                        val semester = it.child("semester").value.toString()
                        val role = it.child("role").value.toString()

                        val newUser = UserData(name, prn, email, program , batch, division, semester , uid , role)
                        userdata.add(newUser)

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