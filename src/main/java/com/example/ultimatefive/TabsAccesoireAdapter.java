package com.example.ultimatefive;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * created by Abdulhalim Khaled on 2020-01-04.
 */
class TabsAccesoireAdapter extends FragmentPagerAdapter {

    public TabsAccesoireAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;


            case 1:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;


            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;


            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "Chat";


            case 1:
                return "Groups";

            case 2:
                return "friends";



            default:
                return null;

        }
    }
}
