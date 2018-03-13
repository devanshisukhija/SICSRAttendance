package com.devanshisukhija.sicsrattendance.Services

import android.util.Log
import com.devanshisukhija.sicsrattendance.Model.ScheduledLectures
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This Serivce singleton is for fetching the daily lecture information and storing it in a lecture array.
 */

object UpdateScheduledLecturesService {
    //[Firebase Database Reference]
    private val mDatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var mValueEventListener : ValueEventListener
    //[Array of lectures fetch from firebase]
    val lectures = ArrayList<ScheduledLectures>()
    //[Start : of function for fetching Lecture data.] -> func# 1
    fun getLectures(complete: (Boolean) -> Unit) {
        //[If..Else : for checking UserData->role]
        if (UserRoleService.role == "student") {
            //TODO : Student redirect.
        } else if (UserRoleService.role == "faculty") {
            val ref = mDatabaseReference.child("Users").child(FacultyDataService.uid).child("Lectures").child(getTime())
            //[Start :  of function for listening values from database.] -> func# 2
            mValueEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {
                    Log.e("Error", p0.toString())
                    complete(false)
                }//[End : OnCancelled.]

                override fun onDataChange(dataSnapshot : DataSnapshot?) {
                    lectures.clear()
                    if(dataSnapshot?.children != null){
                           dataSnapshot.children.forEach{ it ->
                               val start_time = it.child("start_time").value.toString()
                               val end_time = it.child("end_time").value.toString()
                               val batch = it.child("batch_name").value.toString()
                               val course = it.child("course_name").value.toString()
                               val course_code = it.child("course_code").value.toString()
                               val semester = it.child("sem").value.toString()
                               val faculty = it.child("teacher_name").value.toString()
                               val division = it.child("division").value.toString()
                               val room_name = it.child("room_number").value.toString()
                               val timestamp = it.child("timestamp").value.toString()
                               val program = it.child("program_name").value.toString()
                               val lecture_id = it.child("lectureId").value.toString()
                               val modified_start_time = it.child("modified_start_time").value.toString()
                               val modified_end_time = it.child("modified_end_time").value.toString()

                               //[New ScheduledLectures obj.]
                               val new_lecture = ScheduledLectures(modified_start_time,modified_end_time,start_time , end_time, course_code, course, room_name, timestamp, program, batch,semester, faculty, division, lecture_id)
                               if(new_lecture != null) {
                                   lectures.add(new_lecture)
                               }
                               complete(true)
                        }//[End : of iteration.]
                     }//[End : Null Check.]
                }//[End : onDataChange.]
            }//[End : ValueEventListener.]
            ref.addListenerForSingleValueEvent(mValueEventListener)
        }//[End : If..Else, role = faculty.]
    }//[End : getLectures.]

    //[Start : of function that returns current DateTime, pattern: 'Monday, July 03'.] -> func#2
    fun getTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, LLLL dd")
        val formatted = current.format(formatter).toString()

        return formatted
    }//[End : func# 2]
}