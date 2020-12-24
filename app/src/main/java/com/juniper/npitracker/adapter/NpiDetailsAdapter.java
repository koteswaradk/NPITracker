package com.juniper.npitracker.adapter;

/**
 * Created by sarahcs on 2/7/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.juniper.npitracker.R;
import com.juniper.npitracker.model.NPIDetailsModel;
import com.juniper.npitracker.model.RLIRiskModel;

import java.util.ArrayList;

public class NpiDetailsAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    private NPIDetailsModel npiDetails;
    private int[][] mGroups = null;


    private ArrayList<ArrayList<RLIRiskModel>> mChilds = null;

    public NpiDetailsAdapter(Context context, NPIDetailsModel npiDetails) {
        mContext = context;
        this.npiDetails = npiDetails;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        mGroups = new int[][]{
                {R.string.rlis_at_risk, npiDetails.getRLIsAtRisk()},
                {R.string.rlis_missing_fs_approval,npiDetails.getRLIsMissingFSApproval()},
                {R.string.rlis_missing_utp_approval,npiDetails.getRLIsMissingUTPApproval()},
                {R.string.rlis_missing_ftp_approval,npiDetails.getRLIsMissingFTPApproval()},
                {R.string.rlis_missing_fcc_approval,npiDetails.getRLIsMissingFCCApproval()},
                {R.string.rlis_with_rejected_fcc,npiDetails.getRLIsWithRejectedFCC()}
        };

        mChilds = new ArrayList<ArrayList<RLIRiskModel>>();
        mChilds.add(npiDetails.getRLIsAtRiskArray());
        mChilds.add(npiDetails.getRLIsMissingFSApprovalArray());
        mChilds.add(npiDetails.getRLIsMissingUTPApprovalArray());
        mChilds.add(npiDetails.getRLIsMissingFTPApprovalArray());
        mChilds.add(npiDetails.getRLIsMissingFCCApprovalArray());
        mChilds.add(npiDetails.getRLIsWithRejectedFCCArray());

    }

    public void getData(){

    }

    @Override
    public int getGroupCount() {
        return mGroups.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups[groupPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.test_status_outer_list_item, parent, false);
        }

        final TextView listHeading = (TextView) convertView.findViewById(R.id.list_heading);
        listHeading.setText(mGroups[groupPosition][0]);

        final TextView listHeadingValue = (TextView) convertView.findViewById(R.id.list_heading_value);
        listHeadingValue.setText(String.valueOf(mGroups[groupPosition][1]));


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChilds.get(groupPosition).size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChilds.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.test_status_inner_list_item, parent, false);
        }

        final TextView RLIIndex = (TextView) convertView.findViewById(R.id.rli_index_number);
        RLIIndex.setText(String.valueOf(childPosition+1)+ ".");

        final TextView RLIId = (TextView) convertView.findViewById(R.id.rli_id);
        RLIId.setText(mChilds.get(groupPosition).get(childPosition).getRLIId());

        final TextView RLISynopsis = (TextView) convertView.findViewById(R.id.rli_synopsis);
        RLISynopsis.setText(mChilds.get(groupPosition).get(childPosition).getSynopsis());

        final LinearLayout reasonLayout = (LinearLayout) convertView.findViewById(R.id.reason_layout);

        if(mChilds.get(groupPosition).get(childPosition).getReason()!= null){
            final TextView RLIReason = (TextView) convertView.findViewById(R.id.rli_reason);
            RLIReason.setText(mChilds.get(groupPosition).get(childPosition).getReason());
        }
        else{
            reasonLayout.setVisibility(LinearLayout.GONE);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
