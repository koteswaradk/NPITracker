package com.juniper.npitracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.juniper.npitracker.fragments.PhaseFourFragment;
import com.juniper.npitracker.fragments.PhaseOneFragment;
import com.juniper.npitracker.fragments.PhaseThreeFragment;
import com.juniper.npitracker.fragments.PhaseTwoFragment;

/**
 * Created by koteswara on 08/02/17.
 */

public class ViewPagerAdapte extends FragmentStatePagerAdapter{
    int mNumOfTabs;
    public ViewPagerAdapte(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PhaseOneFragment tab1 = new PhaseOneFragment();
                return tab1;
            case 1:
                PhaseTwoFragment tab2 = new PhaseTwoFragment();
                return tab2;
            case 2:
                PhaseThreeFragment tab3 = new PhaseThreeFragment();
                return tab3;
            case 3:
                PhaseFourFragment tab4 = new PhaseFourFragment();
                return tab4;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
