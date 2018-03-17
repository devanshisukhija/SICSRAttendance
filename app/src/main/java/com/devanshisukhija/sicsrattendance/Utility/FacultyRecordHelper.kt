package com.devanshisukhija.sicsrattendance.Utility

import android.util.Log
import com.devanshisukhija.sicsrattendance.Services.UpdateScheduledLecturesService
import java.text.ParseException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This file is checking if there is any lecture assigned at the time of record button clicked, if there is not any lecture it
 * flags for dialog pop up. Also it has a function for returning time difference for the timer to work.
 */
 class FacultyRecordHelper {

    val TAG = "FacultyRecordHelper"
    var count : Int = 0
    var secondsRemaining: Long = 0
    var lectureID : String? = null
    var lectureName : String? = null
    var lectureVenue : String? = null
    var lectureCode : String? = null
    var lecturePushID : String? = null

 fun check_for_lecture(complete:(Boolean) -> Unit) : Int{

         for (x in UpdateScheduledLecturesService.lectures) {
             try {
                 val start_time = x.start_time.toLong()
                 val end_time = x.end_time.toLong()
                 val timeRightnow = (System.currentTimeMillis() / 1000)
            // CompareTo returns a negative number if it's less than other, or a positive number if it's greater than other.
             if (timeRightnow == start_time || timeRightnow > start_time  && timeRightnow < end_time) {
                 count++
                 lectureID = x.lectureID
                 lectureName = x.course_name
                 lectureVenue = x.class_room
                 lectureCode = x.course_code
                 lecturePushID = x.lecture_pushID
                 Log.d(TAG, "Test: lectureId assigned")
                 complete(true)
                 } else {
                     Log.d(TAG, "no lectures at this time")
                     complete(true)
                 }
             } catch (e: ParseException) {
                 Log.d(TAG, "Exception")
                 e.printStackTrace()
                 complete(false)
             }
         }

     return count
 }
    fun currentTime() : String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh:mm")
        val formatted = current.format(formatter).toString()
        return formatted
    }

    fun getDifference() : Long{
        for(x in UpdateScheduledLecturesService.lectures) {
            println(lectureID)
            if(!lectureID.isNullOrBlank() && x.lectureID == lectureID) {
                try {
                    val startTime = x.start_time.toLong()
                    val endTime = x.end_time.toLong()
                    val timeRightnow = System.currentTimeMillis() / 1000
                    val mills = endTime - timeRightnow
                    println(TAG + mills)
                    return mills
                    break;
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Log.d(TAG,"Lecture ID is either null or that id doesnt exist.")
            }
        }

        return 0
    }
}