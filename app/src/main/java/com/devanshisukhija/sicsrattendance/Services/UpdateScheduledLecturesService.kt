package com.devanshisukhija.sicsrattendance.Services

import com.devanshisukhija.sicsrattendance.Model.ScheduledLectures
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by devanshi on 01/03/18.
 */

object UpdateScheduledLecturesService {


    private val mDatabaseReference = FirebaseDatabase.getInstance().reference

    val lectures = ArrayList<ScheduledLectures>()

    fun getLectures ( complete:(Boolean) -> Unit) {

        val ref = mDatabaseReference.child("Users")
    }

}