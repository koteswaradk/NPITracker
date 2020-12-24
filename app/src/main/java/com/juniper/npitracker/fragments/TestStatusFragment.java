package com.juniper.npitracker.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.juniper.npitracker.R;
import com.juniper.npitracker.adapter.NpiDetailsAdapter;
import com.juniper.npitracker.model.NPIDetailsModel;

import org.w3c.dom.Text;

public class TestStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String DESCRIBABLE_KEY = "describable_key";

    private TextView npiName;

    private TextView testProgress;
    private TextView testProgressValue;
    private TextView rspExecution;
    private TextView rspExecutionValue;
    private TextView rspPassRate;
    private TextView rspPassRateValue;
    private TextView rspScripted;
    private TextView rspScriptedValue;

    private OnFragmentInteractionListener mListener;
    private Button sendEmailButton;

    //NPIDetailsParser npiDetailsParser;
    private NPIDetailsModel npiDetails;

    public TestStatusFragment() {
        // Required empty public constructor
    }

    public static TestStatusFragment newInstance(NPIDetailsModel npiDetails) {
        TestStatusFragment fragment = new TestStatusFragment();
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
        View view = inflater.inflate(R.layout.fragment_test_status, container, false);
        npiDetails = (NPIDetailsModel) getArguments().getSerializable(DESCRIBABLE_KEY);

        // view initializer
        initializeViews(view);

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

        npiName = (TextView) view.findViewById(R.id.npi_name);
        npiName.setText(
                String.format(getResources().getString(R.string.npi_name), npiDetails.getNPIName(), npiDetails.getVersionNumber()));
        npiName.setTypeface(null, Typeface.BOLD);
        testProgress = (TextView) view.findViewById(R.id.test_progress);
        testProgress.setText(String.format(getResources().getString(R.string.test_progress), npiDetails.getTestProgress()));

        testProgressValue = (TextView) view.findViewById(R.id.test_progress_value);
        testProgressValue.setText(
                String.format(getResources().getString(R.string.test_progress_value), npiDetails.getTestProgress()));

        rspExecution = (TextView) view.findViewById(R.id.rsp_execution);
        rspExecution.setText(String.format(getResources().getString(R.string.execution), npiDetails.getActualExecuted(), npiDetails.getTotalExecution()));

        rspExecutionValue = (TextView) view.findViewById(R.id.rsp_execution_value);
        rspExecutionValue.setText(String.valueOf(npiDetails.getExecutionPercentage()) + "%");

        rspPassRate = (TextView) view.findViewById(R.id.rsp_pass_rate);
        rspPassRate.setText(String.format(getResources().getString(R.string.pass_rate), npiDetails.getActualPassRate(), npiDetails.getTotalExecution()));

        rspPassRateValue = (TextView) view.findViewById(R.id.rsp_pass_rate_value);
        rspPassRateValue.setText(String.valueOf(npiDetails.getPassRatePercentage()) + "%");;

        rspScripted = (TextView) view.findViewById(R.id.rsp_scripted);
        rspScripted.setText(String.format(getResources().getString(R.string.scripted), npiDetails.getActualScripted(), npiDetails.getTotalScripted()));
        rspScriptedValue = (TextView) view.findViewById(R.id.rsp_scripted_value);
        rspScriptedValue.setText(String.valueOf(npiDetails.getScriptedPercentage()) + "%");

//        sendEmailButton = (Button) view.findViewById(R.id.send_email);
//        sendEmailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendEmail("", "");
//            }
//        });

        FloatingGroupExpandableListView myList = (FloatingGroupExpandableListView) view.findViewById(R.id.expandableListView1);
        BaseExpandableListAdapter myAdapter = new NpiDetailsAdapter(getActivity(), npiDetails);
        WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(myAdapter);
        myList.setAdapter(wrapperAdapter);

    }

    /*
     * Forwards content to Outlook app
     * Content:
     *  1. Subject of the email
     *  2. Test Status Report as body
     */
    private void sendEmail(String subject, String body){

        try{
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");

            if (subject != null)
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            if (body != null)
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);

            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(getActivity().getApplicationContext(),"Cannot send email.",Toast.LENGTH_SHORT).show();
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
