package com.devanshisukhija.sicsrattendance.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.FacultyLecture
import com.devanshisukhija.sicsrattendance.R
import java.util.*

/**
 * Created by devanshi on 15/03/18.
 */
class FacultyLectureAdapter (val context : Context, val lectures : ArrayList<FacultyLecture>) : RecyclerView.Adapter<FacultyLectureAdapter.Holder>()  {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.faculty_lecture_layout, parent , false)
        return Holder(view)
    }//[End : func# 1]

    //[Start]  --> func#2
    override fun getItemCount(): Int {
        return lectures.count()
    }//[End : func# 2]

    //[Start : onBindViewHolder]  --> func# 3
    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindRows(context, lectures[position])
    }//[End : func# 3]

    //[Start : inner Holder class] --> func# 4
    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        //[Variable initialization]
        val lecture_learningObj = itemView?.findViewById<TextView>(R.id.faculty_lecture_learningObj)
        val lecture_div = itemView?.findViewById<TextView>(R.id.faculty_lecture_div)
        val lecture_date = itemView?.findViewById<TextView>(R.id.faculty_lecture_date)
        val lecture_id = itemView?.findViewById<TextView>(R.id.faculty_lecture_ID)

        //[Start : bindRows] --> func# 5
        fun bindRows(context: Context, lectures: FacultyLecture) {
            lecture_date?.text = lectures.date
            lecture_div?.text = lectures.div
            lecture_id?.text = lectures.lectureID
            lecture_learningObj?.text = lectures.learningObj
        }//[End : func# 5]
    }//[End : inner class func# 4]
}