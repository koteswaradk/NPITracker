package com.juniper.npitracker.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.juniper.npitracker.JdiAppController;
import com.juniper.npitracker.R;
import com.juniper.npitracker.adapter.PhaseFourAdapter;
import com.juniper.npitracker.adapter.PhaseThreeAdapter;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.model.NPIModel;
import com.juniper.npitracker.model.PhaseFourModel;
import com.juniper.npitracker.model.PhaseTwoModel;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PhaseFourFragment extends Fragment {

    String TAG=getClass().getSimpleName();
    ListView listviewphasefour;
    SearchView searchview;
    Button savephase4;
    PhaseFourAdapter adapter;
    private ArrayList<PhaseFourModel> npiTrackerModels = new ArrayList<PhaseFourModel>();
    public PhaseFourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_phase_four, container, false);
        searchview=(SearchView)view.findViewById(R.id.phasefoursearchview);
        listviewphasefour=(ListView) view.findViewById(R.id.phase_four_listview);
       savephase4=(Button)view.findViewById(R.id.phase4save);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setupSearchView();
        npiTrackerModels.clear();
        Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API));
                //  Log.i(TAG+"String",apistring);
                npiTrackerModels=  parseDbStringValues(apistring);
                // Log.i("string",""+apistring);
                adapter=new PhaseFourAdapter(getActivity(),npiTrackerModels);
                listviewphasefour.setAdapter(adapter);

            } while (cursor.moveToNext());
        }
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
       /* npiTrackerModels.clear();
        Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API));
              //  Log.i(TAG+"String",apistring);
                npiTrackerModels=  parseDbStringValues(apistring);
               // Log.i("string",""+apistring);
                adapter=new PhaseFourAdapter(getActivity(),npiTrackerModels);
                listviewphasefour.setAdapter(adapter);

            } while (cursor.moveToNext());
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();
        savephase4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JdiAppController globalVariable= globalVariable = (JdiAppController) getActivity().getApplicationContext();
                ArrayList<String> npiid= globalVariable.getNpiid();
                HashMap keyandid= globalVariable.getNpikeyandid();

               // Log.i(TAG,""+npiid);
               // Log.i(TAG,""+keyandid);
            }
        });
    }

    private void setupSearchView()
    {

        searchview.setQueryRefinementEnabled(true);
        searchview.setSubmitButtonEnabled(true);
        searchview.setQueryHint("Search Here");

        searchview.setIconifiedByDefault(false);


        searchview.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);
//                mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);

                if (TextUtils.isEmpty(newText)) {
                    listviewphasefour.clearTextFilter();
                    npiTrackerModels.clear();
                    searchview.clearFocus();
                    Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do{
                            String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API));
                            //  Log.i(TAG+"String",apistring);
                            npiTrackerModels=  parseDbStringValues(apistring);
                            // Log.i("string",""+apistring);
                            adapter=new PhaseFourAdapter(getActivity(),npiTrackerModels);
                            listviewphasefour.setAdapter(adapter);

                        } while (cursor.moveToNext());
                    }
                } else {
                    PhaseFourAdapter ca = (PhaseFourAdapter)listviewphasefour.getAdapter();
                    ca.getFilter().filter(newText);
                    //following line was causing the ugly popup window.
                    //m_listView.setFilterText(newText);
                }
                /*if (TextUtils.isEmpty(s)) {
                    listview.clearTextFilter();
                } else {
                    listview.setFilterText(s);
                }*/

                return true;
            }

        });
    }


    private ArrayList<PhaseFourModel> parseDbStringValues(String jsonStr) {
        if (jsonStr!=null) {
            try {

                JSONObject obj = new JSONObject(jsonStr);


                JSONArray m_jArry = obj.getJSONArray("npilistP4");

                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);

                    PhaseFourModel model = new PhaseFourModel();

                    model.setNpiname(jo_inside.getString("npi_synopsis"));
                    //  Log.i(TAG,model.getNpiname());
                    model.setSwlead(jo_inside.getString("sw_lead"));
                    //Log.i(TAG,model.getSwlead());
                    model.setPlm(jo_inside.getString("plm_lead"));
                    //  Log.i(TAG,model.getPlm());
                    model.setTestlead(jo_inside.getString("systest_lead"));
                    model.setNpiid(jo_inside.getString("npi_id"));
                    // Log.i(TAG,model.getTestlead());
                    model.setStatus(jo_inside.getString("status"));
                    npiTrackerModels.add(model);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            CustomTast ct=new CustomTast(getActivity());
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }


        return npiTrackerModels;
    }
}
