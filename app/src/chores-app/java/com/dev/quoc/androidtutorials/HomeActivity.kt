package com.dev.quoc.androidtutorials

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.dev.quoc.androidtutorials.data.ChoresDatabaseHandler
import com.dev.quoc.androidtutorials.model.Chore
import kotlinx.android.synthetic.`chores-app`.activity_home.*

class HomeActivity : AppCompatActivity() {

    var dbHandler: ChoresDatabaseHandler? = null

    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        progressDialog = ProgressDialog(this)

        dbHandler = ChoresDatabaseHandler(this)

        checkDB()

        saveChore.setOnClickListener {
            progressDialog?.setMessage("Saving...")
            progressDialog?.show()

            if (!TextUtils.isEmpty(enterChoreId.text.toString())
                    && !TextUtils.isEmpty(assignToId.text.toString())
                    && !TextUtils.isEmpty(assignedById.text.toString())) {

                // save to database
                var chore = Chore()
                chore.choreName = enterChoreId.text.toString()
                chore.assignedTo = assignToId.text.toString()
                chore.assignedBy = assignedById.text.toString()
                saveToDB(chore)

                progressDialog?.cancel()
                startActivity(Intent(this, ChoreListActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter a chore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveToDB(chore: Chore) {
        dbHandler?.createChore(chore)
    }

    fun checkDB() {
        if (dbHandler?.getChoresCount()!! > 0) {
            startActivity(Intent(this, ChoreListActivity::class.java))
        }
    }
}
