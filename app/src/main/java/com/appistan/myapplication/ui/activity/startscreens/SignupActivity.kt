package com.appistan.myapplication.ui.activity.startscreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.appistan.myapplication.ui.activity.main.MainActivity
import com.appistan.myapplication.Model.User
import com.appistan.myapplication.R
import com.appistan.myapplication.Utils.FireRef
import com.appistan.myapplication.Utils.Func
import com.appistan.myapplication.Utils.SharedPref
import com.appistan.myapplication.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    var context = this
    lateinit var bin: ActivitySignupBinding

    var isDrawableClicked = false
    var isDrawableClicked2 = false

    lateinit var sp: SharedPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivitySignupBinding.inflate(layoutInflater)
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

        bin.btnToggle2.setOnClickListener {
            if (!isDrawableClicked2) {
                bin.conPassET.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                bin.btnToggle2.setImageDrawable(getDrawable(R.drawable.ic_toggle_on))
                isDrawableClicked2 = true
            } else {
                bin.conPassET.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                bin.btnToggle2.setImageDrawable(getDrawable(R.drawable.ic_toggle_off))
                isDrawableClicked2 = false
            }
        }

        bin.btnLogin.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }

        bin.btnSignup.setOnClickListener {
            checkValidation()
        }


    }

    private fun checkValidation() {
        if (bin.fnameET.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Enter First Name", Toast.LENGTH_SHORT).show()
            return
        }

        if (bin.fnameET.text.toString().trim().length < 3) {
            Toast.makeText(context, "First Name must be 3 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (bin.lnameET.text.toString().trim().isEmpty()) {
            Toast.makeText(context, "Enter Last Name", Toast.LENGTH_SHORT).show()
            return
        }

        if (bin.lnameET.text.toString().trim().length < 3) {
            Toast.makeText(context, "Last Name must be 3 characters", Toast.LENGTH_SHORT).show()
            return
        }

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

        if (bin.conPassET.text.toString().trim().isEmpty()) {
            Toast.makeText(context,
                "Enter Confirm Password", Toast.LENGTH_SHORT).show()
            return
        }

        if (bin.conPassET.text.toString().trim().length < 8) {
            Toast.makeText(
                context,
                "Confirm Password must be 8 characters",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (bin.passET.text.toString().trim() != bin.conPassET.text.toString().trim()) {
            Toast.makeText(context,
                "Password not match!", Toast.LENGTH_SHORT).show()
            return
        }

        else{
            if (Func.isNetworkConnected(context)){
                allowToSignUp()
            }else{
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun allowToSignUp() {
        Func.loadingDialog(true, context)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            bin.emailET.text.toString().trim(),
            bin.passET.text.toString().trim()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User()
                    user.id = FirebaseAuth.getInstance().uid.toString()
                    user.fName = bin.fnameET.text.toString().trim()
                    user.lName = bin.lnameET.text.toString().trim()
                    user.email = bin.emailET.text.toString().trim()
                    user.password = bin.passET.text.toString().trim()
                    user.created = Func.getCurrentDate()
                    user.updated = Func.getCurrentDate()
                    user.status = 1 // set 0 if the create profile screen is also

                    FireRef.USER.document(user.id).set(user).addOnCompleteListener { it2 ->
                        if (it2.isSuccessful){
                           moveNext(user)
                        }else{
                            Func.loadingDialog(false, context)
                            sp.clear()
                            Toast.makeText(context, "Error in account creation", Toast.LENGTH_SHORT).show()
                        }
                    } // also set into the database


                }else{
                    Func.loadingDialog(false, context)
                    Toast.makeText(context, "Signup Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun moveNext(user: User) {
        Func.loadingDialog(false, context)
        sp.saveProfile(user) // also set in shared pref
        Toast.makeText(context, "Signup Successful!", Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(
                context,
//                CreateProfileActivity::class.java
                MainActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

}