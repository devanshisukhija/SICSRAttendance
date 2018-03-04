
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.ScheduledLectures
import com.devanshisukhija.sicsrattendance.R
import java.util.*

/*
package com.devanshisukhija.sicsrattendance.Adapter

*
*/

class FacultyScheduleAdapter(val context : Context, val scheduled_lecture : ArrayList<ScheduledLectures>) : RecyclerView.Adapter<FacultyScheduleAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.faculty_schedule_layout, parent , false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return scheduled_lecture.count()
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {
        holder?.bindRows(context, scheduled_lecture[position])
    }


    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

        val tableRow = itemView?.findViewById<TableRow>(R.id.lecture_tablerow_layout)
        val program = itemView?.findViewById<TextView>(R.id.lecture_program_column)
        val course = itemView?.findViewById<TextView>(R.id.lecture_course_column)
        val lecture_time_start = itemView?.findViewById<TextView>(R.id.lecture_time_column)
        val venue = itemView?.findViewById<TextView>(R.id.lecture_room_column)


        fun bindRows(context: Context, lecture: ScheduledLectures) {

            program?.text = lecture.program
            course?.text = lecture.course_name
            lecture_time_start?.text = lecture.start_time
            venue?.text = lecture.class_room

        }
    }
}

