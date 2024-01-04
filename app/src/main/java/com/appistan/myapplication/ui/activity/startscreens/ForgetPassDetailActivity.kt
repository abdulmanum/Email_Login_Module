package com.appistan.myapplication.ui.activity.startscreens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appistan.myapplication.R
import com.appistan.myapplication.databinding.ActivityForgetPassDetailBinding
import com.appistan.myapplication.databinding.ActivityForgetPasswordBinding

class ForgetPassDetailActivity : AppCompatActivity() {

    var context = this
    lateinit var bin: ActivityForgetPassDetailBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityForgetPassDetailBinding.inflate(layoutInflater)
        setContentView(bin.root)

        clickListener()

    }

    private fun clickListener() {

        bin.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}