package com.yoyoyee.zerodistance.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yoyoyee.zerodistance.fragment.fragment_achievement;
import com.yoyoyee.zerodistance.fragment.fragment_havebeen;
import com.yoyoyee.zerodistance.fragment.fragment_mission;
import com.yoyoyee.zerodistance.fragment.fragment_notbeen;
import com.yoyoyee.zerodistance.fragment.fragment_setting;
import com.yoyoyee.zerodistance.fragment.fragment_team;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            fragment_mission fm = new fragment_mission();
            f= fm;
        }
        if(position == 1)             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            fragment_team ft = new fragment_team();
            f = ft;
        }
        if(position==2){
            fragment_notbeen fnb = new fragment_notbeen();
            f = fnb;
        }
        if(position==3){
            fragment_havebeen fhb = new fragment_havebeen();
            f = fhb;
        }
        if(position==4){
            fragment_achievement fah = new fragment_achievement();
            f = fah;
        }
        if(position==5){
            fragment_setting fah = new fragment_setting();
            f = fah;
        }
        return f;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}