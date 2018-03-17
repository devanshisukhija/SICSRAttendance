package com.devanshisukhija.sicsrattendance.Services

import com.devanshisukhija.sicsrattendance.Model.StudentLecture
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by devanshi on 15/03/18.
 */
class StudentLectureDataService {

    val lectures = ArrayList<StudentLecture>()

    private val TAG = "StudentLectureDataService"

    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var mValueEventListener : ValueEventListener

    fun fetchlectures(code : String, complete: (Boolean) -> Unit) {
        lectures.clear()
        println(TAG + "  I am called")
        val ref = mDatabaseReference.child(StudentDataService.batch).child(StudentDataService.program).child(StudentDataService.semester).child(StudentDataService.division)
                .child("LecturesCourseWise").child(code)
        println(TAG + ref.toString())
        mValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?){
                for (snapshot in dataSnapshot!!.children) {
                    val lectureDate = snapshot.child("timestamp").value.toString()
                    val lectureLearningObj = snapshot.child("learningObj").value.toString()
                    val lectureID = snapshot.child("lectureId").value.toString()
                    println(TAG + "--- intent = "+ code + ",-- date "+ lectureDate )
                    val newLectureSet = StudentLecture(lectureDate, lectureLearningObj,lectureID)
                        lectures.add(newLectureSet)
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