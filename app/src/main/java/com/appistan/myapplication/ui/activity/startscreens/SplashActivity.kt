package com.appistan.myapplication.ui.activity.startscreens

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.appistan.myapplication.Model.User
import com.appistan.myapplication.Utils.FireRef
import com.appistan.myapplication.Utils.SharedPref
import com.appistan.myapplication.databinding.ActivitySplashBinding
import com.appistan.myapplication.ui.activity.main.MainActivity

class SplashActivity : AppCompatActivity() {

    var context = this
    lateinit var bin: ActivitySplashBinding

    lateinit var sp: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(bin.root)

        sp = SharedPref(context)

        nextScreen()

    }

    private fun nextScreen() {

        Handler(Looper.myLooper()!!).postDelayed({

            if (sp.getProfile() != null) {

                FireRef.USER.document(sp.getProfile()!!.id)
                    .get()
                    .addOnCompleteListener {
                        if (it.isSuccessful && it.result.toObject(User::class.java) != null) {
                            sp.saveProfile(it.result.toObject(User::class.java)!!)
                            checkData()
                        } else {
                            moveToLogin()
                        }
                    }


            } else {
                moveToLogin()
            }
        }, 1500)

    }

    private fun checkData() {
        when (sp.getProfile()!!.status) {
            1 -> {
                startActivity(
                    Intent(
                        context,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }

            0 -> {
                startActivity(
                    Intent(
                        context,
//                  CreateProfileActivity::class.java
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
    }

    fun moveToLogin() {
        startActivity(
            Intent(
                context,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

}