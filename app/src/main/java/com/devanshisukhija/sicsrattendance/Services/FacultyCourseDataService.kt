package com.devanshisukhija.sicsrattendance.Services

import com.devanshisukhija.sicsrattendance.Model.FacultyCourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



/**
 * Created by devanshi on 13/03/18.
 */
object FacultyCourseDataService {

    val courses = ArrayList<FacultyCourse>()

    private const val TAG = "FacultyCourseDataService"

    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    val mUserUId = mAuth.currentUser?.uid
    private lateinit var mValueEventListener : ValueEventListener
    val ref = mDatabaseReference.child("Users").child(mUserUId).child("CoursesTaught")

    fun fetchcourseData(complete: (Boolean) -> Unit) {

        mValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?){
                for (snapshot in dataSnapshot!!.children) {
                    snapshot.children.forEach {
                        val course_code = it.child("course_code").value as String?
                        val course_name = it.child("course_name").value as String?
                        val course_program = it.child("course_program").value as String?
                        val course_semester = it.child("course_semester").value as String?

                        val newCourseSet = FacultyCourse(course_code , course_name, course_program, course_semester)
                        if(newCourseSet != null) {
                            courses.add(newCourseSet)
                        }
                    }
                 }
                complete(true)
            }
            override fun onCancelled(p0: DatabaseError?) {
                complete(false)
            }
        }

        ref.addValueEventListener(mValueEventListener)
    }

    
}