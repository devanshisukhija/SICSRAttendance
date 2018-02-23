
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import com.devanshisukhija.sicsrattendance.Model.FacultyScheduleLecture
import com.devanshisukhija.sicsrattendance.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/*
package com.devanshisukhija.sicsrattendance.Adapter

*
*/



class FacultyScheduleAdapter(val context : Context, val scheduled_lecture : ArrayList<FacultyScheduleLecture>) : RecyclerView.Adapter<FacultyScheduleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder{
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return scheduled_lecture.count()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindRows(context, scheduled_lecture[position])
    }


    inner class ViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView) {

        val tableRow = itemView?.findViewById<TableRow>(R.id.lecture_tablerow_layout)
        val program = itemView?.findViewById<TextView>(R.id.lecture_program_column)
        val course = itemView?.findViewById<TextView>(R.id.lecture_course_column)
        val lecture_time_start = itemView?.findViewById<TextView>(R.id.lecture_time_column)
        lateinit var lecture_time_end : String
        val venue = itemView?.findViewById<TextView>(R.id.lecture_room_column)


        fun bindRows(context: Context, lecture: FacultyScheduleLecture) {

            program?.text = lecture.program
            course?.text = lecture.course_name
//            lecture_time_start?.text = lecture.start_time
//            lecture_time_end = lecture.end_time
            venue?.text = lecture.class_room



           //TODO : formatting lecture model class.

        }

        fun returnTimeString(isoString : String) : String {

            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var converterDate = Date()

            try {
                converterDate = isoFormatter.parse(isoString)
            } catch (e : ParseException) {
                Log.d("PARSE" , "EXC:  can not parse date")
            }

            val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())
            return outDateString.format(converterDate)
        }
    }


}

