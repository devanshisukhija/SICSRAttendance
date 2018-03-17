package com.devanshisukhija.sicsrattendance.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.StudentCourse
import com.devanshisukhija.sicsrattendance.R
import java.util.*

/**
 * This Adapter has item clicked listener attached to it. It will be used in Student_LectureActivity.
 */
class StudentCourseAdapter(val context : Context, val courses : ArrayList<StudentCourse>, val itemClicked : (StudentCourse) -> Unit) : RecyclerView.Adapter<StudentCourseAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.student_course_layout, parent , false)
        return Holder(view, itemClicked)
    }//[End : func# 1]

    //[Start]  --> func#2
    override fun getItemCount(): Int {
        return courses.count()
    }//[End : func# 2]

    //[Start : onBindViewHolder]  --> func# 3
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindRows(context, courses[position])
    }//[End : func# 3]

    //[Start : inner Holder class] --> func# 4
    inner class Holder(itemView : View?, val itemClicked: (StudentCourse) -> Unit) : RecyclerView.ViewHolder(itemView) {
        //[Variable initialization]
        val courseName = itemView?.findViewById<TextView>(R.id.student_course_name)
        //[Start : bindRows] --> func# 5
        fun bindRows(context: Context, courses: StudentCourse) {
            courseName?.text = courses.course_name

            itemView.setOnClickListener {itemClicked(courses)}
        }//[End : func# 5]
    }//[End : inner class func# 4]
}