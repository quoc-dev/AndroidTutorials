package com.dev.quoc.androidtutorials.ui.dashboard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.dev.quoc.androidtutorials.ui.page.ChatFragment
import com.dev.quoc.androidtutorials.ui.page.UsersFragment

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 11/28/17.
 */
class SectionPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return UsersFragment()
            }
            1 -> {
                return ChatFragment()
            }
        }
        return null!!
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return "USERS"
            1 -> return "CHATS"
        }
        return null!!
    }
}
