package com.dev.quoc.androidtutorials.ui.status

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.dev.quoc.androidtutorials.R
import com.dev.quoc.androidtutorials.ui.profile.ProfileActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.`tintin-app`.activity_status.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class StatusActivity : AppCompatActivity() {

    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        supportActionBar!!.title = "Status"

        if (intent.extras != null) {
            var oldStatus = intent.extras.get("status")
            statusUpdateEt.setText(oldStatus.toString())
        }

        if (intent.extras == null) {
            statusUpdateEt.setText("Enter Your New Status")
        }

        statusUpdateBtn.setOnClickListener {
            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var useId = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(useId)

            var status = statusUpdateEt.text.toString().trim()

            mDatabase!!.child("status")
                    .setValue(status)
                    .addOnCompleteListener { task: Task<Void> ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Status update Successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, ProfileActivity::class.java))
                        }
                    }
        }
    }
}
