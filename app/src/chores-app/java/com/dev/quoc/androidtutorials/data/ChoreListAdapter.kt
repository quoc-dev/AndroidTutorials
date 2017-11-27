package com.dev.quoc.androidtutorials.data

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.quoc.androidtutorials.R
import com.dev.quoc.androidtutorials.model.Chore
import kotlinx.android.synthetic.`chores-app`.list_row.view.*
import kotlinx.android.synthetic.`chores-app`.popup.view.*
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/27/17.
 */
class ChoreListAdapter(private val chores: ArrayList<Chore>, private val context: Context) :
        RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.list_row, parent, false)
        return ViewHolder(view, chores)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindItem()
    }

    override fun getItemCount(): Int = chores.size

    inner class ViewHolder(itemView: View, list: ArrayList<Chore>) : RecyclerView.ViewHolder(itemView) {
        var mList = list

        fun bindItem() = with(itemView) {
            var mPosition: Int = adapterPosition
            var chore = mList[mPosition]

            listChoreName.text = chore.choreName
            listAssignedBy.text = chore.assignedBy
            listAssignedTo.text = chore.assignedTo
            listDate.text = chore.showHumanDate(System.currentTimeMillis())

            listDeleteButton.setOnClickListener {
                deleteChore(chore.id!!)
                mList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }

            listEditButton.setOnClickListener {
                editChore(chore)
            }

        }

        fun deleteChore(id: Int) {

            var db = ChoresDatabaseHandler(context)
            db.deleteChore(id)

        }

        fun editChore(chore: Chore) {

            var dialogBuilder: AlertDialog.Builder?
            var dialog: AlertDialog?
            var dbHandler = ChoresDatabaseHandler(context)

            var view = LayoutInflater.from(context).inflate(R.layout.popup, null)
            var choreName = view.popEnterChore
            var assignedBy = view.popEnterAssignedBy
            var assignedTo = view.popEnterAssignedTo
            var saveButton = view.popSaveChore

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder!!.create()
            dialog?.show()

            saveButton.setOnClickListener {
                var name = choreName.text.toString().trim()
                var aBy = assignedBy.text.toString().trim()
                var aTo = assignedTo.text.toString().trim()

                if (!TextUtils.isEmpty(name)
                        && !TextUtils.isEmpty(aBy)
                        && !TextUtils.isEmpty(aTo)) {
                    // var chore = Chore()

                    chore.choreName = name
                    chore.assignedTo = aTo
                    chore.assignedBy = aBy

                    dbHandler!!.updateChore(chore)
                    notifyItemChanged(adapterPosition, chore)
                    dialog!!.dismiss()
                } else {

                }
            }

        }
    }
}