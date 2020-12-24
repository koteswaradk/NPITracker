package com.juniper.npitracker;

/**
 * Created by sarahcs on 2/7/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.juniper.npitracker.fragments.PrSummaryFragment;
import com.juniper.npitracker.fragments.RliStatusFragment;
import com.juniper.npitracker.fragments.TestStatusFragment;
import com.juniper.npitracker.model.NPIDetailsModel;


public class NPIDetailsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    NPIDetailsModel npiDetails;

    public NPIDetailsPagerAdapter(FragmentManager fm, int NumOfTabs, NPIDetailsModel npiDetails) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.npiDetails = npiDetails;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                //TestStatusFragment tab1 = new TestStatusFragment();
                TestStatusFragment tab1 = TestStatusFragment.newInstance(npiDetails);
                return tab1;
            case 1:
                PrSummaryFragment tab2 = PrSummaryFragment.newInstance(npiDetails);
                return tab2;
//            case 2:
//                RliStatusFragment tab3 = new RliStatusFragment();
//                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}