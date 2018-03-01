package com.devanshisukhija.sicsrattendance.Model

import android.icu.text.DateFormat

/**
 * Created by devanshi on 01/03/18.
 */
class ScheduledLectures(val start_time : DateFormat, val end_time : DateFormat, val course_code : String?, val course_name : String?, val class_room : String?, val timestamp : DateFormat?, val program : String?, val batch : String?, val semester : String, val faculty_code : String, val division : String, val lectureID : String, val role : String) {

}
