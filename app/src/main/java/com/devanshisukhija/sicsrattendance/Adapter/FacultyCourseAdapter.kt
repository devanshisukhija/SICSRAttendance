package com.devanshisukhija.sicsrattendance.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.FacultyCourse
import com.devanshisukhija.sicsrattendance.R
import java.util.*

/**
 * Created by devanshi on 13/03/18.
 */
class FacultyCourseAdapter(val context : Context, val courses : ArrayList<FacultyCourse>) : RecyclerView.Adapter<FacultyCourseAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.faculty_course_layout, parent , false)
        return Holder(view)
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
    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        //[Variable initialization]
        val course_name = itemView?.findViewById<TextView>(R.id.faculty_course_name)
        val course_program = itemView?.findViewById<TextView>(R.id.faculty_course_program)
        val course_semester = itemView?.findViewById<TextView>(R.id.faculty_course_sem)

        //[Start : bindRows] --> func# 5
        fun bindRows(context: Context, courses: FacultyCourse) {
            course_name?.text = courses.course_name
            course_program?.text = courses.course_program
            course_semester?.text = courses.course_semester
        }//[End : func# 5]
    }//[End : inner class func# 4]
}