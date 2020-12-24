package com.juniper.npitracker.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.juniper.npitracker.JdiAppController;
import com.juniper.npitracker.R;
import com.juniper.npitracker.adapter.PhaseOneAdapter;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.model.PhaseOneModel;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class PhaseOneFragment extends Fragment implements SearchView.OnCloseListener{
    String TAG=getClass().getSimpleName();
    ListView listview;
    SearchView searchview;
    PhaseOneAdapter adapter;
    Button button;
    private ArrayList<PhaseOneModel> npiTrackerModels = new ArrayList<PhaseOneModel>();

    public PhaseOneFragment() {
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
        View view= inflater.inflate(R.layout.fragment_phase_one, container, false);
        searchview=(SearchView)view.findViewById(R.id.phaseone_searchview);
        listview=(ListView) view.findViewById(R.id.phase_one_listview);
        button=(Button)view.findViewById(R.id.phase1save);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setupSearchView();

       /* listview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //CheckBox checkBox=(CheckBox)view.findViewById(R.id.select);
                LinearLayout  layout_id=(LinearLayout)view.findViewById(R.id.npi_id);
                Log.i(TAG,layout_id+"this is the id");


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               ArrayList<String> selectednpiid= adapter.getAllData();
               /* Log.i(TAG,""+selectednpiid);
                Log.i(TAG,""+adapter.getAllwithkeyData());*/
                HashMap<String,ArrayList<String>> allData= adapter.getAllwithkeyDataFull();
                JdiAppController globalVariable= globalVariable = (JdiAppController) getActivity().getApplicationContext();
               ArrayList<String> npiid= globalVariable.getNpiid();
                HashMap keyandid= globalVariable.getNpikeyandid();

               // Log.i(TAG,""+npiid);
               // Log.i(TAG,""+keyandid);
               // Log.i(TAG,""+adapter.getAllwithkeyDataFull());
               /* for (int i = 0; i <allData.size() ; i++) {
                    allData.get(i);
                    //Log.i(TAG,allData.get(i).toString());
                }*/
                /*for(PhaseOneModel hold: adapter.getAllData()){
                    if(hold.isCheckbox()){
                        countries += " "  + hold.getName();
                    }
                }*/
            }
        });

        npiTrackerModels.clear();

        Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API));
                // Log.i(TAG+"String",apistring);
                npiTrackerModels=  parseDbStringValues(apistring);
                //  Log.i("string",""+apistring);
                adapter=new PhaseOneAdapter(getActivity(),npiTrackerModels);
                listview.setAdapter(adapter);

            } while (cursor.moveToNext());
        }
        return view;
    }
    private void setupSearchView()
    {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        searchview.setIconifiedByDefault(false);
        searchview.setQueryRefinementEnabled(true);
        searchview.setSubmitButtonEnabled(true);
        searchview.setQueryHint("Search Here");
        searchview.setIconifiedByDefault(false);

        searchview.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
                //return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);
//                mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);

                if (TextUtils.isEmpty(newText)) {
                    listview.clearTextFilter();
                    npiTrackerModels.clear();
                    searchview.clearFocus();
                    Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do{
                            String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API));
                            // Log.i(TAG+"String",apistring);
                            npiTrackerModels=  parseDbStringValues(apistring);
                            //  Log.i("string",""+apistring);
                            adapter=new PhaseOneAdapter(getActivity(),npiTrackerModels);
                            listview.setAdapter(adapter);

                        } while (cursor.moveToNext());
                    }
                } else {
                    PhaseOneAdapter ca = (PhaseOneAdapter)listview.getAdapter();
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
    @Override
    public void onStart() {
        super.onStart();



    }


    private ArrayList<PhaseOneModel> parseDbStringValues(String jsonStr) {

        if (jsonStr!=null){
                    try {

                        JSONObject obj = new JSONObject(jsonStr);


                        String result = obj.getString("sync_time");
                      //  Log.i(TAG+"synch time-----------",result);


                            JSONArray m_jArry = obj.getJSONArray("npilistP1");

                            for (int i = 0; i < m_jArry.length(); i++) {
                                JSONObject jo_inside = m_jArry.getJSONObject(i);

                                PhaseOneModel model = new PhaseOneModel();

                                model.setNpiname(jo_inside.getString("npi_synopsis"));
                                  //Log.i(TAG,model.getNpiname());
                                model.setSwlead(jo_inside.getString("sw_lead"));
                                // Log.i(TAG,model.getSwlead());
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
        }
        else {
            CustomTast ct=new CustomTast(getActivity());
            ct.showCustomAlert("Check Pulse Connection",R.drawable.disconnect);
        }


        return npiTrackerModels;
    }


    @Override
    public boolean onClose() {
       /* PhaseOneAdapter ca = (PhaseOneAdapter)listview.getAdapter();
        listview.setAdapter(ca);*/
        listview.clearTextFilter();
        npiTrackerModels.clear();

        Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_PHASEWISE_API_URI, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                String apistring= cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASEWISE_API));
                // Log.i(TAG+"String",apistring);
                npiTrackerModels=  parseDbStringValues(apistring);
                //  Log.i("string",""+apistring);
                adapter=new PhaseOneAdapter(getActivity(),npiTrackerModels);
                listview.setAdapter(adapter);

            } while (cursor.moveToNext());
        }
        return true;
    }
}
