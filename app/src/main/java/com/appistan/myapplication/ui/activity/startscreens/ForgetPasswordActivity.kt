package com.appistan.myapplication.ui.activity.startscreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.appistan.myapplication.R
import com.appistan.myapplication.Utils.Func
import com.appistan.myapplication.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {

    var context = this
    lateinit var bin: ActivityForgetPasswordBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(bin.root)

        clickListener()

    }

    private fun clickListener() {

        bin.btnSend.setOnClickListener {
            validation()
        }

        bin.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun validation() {
        if (bin.emailET.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
            return
        }

        if (bin.emailET.text.toString().trim().length < 10) {
            Toast.makeText(context, "Email minimum be 10 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Func.isValidEmailAddress(bin.emailET.text.toString().trim())) {
            Toast.makeText(context, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            return
        } else {
            forgetData()
        }
    }

    private fun forgetData() {
        Func.loadingDialog(true, context)
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(bin.emailET.text.toString().trim())
            .addOnCompleteListener { task ->
                Func.loadingDialog(false, context)
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Email link sent successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(context, ForgetPassDetailActivity::class.java))
                }else{
                    Toast.makeText(
                        context,
                        "Password reset failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                Func.loadingDialog(false, context)
                Toast.makeText(
                    context,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

}