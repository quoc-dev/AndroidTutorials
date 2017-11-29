package com.dev.quoc.androidtutorials.ui.setting

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.dev.quoc.androidtutorials.R
import com.dev.quoc.androidtutorials.ui.status.StatusActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.`tintin-app`.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/29/17.
 */

class SettingActivity : AppCompatActivity() {

    var GALLERY_ID: Int = 100

    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mStorageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        mStorageRef = FirebaseStorage.getInstance().reference

        var userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                var displayName = dataSnapshot!!.child("display_name").value
                var image = dataSnapshot!!.child("image").value.toString()
                var status = dataSnapshot!!.child("status").value
                var thumbNail = dataSnapshot!!.child("thumb_image").value

                settingsDisplayName.text = displayName.toString()
                settingsStatusText.text = status.toString()

                if (image!! != "default") {
                    Picasso.with(applicationContext)
                            .load(image)
                            .placeholder(R.drawable.profile_img)
                            .into(settingsProfileID)
                }
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }
        })

        settingsChangeStatus.setOnClickListener {
            var intent = Intent(this, StatusActivity::class.java)

            intent.putExtra("status", settingsStatusText.text.toString().trim())
            startActivity(intent)
        }

        settingsChangeImgBtn.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {

            var image: Uri = data!!.data

            CropImage.activity(image)
                    .setAspectRatio(1, 1)
                    .start(this)
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {
                val resultUri = result.uri

                var userId = mCurrentUser!!.uid
                var thumbFile = File(resultUri.path)

                // giam size image
                var thumbBitmap = Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(65)
                        .compressToBitmap(thumbFile)

                // Upload to fire base
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                        byteArray)

                var thumbByteArray: ByteArray
                thumbByteArray = byteArray.toByteArray()

                var filePath = mStorageRef!!.child("chat_profile_images")
                        .child(userId + ".jpg")

                // create another directory for thumb image (smaller, compressed images)
                var thumbFilePath = mStorageRef!!.child("chat_profile_images")
                        .child("thumbs")
                        .child(userId + ".jpg")

                filePath.putFile(resultUri)
                        .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                            if (task.isSuccessful) {

                                // let's get the pic url
                                var downloadUrl = task.result.downloadUrl.toString()

                                // Upload task
                                var uploadTask: UploadTask = thumbFilePath
                                        .putBytes(thumbByteArray)

                                uploadTask.addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->

                                    var thumbUrl = task.result.downloadUrl.toString()

                                    if (task.isSuccessful) {

                                        var updateObj = HashMap<String, Any>()
                                        updateObj.put("image", downloadUrl)
                                        updateObj.put("thumb_image", thumbUrl)

                                        // We save the profile image

                                        mDatabase!!.updateChildren(updateObj)
                                                .addOnCompleteListener { task: Task<Void> ->
                                                    if (task.isSuccessful) {
                                                        Toast.makeText(this, "Profile iamge save", Toast.LENGTH_SHORT).show()
                                                    } else {

                                                    }
                                                }
                                    } else {

                                    }
                                }
                            }
                        }
            }
            super.onActivityResult(requestCode, resultCode, data)

        }
    }
}
