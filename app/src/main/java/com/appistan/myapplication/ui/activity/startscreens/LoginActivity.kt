package com.appistan.myapplication.ui.activity.startscreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.appistan.myapplication.Model.User
import com.appistan.myapplication.R
import com.appistan.myapplication.Utils.FireRef
import com.appistan.myapplication.Utils.Func
import com.appistan.myapplication.Utils.SharedPref
import com.appistan.myapplication.databinding.ActivityLoginBinding
import com.appistan.myapplication.ui.activity.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

        var context = this
    lateinit var bin: ActivityLoginBinding

    var isDrawableClicked = false

    lateinit var sp: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bin.root)

        _init()

        clickListener()

    }

    private fun _init() {
        sp = SharedPref(context)
    }

    private fun clickListener() {

        bin.btnToggle.setOnClickListener {
            if (!isDrawableClicked) {
                bin.passET.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                bin.btnToggle.setImageDrawable(getDrawable(R.drawable.ic_toggle_on))
                isDrawableClicked = true
            } else {
                bin.passET.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                bin.btnToggle.setImageDrawable(getDrawable(R.drawable.ic_toggle_off))
                isDrawableClicked = false
            }
        }

        bin.btnSignup.setOnClickListener {
            startActivity(Intent(context, SignupActivity::class.java))
        }

        bin.btnForget.setOnClickListener {
            startActivity(Intent(context, ForgetPasswordActivity::class.java))
        }

        bin.btnLogin.setOnClickListener {
            checkValidation()
        }

    }

    private fun checkValidation() {

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
        }

        if (bin.passET.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Enter Password", Toast.LENGTH_SHORT).show()
            return
        }

        if (bin.passET.text.toString().trim().length < 8) {
            Toast.makeText(
                context,
                "Password must be 8 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        else{
            if (Func.isNetworkConnected(context)){
                allowLogin()
            }else{
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allowLogin() {
        Func.loadingDialog(true, context)
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(bin.emailET.text.toString().trim(), bin.passET.text.toString().trim())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    getUserData()
                }
            }
            .addOnFailureListener {
                Func.loadingDialog(false, context)
                Toast.makeText(context, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun getUserData() {
        FireRef.USER.document(FirebaseAuth.getInstance().uid!!)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result.exists()){
                    val md = it.result.toObject(User::class.java)
                    if (md != null){
                        sp.saveProfile(md) // save into shared pref also
                        checkData()
                    }else{
                        sp.clear()
                        Func.loadingDialog(false, context)
                        Toast.makeText(context, "Error in data!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    sp.clear()
                    Func.loadingDialog(false, context)
                    Toast.makeText(context, "Error in data!", Toast.LENGTH_SHORT).show()
                }
            }
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


}