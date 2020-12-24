package com.juniper.npitracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.juniper.npitracker.fragments.PhaseOneFragment;
import com.juniper.npitracker.fragments.SelectedNPIFragment;


/**
 * Created by koteswara on 09/02/17.
 */

public class SelctedPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public SelctedPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                PhaseOneFragment tab1 = new PhaseOneFragment();
                return tab1;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}