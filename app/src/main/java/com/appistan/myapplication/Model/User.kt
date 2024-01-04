package com.appistan.myapplication.Model

import com.appistan.myapplication.Utils.Func
import java.util.*


data class User(
    var id: String = "",
    var fName: String = "",
    var lName: String = "",
    var email: String = "",
    var password: String = "",
    var created: Date = Func.getCurrentDate(),
    var updated: Date = Func.getCurrentDate(),
    var status: Int = 0, // 0=profilenotsetup, 1=active, 2=block
)
