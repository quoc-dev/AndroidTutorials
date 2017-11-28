package com.dev.quoc.androidtutorials.ui.page

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.dev.quoc.androidtutorials.R
import com.dev.quoc.androidtutorials.model.Users
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class UsersAdapter(databaseReference: DatabaseReference, var context: Context)
    : FirebaseRecyclerAdapter<Users, UsersAdapter.ViewHolder>(
        Users::class.java,
        R.layout.users_row,
        UsersAdapter.ViewHolder::class.java,
        databaseReference
) {
    override fun populateViewHolder(viewHolder: UsersAdapter.ViewHolder?, users: Users?, position: Int) {
        var userId = getRef(position).key
        viewHolder!!.bindView(users!!, context)

        viewHolder.itemView.setOnClickListener {
            Toast.makeText(context, "User row clicked $userId", Toast.LENGTH_LONG)
                    .show()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userNameTxt: String? = null
        var userStatusTxt: String? = null
        var userProfilePicLink: String? = null

        fun bindView(user: Users, context: Context) {
            var userName = itemView.findViewById<TextView>(R.id.userName)
            var userStatus = itemView.findViewById<TextView>(R.id.userStatus)
            var userProfilePic = itemView.findViewById<CircleImageView>(R.id.usersProfile)

            //set the strings so we can pass in the intent
            userNameTxt = user.diplay_name
            userStatusTxt = user.status
            userProfilePicLink = user.thumb_image

            userName.text = user.diplay_name
            userStatus.text = user.status

            Picasso.with(context)
                    .load(userProfilePicLink)
                    .placeholder(R.drawable.profile_img)
                    .into(userProfilePic)


        }
    }
}