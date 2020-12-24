package com.juniper.npitracker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.juniper.npitracker.R;
import com.juniper.npitracker.adapter.PRSeverityAdpater;
import com.juniper.npitracker.adapter.PRSeverityDetailsAdapter;
import com.juniper.npitracker.model.NPIDetailsModel;

public class AnySeverityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String DESCRIBABLE_KEY = "describable_key";

    private NPIDetailsModel npiDetails;

    public AnySeverityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AnySeverityFragment newInstance(NPIDetailsModel npiDetails) {
        AnySeverityFragment fragment = new AnySeverityFragment();
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
        View view = inflater.inflate(R.layout.fragment_any_severity, container, false);

        initilaizeViews(view);

        return view;
    }

    public void initilaizeViews(View view){

        npiDetails = (NPIDetailsModel) getArguments().getSerializable(DESCRIBABLE_KEY);

        ListView prSeverityList = (ListView) view.findViewById(R.id.pr_severity_summary_list);
        PRSeverityAdpater adapter1= new PRSeverityAdpater(getActivity(), npiDetails.getPrSeverityGroups());
        prSeverityList.setAdapter(adapter1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
