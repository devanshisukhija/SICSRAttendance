
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.ScheduledLectures
import com.devanshisukhija.sicsrattendance.R
import java.util.*

/*
package com.devanshisukhija.sicsrattendance.Adapter

*
*/

class FacultyScheduleAdapter(val context : Context, val scheduled_lecture : ArrayList<ScheduledLectures>) : RecyclerView.Adapter<FacultyScheduleAdapter.Holder>() {

    //[Start : onCreatViewHolder]  --> func#1
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.faculty_schedule_layout, parent , false)
        return Holder(view)
    }//[End : func# 1]

    //[Start]  --> func#2
    override fun getItemCount(): Int {
        return scheduled_lecture.count()
    }//[End : func# 2]

    //[Start : onBindViewHolder]  --> func# 3
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindRows(context, scheduled_lecture[position])
    }//[End : func# 3]

    //[Start : inner Holder class] --> func# 4
    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        //[Variable initialization]
        val program = itemView?.findViewById<TextView>(R.id.lecture_program_column)
        val course = itemView?.findViewById<TextView>(R.id.lecture_course_column)
        val lecture_time = itemView?.findViewById<TextView>(R.id.lecture_time_column)
        val venue = itemView?.findViewById<TextView>(R.id.lecture_room_column)
        val semester = itemView?.findViewById<TextView>(R.id.lecture_semester)
        //[Start : bindRows] --> func# 5
        fun bindRows(context: Context, lecture: ScheduledLectures) {

            program?.text = lecture.program
            course?.text = lecture.course_name
            lecture_time?.text = lecture.modified_start_time + "- \n" +lecture.modified_end_time
            venue?.text = lecture.class_room
            semester?.text = lecture.semester

        }//[End : func# 5]
    }//[End : inner class func# 4]
}//[End : class]

