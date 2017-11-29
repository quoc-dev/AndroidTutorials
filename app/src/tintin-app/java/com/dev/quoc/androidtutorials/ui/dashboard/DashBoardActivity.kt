package com.dev.quoc.androidtutorials.ui.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.dev.quoc.androidtutorials.R
import com.dev.quoc.androidtutorials.TinTinActivity
import com.dev.quoc.androidtutorials.ui.setting.SettingActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.`tintin-app`.activity_dashboard.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class DashBoardActivity : AppCompatActivity() {

    var sectionAdapter: SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar!!.title = "Dashboard"

        sectionAdapter = SectionPagerAdapter(supportFragmentManager)

        dashViewPagerId.adapter = sectionAdapter
        mainTabs.setupWithViewPager(dashViewPagerId)
        mainTabs.setTabTextColors(Color.WHITE, Color.GREEN)


        if (intent.extras != null) {
            var username = intent.extras.get("name")
            Toast.makeText(this, username.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == R.id.logoutId) {
                //Log the user out!
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, TinTinActivity::class.java))
                finish()
            }

            if (item.itemId == R.id.settingsId) {
                //take user to settingsActivity
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
        return true
    }
}