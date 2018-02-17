package com.devanshisukhija.sicsrattendance.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.FacultyScheduleLecture
import com.devanshisukhija.sicsrattendance.R

/**
 * Created by devanshi on 17/02/18.
 */
class FacultyScheduleAdapter(val context : Context, val scheduled_lecture : ArrayList<FacultyScheduleLecture>) : RecyclerView.Adapter<FacultyScheduleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return scheduled_lecture.count()
    }

    override fun onBindViewHolder(holder: ?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

        val tableRow = itemView?.findViewById<TableRow>(R.id.lecture_tablerow_layout)
        val program = itemView?.findViewById<TextView>(R.id.lecture_program_column)
        val course = itemView?.findViewById<TextView>(R.id.lecture_course_column)
        val lecture_time = itemView?.findViewById<TextView>(R.id.lecture_time_column)


        fun bindRows( context: Context , lecture: FacultyScheduleLecture) {

            program?.text = lecture.program;
            course?.text = lecture.course;
            lecture

            //TODO : formatting lecture model class.

        }

    }


}