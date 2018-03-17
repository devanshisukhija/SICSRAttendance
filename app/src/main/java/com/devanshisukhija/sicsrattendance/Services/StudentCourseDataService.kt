package com.devanshisukhija.sicsrattendance.Services

import com.devanshisukhija.sicsrattendance.Model.StudentCourse
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Fetches data from firebase database from node Batch->Program->Semester->Courses.
 * It is getting used for StudentCourseAdapter
 */
object StudentCourseDataService {

    val courses = ArrayList<StudentCourse>()

    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var mValueEventListener : ValueEventListener
    val ref = mDatabaseReference.child(StudentDataService.batch).child(StudentDataService.program).child(StudentDataService.semester).child("Courses")

    fun fetchCourseData(complete: (Boolean) -> Unit) {
        courses.clear()
        mValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?){
                for (snapshot in dataSnapshot!!.children) {
                    val courseName = snapshot.value.toString()
                    val courseCode = snapshot.key.toString()
                    val newCourseSet = StudentCourse(courseName, courseCode)
                    println("STUDENT course service, : "+ courseCode + " and " + courseCode)
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
