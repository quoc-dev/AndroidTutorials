package com.dev.quoc.androidtutorials.ui.register

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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.`tintin-app`.activity_create_account.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class RegisterActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDataBase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        mAuth = FirebaseAuth.getInstance()

        accountCreateActBtn.setOnClickListener {
            var email = accountEmailEt.text.toString().trim()
            var password = accountPasswordEt.text.toString().trim()
            var displayName = accountDisplayNameEt.text.toString().trim()

            if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)
                    || !TextUtils.isEmpty(displayName)) {

                createAccount(email, password, displayName)
            } else {
                Toast.makeText(this, "Please fill out the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createAccount(email: String, password: String, displayName: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        var currentUser = mAuth!!.currentUser
                        var userId = currentUser!!.uid

                        mDataBase = FirebaseDatabase.getInstance().reference
                                .child("Users").child(userId)

                        var userObject = HashMap<String, String>()
                        userObject.put("display_name", displayName)
                        userObject.put("status", "Hello there...")
                        userObject.put("image", "default")
                        userObject.put("thumb_image", "default")

                        mDataBase!!.setValue(userObject).addOnCompleteListener { task: Task<Void> ->
                            if (task.isSuccessful) {
                                var dashboardIntent = Intent(this, DashBoardActivity::class.java)
                                dashboardIntent.putExtra("name", displayName)
                                startActivity(dashboardIntent)
                                finish()

                            } else {
                                Toast.makeText(this, "Created fails", Toast.LENGTH_SHORT).show()
                            }
                        }

                    } else {

                    }
                }
    }
}