package com.devanshisukhija.sicsrattendance.Utility

import android.util.Log
import com.devanshisukhija.sicsrattendance.Services.UpdateScheduledLecturesService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by devanshi on 08/03/18.
 */
 class FacultyRecordHelper {

    val TAG = "FacultyRecordHelper"
    var count : Int = 0
    var time_difference : Int = 0

 fun check_for_lecture(complete:(Boolean) -> Unit) : Int{
    if(UpdateScheduledLecturesService.lectures != null) {
        for(x in UpdateScheduledLecturesService.lectures) {
            try {
                val start_time = x.start_time
                val starttime_date = SimpleDateFormat("hh:mm").parse(start_time)
                val starttime_calendar = Calendar.getInstance()
                starttime_calendar.setTime(starttime_date)

                val end_time = x.end_time
                val endtime_date = SimpleDateFormat("hh:mm").parse(end_time)
                val endtime_calendar = Calendar.getInstance()
                endtime_calendar.setTime(endtime_date)
                endtime_calendar.add(Calendar.DATE, 1)

                val time_rightnow = currentTime()
                val currenttime_date = SimpleDateFormat("hh:mm").parse(time_rightnow)
                val currenttime_calendar = Calendar.getInstance()
                currenttime_calendar.setTime(currenttime_date)
                currenttime_calendar.add(Calendar.DATE, 1)

                val get_time_rightnow = currenttime_calendar.time
                if (get_time_rightnow.after(starttime_calendar.time) && get_time_rightnow.before(endtime_calendar.time)) {
                    count++
                    complete(true)
                }
                else {
                    complete(false)
                }
            } catch (e: ParseException) {
                Log.d(TAG , "Exception")
                e.printStackTrace()
                complete(false)
            }
        }
    } else {
        Log.d(TAG, "lecture is null")
    }
     return count
 }
    fun currentTime() : String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("hh:mm")
        val formatted = current.format(formatter).toString()
        return formatted
    }

    fun get_difference(endtime_date : Date , currenttime_date : Date) : String {
        var diff: String
        try {
            val mills = endtime_date.time.minus(currenttime_date.time )
                        val hours = mills / (1000 * 60 * 60)
                        val mins = (mills / (1000 * 60) % 60).toInt()
                        diff = "" +hours+ ":"+mins // updated value every1 second
            println("LOLOLL :::: "+currenttime_date.time +"---"+ endtime_date.time +"---" + mills)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "ty"
    }
}