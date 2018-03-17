package com.devanshisukhija.sicsrattendance.Services

import com.devanshisukhija.sicsrattendance.Model.FacultyCourse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


object FacultyCourseDataService {

    val courses = ArrayList<FacultyCourse>()

    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth = FirebaseAuth.getInstance()
    private val mUserUId = mAuth.currentUser?.uid
    private lateinit var mValueEventListener : ValueEventListener


    fun fetchCourseData(complete: (Boolean) -> Unit) {
        val ref = mDatabaseReference.child("Users").child(mUserUId).child("CoursesTaught")
    courses.clear()
        mValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?){
                for (snapshot in dataSnapshot!!.children) {
                    val courseCode = snapshot.child("course_code").value.toString()
                    val courseName = snapshot.child("course_name").value.toString()
                    val courseProgram = snapshot.child("course_program").value.toString()
                    val courseSemester = snapshot.child("course_semester").value.toString()

                    val newCourseSet = FacultyCourse(courseCode , courseName, courseProgram, courseSemester)
                    courses.add(newCourseSet)
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