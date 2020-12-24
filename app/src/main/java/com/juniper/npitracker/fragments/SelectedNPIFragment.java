package com.juniper.npitracker.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.juniper.npitracker.R;
import com.juniper.npitracker.SelctedPageAdapter;
import com.juniper.npitracker.adapter.PhaseOneAdapter;
import com.juniper.npitracker.adapter.SelectedNPIAdapter;
import com.juniper.npitracker.adapter.SelectedNPICursorAdapter;
import com.juniper.npitracker.data.NPIProvider;
import com.juniper.npitracker.data.NPITrackerDBHelper;
import com.juniper.npitracker.model.PhaseOneModel;
import com.juniper.npitracker.model.SelectedNPIModel;
import com.juniper.npitracker.util.CustomTast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectedNPIFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectedNPIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectedNPIFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   /* private TabLayout tabLayoutselected;
    private ViewPager viewPagerselected;*/
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listview;
    SearchView searchview;
    LinearLayout layout_selected_list,layout_no_data_display,l2;
    SelectedNPICursorAdapter cadapter;
    SelectedNPIAdapter seletednpiadapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<SelectedNPIModel> seletedNpiModel = new ArrayList<SelectedNPIModel>();
    private OnFragmentInteractionListener mListener;

    public SelectedNPIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectedNPIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectedNPIFragment newInstance(String param1, String param2) {
        SelectedNPIFragment fragment = new SelectedNPIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        View view= inflater.inflate(R.layout.fragment_selected_npi, container, false);
        searchview=(SearchView)view.findViewById(R.id.selectednpi_searchview);
        listview=(ListView) view.findViewById(R.id.selected_npi_list);
        layout_selected_list=(LinearLayout)view.findViewById(R.id.layout_selected_list);
        layout_no_data_display=(LinearLayout)view.findViewById(R.id.layout_no_data_display);
        l2=(LinearLayout)view.findViewById(R.id.l2);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_npi_selected_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        setupSearchView();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

                //return false;
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);
//                mCloseButton.setVisibility(newText.isEmpty() ? View.GONE : View.VISIBLE);

                if (TextUtils.isEmpty(newText)) {
                    listview.clearTextFilter();

                    seletedNpiModel.clear();
                    searchview.clearFocus();
                    Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, null, null, null, null, null);
                    if (!cursor.moveToFirst()){
                        layout_selected_list.setVisibility(View.GONE);
                        layout_no_data_display.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.VISIBLE);
                    }

                    if (cursor.moveToFirst()) {
                        layout_no_data_display.setVisibility(View.GONE);
                        layout_selected_list.setVisibility(View.VISIBLE);
                        seletedNpiModel=  parseDbCursor(cursor);
                        seletednpiadapter=new SelectedNPIAdapter(getActivity(),seletedNpiModel);
                        listview.setAdapter(seletednpiadapter);

                    }
                } else {
                    SelectedNPIAdapter ca = (SelectedNPIAdapter)listview.getAdapter();
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
    public void onResume() {
        super.onResume();
        seletedNpiModel.clear();

        Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, null, null, null, null, null);
        if (!cursor.moveToFirst()){
            layout_selected_list.setVisibility(View.GONE);
            layout_no_data_display.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
        }

        if (cursor.moveToFirst()) {
            layout_no_data_display.setVisibility(View.GONE);
            layout_selected_list.setVisibility(View.VISIBLE);
            seletedNpiModel=  parseDbCursor(cursor);
            seletednpiadapter=new SelectedNPIAdapter(getActivity(),seletedNpiModel);
            listview.setAdapter(seletednpiadapter);

        }
        else{
            //Log.i("hhhhh","uuuu");
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }

    private void refreshContent() {
        seletedNpiModel.clear();
        Cursor cursor = getActivity().getContentResolver().query(NPIProvider.CONTENT_NPI_TRACKER_USER_SELECTED_NPI_URI, null, null, null, null, null);
        if (!cursor.moveToFirst()){
            layout_selected_list.setVisibility(View.GONE);
            layout_no_data_display.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
        }

        if (cursor.moveToFirst()) {
            layout_no_data_display.setVisibility(View.GONE);
            layout_selected_list.setVisibility(View.VISIBLE);
            seletedNpiModel=  parseDbCursor(cursor);
            seletednpiadapter=new SelectedNPIAdapter(getActivity(),seletedNpiModel);
            listview.setAdapter(seletednpiadapter);
            mSwipeRefreshLayout.setRefreshing(false);

        }
    }


    @Override
    public void onStart() {
        super.onStart();


    }
    private ArrayList<SelectedNPIModel> parseDbCursor(Cursor cursor){

                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    SelectedNPIModel model = new SelectedNPIModel();

                    model.setNpiname(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPINAME)));
                    //Log.i(TAG,model.getNpiname());
                    model.setSwlead(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_SWLEAD)));
                    // Log.i(TAG,model.getSwlead());
                    model.setPlm(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PLM)));
                    //  Log.i(TAG,model.getPlm());
                    model.setTestlead(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_TL)));

                    model.setStatus(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_STATUS)));

                    model.setNpiid(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_USER_SELECTED_NPI_ID)));

                    model.setPhase(cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASE)));

                  //   Log.i("pahse Afte check",cursor.getString(cursor.getColumnIndex(NPITrackerDBHelper.KEY_NPITRACKER_PHASE)));
                    seletedNpiModel.add(model);
                    // do what you need with the cursor here
                }

        return seletedNpiModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
