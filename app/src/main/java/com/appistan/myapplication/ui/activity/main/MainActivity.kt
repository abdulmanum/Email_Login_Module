package com.appistan.myapplication.ui.activity.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.appistan.myapplication.Utils.SharedPref
import com.appistan.myapplication.databinding.ActivityMainBinding
import com.appistan.myapplication.ui.activity.startscreens.LoginActivity

class MainActivity : AppCompatActivity() {

    var context = this
    lateinit var bin: ActivityMainBinding

    lateinit var sp: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bin.root)

        sp = SharedPref(context)

        bin.btnLogout.text = "${sp.getProfile()!!.fName} ${sp.getProfile()!!.lName}\nLogout"

        bin.btnLogout.setOnClickListener {
            sp.clear()
            Toast.makeText(context, "Logout!", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    context,
                    LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

    }



}