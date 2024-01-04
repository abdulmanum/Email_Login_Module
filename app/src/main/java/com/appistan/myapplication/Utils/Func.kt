package com.appistan.myapplication.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.ActionBar
import com.appistan.myapplication.R
import java.util.Calendar
import java.util.Date
import java.util.Objects
import java.util.regex.Pattern

object Func {

    private var dialog: Dialog? = null


    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun getCurrentDateInLong(): Long {
        return Calendar.getInstance().time.time
    }

    fun isValidEmailAddress(email: String?): Boolean {
        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

    // check internet is connected
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    // loading dialog
    fun loadingDialog(show: Boolean, context: Context?) {
        if (show) {
            dialog = Dialog(context!!)
            dialog!!.setContentView(R.layout.loading_dialog)
            dialog!!.setCancelable(false)
            Objects.requireNonNull(dialog!!.window)!!.setBackgroundDrawable(
                ColorDrawable(
                    Color.TRANSPARENT
                )
            )
            dialog!!.show()
            dialog!!.window!!
                .setLayout(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT
                )
        } else {
            dialog?.dismiss()
        }
    }

}