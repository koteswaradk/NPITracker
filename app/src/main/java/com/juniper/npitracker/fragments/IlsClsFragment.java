package com.juniper.npitracker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.juniper.npitracker.R;
import com.juniper.npitracker.adapter.PRSeverityDetailsAdapter;
import com.juniper.npitracker.model.NPIDetailsModel;

public class IlsClsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String DESCRIBABLE_KEY = "describable_key";

    private NPIDetailsModel npiDetails;

    public IlsClsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static IlsClsFragment newInstance(NPIDetailsModel npiDetails) {
        IlsClsFragment fragment = new IlsClsFragment();
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
        View view = inflater.inflate(R.layout.fragment_ils_cls, container, false);

        initializeViews(view);

        return view;
    }

    public void initializeViews(View view){

        npiDetails = (NPIDetailsModel) getArguments().getSerializable(DESCRIBABLE_KEY);

        ListView prSeverityDetailedList = (ListView) view.findViewById(R.id.pr_severity_detailed_list);
        PRSeverityDetailsAdapter adapter2= new PRSeverityDetailsAdapter(getActivity(), npiDetails.getPrSeverityGroups());
        prSeverityDetailedList.setAdapter(adapter2);

        TextView clClosed = (TextView) view.findViewById(R.id.customer_closed);
        clClosed.setText(String.valueOf(npiDetails.getClClosed()));

        TextView clNotClosed = (TextView) view.findViewById(R.id.customer_non_closed);
        clNotClosed.setText(String.valueOf(npiDetails.getClNotClosed()));

        TextView clBlocker = (TextView) view.findViewById(R.id.customer_blocker);
        clBlocker.setText(String.valueOf(npiDetails.getClBlocker()));

        TextView cl1 = (TextView) view.findViewById(R.id.customer_cl1);
        cl1.setText(String.valueOf(npiDetails.getCl1()));

        TextView cl2 = (TextView) view.findViewById(R.id.customer_cl2);
        cl2.setText(String.valueOf(npiDetails.getCl2()));

        TextView cl3 = (TextView) view.findViewById(R.id.customer_cl3);
        cl3.setText(String.valueOf(npiDetails.getCl3()));

        TextView cl4 = (TextView) view.findViewById(R.id.customer_cl4);
        cl4.setText(String.valueOf(npiDetails.getCl4()));
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
