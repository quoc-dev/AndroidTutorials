package com.dev.quoc.androidtutorials.ui.profile

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dev.quoc.androidtutorials.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.`tintin-app`.activity_profile.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class ProfileActivity : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mUserDatabase: DatabaseReference? = null
    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar!!.title = "Profile"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (intent.extras != null) {
            userId = intent.extras.get("userId").toString()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            mUserDatabase = FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(userId)

            setUpProfile()
        }
    }

    private fun setUpProfile() {
        mUserDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                var displayName = dataSnapshot!!.child("display_name").value.toString()
                var status = dataSnapshot!!.child("status").value.toString()
                var image = dataSnapshot!!.child("image").value.toString()


                profileName.text = displayName
                profileStatus.text = status


                Picasso.with(this@ProfileActivity)
                        .load(image)
                        .error(R.drawable.happy_woman)
                        .placeholder(R.drawable.happy_woman)
                        .into(profilePicture)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }
}