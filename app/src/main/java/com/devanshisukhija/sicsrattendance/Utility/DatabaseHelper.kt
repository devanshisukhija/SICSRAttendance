package com.devanshisukhija.sicsrattendance.Utility

import com.google.firebase.database.FirebaseDatabase

/**
 * Created by devanshi on 01/03/18.
 */

object DatabaseHelper {
    var isPersistenceEnable = false
    private var mDatabase: FirebaseDatabase? = FirebaseDatabase.getInstance()
    val instance: FirebaseDatabase
        get() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance()
                if (isPersistenceEnable == true) {
                    mDatabase!!.setPersistenceEnabled(true)
                }
            }

            return mDatabase!!
        }
}
