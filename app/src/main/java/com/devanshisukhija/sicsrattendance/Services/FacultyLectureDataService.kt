package com.devanshisukhija.sicsrattendance.Services

import android.util.Log
import com.devanshisukhija.sicsrattendance.Model.FacultyLecture
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Fetches lecture information from particular faculty node.
 * Data array is created and used in the adapter FacultyLectureAdapter
 */

object FacultyLectureDataService {

    val lectures = ArrayList<FacultyLecture>()

    private const val TAG = "FacultyLectureDataService"

    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    val mUserUId = mAuth.currentUser?.uid
    private lateinit var mValueEventListener : ValueEventListener

    fun fetchlectures(code : String, complete: (Boolean) -> Unit) {
        lectures.clear()
        println(TAG + "  I am called")
        val ref = mDatabaseReference.child("Users").child(FacultyDataService.uid).child("LecturesCourseWise").child(code)
        mValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?){
                for (snapshot in dataSnapshot!!.children) {
                    val lectureDate = snapshot.child("timestamp").value.toString()
                    val lectureDivision = snapshot.child("division").value.toString()
                    val lectureLearningObj = snapshot.child("learningObj").value.toString()
                    val lectureID = snapshot.child("lectureId").value.toString()

                    Log.d(TAG , lectureDate)

                    println(TAG + "--- intent = "+ code + ",-- date "+ lectureDate )
                    val newLectureSet = FacultyLecture(lectureDate , lectureDivision, lectureLearningObj,lectureID)
                    if(newLectureSet != null) {
                        lectures.add(newLectureSet)
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