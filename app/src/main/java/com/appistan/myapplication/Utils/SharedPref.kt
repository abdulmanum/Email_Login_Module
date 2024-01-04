package com.appistan.myapplication.Utils

import android.content.Context
import android.content.SharedPreferences
import com.appistan.myapplication.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson


class SharedPref(var context: Context) {

     fun save(key: String?, value: String?) {
        val sh: SharedPreferences = context?.getSharedPreferences("data", Context.MODE_PRIVATE)!!
        val editor = sh.edit()
        editor.putString(key, value)
        editor.commit()
    }

     fun get(key: String?): String? {
        val sh = context!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        return sh.getString(key, "")
    }



    fun clear() {
        val settings = context!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = settings.edit()

        FirebaseAuth.getInstance().signOut()
//        val map: HashMap<String, Any> = HashMap()
//        map["fcm"] = ""
//        FireRef.USER.document(getProfile()!!.uid).update(map).isSuccessful
        editor.clear()
        editor.apply()

        editor.putString("profile", "")
        editor.apply()

    }

    fun saveProfile(users: User) {
        save("profile", Gson().toJson(users))
    }

    fun getProfile(): User? {
        val data = get("profile")
        return Gson().fromJson(data, User::class.java)
    }


    fun getBoolean(key: String?): Boolean? {
        val sh = context!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        return sh.getBoolean(key, false)
    }

    fun saveBoolen(key: String?, value: Boolean) {
        val prefs = context!!.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }


}