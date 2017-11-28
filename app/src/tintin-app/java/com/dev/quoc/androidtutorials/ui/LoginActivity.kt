package com.dev.quoc.androidtutorials.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.dev.quoc.androidtutorials.R
import com.dev.quoc.androidtutorials.ui.dashboard.DashBoardActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.`tintin-app`.activity_login.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDataBase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        loginButtonId.setOnClickListener {
            var email = loginEmailE.text.toString().trim()
            var password = loginPasswordEt.text.toString().trim()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {

                        var username = email.split("@")[0]


                        var dashboardIntent = Intent(this, DashBoardActivity::class.java)
                        dashboardIntent.putExtra("name", username)
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        Toast.makeText(this, "Login false", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}