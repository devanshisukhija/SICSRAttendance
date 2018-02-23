package com.devanshisukhija.sicsrattendance.Model

import android.icu.text.DateFormat

/**
 * Created by devanshi on 18/02/18.
 */
class FacultyScheduleLecture( start_time : DateFormat?,  end_time : DateFormat?,  course_code : String?, course_name : String?,  class_room : String?,  timestamp : DateFormat?,  program : String?,  batch : String?,  semester : String?,  faculty_code : String?,  division : String?, lectureID : String?){


    var course_code = course_code
    get() {
        return course_code
    }
    var start_time = start_time
    var end_time=end_time
    var course_name =course_name
    var class_room = class_room
    var timestamp = timestamp
    var program = program
    var batch = batch
    var semester =semester
    var faculty_code =faculty_code
    var division = division
    var lectureID = lectureID


}