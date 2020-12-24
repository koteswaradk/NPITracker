package com.juniper.npitracker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.juniper.npitracker.PRDetailsPagerAdapter;
import com.juniper.npitracker.R;
import com.juniper.npitracker.adapter.PRSeverityAdpater;
import com.juniper.npitracker.adapter.PRSeverityDetailsAdapter;
import com.juniper.npitracker.model.NPIDetailsModel;

import java.util.ArrayList;

public class PrSummaryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String DESCRIBABLE_KEY = "describable_key";

    private NPIDetailsModel npiDetails;

    public PrSummaryFragment(){

    }

    public static PrSummaryFragment newInstance(NPIDetailsModel npiDetails) {
        PrSummaryFragment fragment = new PrSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DESCRIBABLE_KEY, npiDetails);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;

        npiDetails = (NPIDetailsModel) getArguments().getSerializable(
                DESCRIBABLE_KEY);

        if(!npiDetails.getPrSeverityGroups().isEmpty()){
             view = inflater.inflate(R.layout.fragment_pr_summary, container, false);



             // view initializer
             initializeViews(view);
         }
        else{
            view = inflater.inflate(R.layout.fragment_pr_summary_empty, container, false);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void initializeViews(View view){

//        ListView prSeveritySummaryList = (ListView) view.findViewById(R.id.pr_severity_summary_list);
//        ListView prSeverityDetailedList = (ListView) view.findViewById(R.id.pr_severity_detailed_list);
//
////        ArrayList<PRSeverityGroupModel> arr = new ArrayList<PRSeverityGroupModel>();
////        arr.add(new PRSeverityGroupModel("hello", 1, 1, 1, 1, 1));
////        arr.add(new PRSeverityGroupModel("hello", 2,2,2,2,2));
////        arr.add(new PRSeverityGroupModel("hello", 3,3,3,3,3));
////        arr.add(new PRSeverityGroupModel("hello", 4,4,4,4,4));
////        arr.add(new PRSeverityGroupModel("hello", 5,5,5,5,5));
//        PRSeverityAdpater adapter1= new PRSeverityAdpater(getActivity(), npiDetails.getPrSeverityGroups());
//        prSeveritySummaryList.setAdapter(adapter1);
//
//        PRSeverityDetailsAdapter adapter2= new PRSeverityDetailsAdapter(getActivity(), npiDetails.getPrSeverityGroups());
//        prSeverityDetailedList.setAdapter(adapter2);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.any_severity_heading));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.ils_cls_heading));
//        tabLayout.addTab(tabLayout.newTab().setText("Blocker Graphs"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PRDetailsPagerAdapter adapter = new PRDetailsPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount(), npiDetails);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
