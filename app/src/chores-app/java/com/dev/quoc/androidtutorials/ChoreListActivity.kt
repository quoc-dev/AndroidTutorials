package com.dev.quoc.androidtutorials

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.dev.quoc.androidtutorials.data.ChoreListAdapter
import com.dev.quoc.androidtutorials.data.ChoresDatabaseHandler
import com.dev.quoc.androidtutorials.model.Chore
import kotlinx.android.synthetic.`chores-app`.activity_chore_list.*
import kotlinx.android.synthetic.`chores-app`.popup.view.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/27/17.
 */
class ChoreListActivity : AppCompatActivity() {

    private lateinit var choreListAdapter: ChoreListAdapter
    private lateinit var choreList: ArrayList<Chore>
    private lateinit var choreListItems: ArrayList<Chore>
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var dbHandler: ChoresDatabaseHandler

    private var dialogBuilder: AlertDialog.Builder? = null
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        dbHandler = ChoresDatabaseHandler(this)

        choreList = ArrayList<Chore>()
        choreListItems = ArrayList()
        layoutManager = LinearLayoutManager(this)
        choreListAdapter = ChoreListAdapter(choreListItems, this)


        recyclerViewId.layoutManager = layoutManager
        recyclerViewId.adapter = choreListAdapter

        loadData()
    }

    fun loadData() {
        choreList = dbHandler.readChores()
        choreList.reverse()

        for (c in choreList.iterator()) {
            val chore = Chore()
            chore.choreName = c.choreName
            chore.assignedBy = c.assignedBy
            chore.assignedTo = c.assignedTo
            chore.id = c.id
            chore.showHumanDate(c.timeAssigned!!)

            choreListItems.add(chore)
        }
        choreListAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.add_menu_button) {
            createPopupDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun createPopupDialog() {
        var view = layoutInflater.inflate(R.layout.popup, null)
        var choreName = view.popEnterChore
        var assignedBy = view.popEnterAssignedBy
        var assignedTo = view.popEnterAssignedTo
        var saveButton = view.popSaveChore

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog.show()


        saveButton.setOnClickListener {
            var name = choreName.text.toString().trim()
            var aBy = assignedBy.text.toString().trim()
            var aTo = assignedTo.text.toString().trim()

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(aBy) && !TextUtils.isEmpty(aTo)) {

                var chore = Chore()
                chore.choreName = name
                chore.assignedBy = aBy
                chore.assignedTo = aTo


                dbHandler.createChore(chore)

                dialog.dismiss()

                startActivity(Intent(this, ChoreListActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter a chore", Toast.LENGTH_SHORT).show()
            }
        }
    }
}