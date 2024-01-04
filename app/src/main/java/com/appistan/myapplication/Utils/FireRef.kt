package com.appistan.myapplication.Utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FireRef {
    private val firestore = FirebaseFirestore.getInstance()

    //auth
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    //fireStorage ref
    val USER = firestore.collection("Users")

}