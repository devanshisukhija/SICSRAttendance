package com.devanshisukhija.sicsrattendance.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.StudentScheduledLectures
import com.devanshisukhija.sicsrattendance.R
import java.util.*

/**
 * Created by devanshi on 15/03/18.
 */
class StudentScheduleAdapter(val context : Context, val student_scheduled_lecture : ArrayList<StudentScheduledLectures>) : RecyclerView.Adapter<StudentScheduleAdapter.Holder>(){
    //[Start : onCreatViewHolder]  --> func#1
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.student_schedule_layout, parent , false)
        return Holder(view)
    }//[End : func# 1]

    //[Start]  --> func#2
    override fun getItemCount(): Int {
        return student_scheduled_lecture.count()
    }//[End : func# 2]

    //[Start : onBindViewHolder]  --> func# 3
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindRows(context, student_scheduled_lecture[position])
    }//[End : func# 3]

    //[Start : inner Holder class] --> func# 4
    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        //[Variable initialization]
        val course = itemView?.findViewById<TextView>(R.id.student_lecture_course_column)
        val lecture_time = itemView?.findViewById<TextView>(R.id.student_lecture_time_column)
        val venue = itemView?.findViewById<TextView>(R.id.student_lecture_room_column)
        //[Start : bindRows] --> func# 5
        fun bindRows(context: Context, student_scheduled_lecture : StudentScheduledLectures) {
            course?.text = student_scheduled_lecture.course_name
            lecture_time?.text = student_scheduled_lecture.start_time + "- \n" +student_scheduled_lecture.end_time
            venue?.text = student_scheduled_lecture.class_room

        }//[End : func# 5]
    }//[End : inner class func# 4]
}//[End : class]