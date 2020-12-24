package com.juniper.npitracker.rlistatus;

import java.util.ArrayList;

/**
 * Created by koteswara on 15/02/17.
 */

public class ReleaseListModel {
    private String relname;
    private String swlead;
    private ArrayList<ReleaseNPIDetailsModel> npis;

    public ReleaseListModel(){
        npis = new ArrayList<ReleaseNPIDetailsModel>();
    }

    public ArrayList<ReleaseNPIDetailsModel> getNpis() {
        return npis;
    }

    public void setNpis(ArrayList<ReleaseNPIDetailsModel> npis) {
        this.npis = npis;
    }

    public String getHwlead() {
        return hwlead;
    }

    public void setHwlead(String hwlead) {
        this.hwlead = hwlead;
    }

    private String hwlead;

    public String getSwlead() {
        return swlead;
    }

    public void setSwlead(String swlead) {
        this.swlead = swlead;
    }

    public String getRelname() {
        return relname;
    }

    public void setRelname(String relname) {
        this.relname = relname;
    }

    public void addToNPIList(String npiName, int totalRLIs, int RLIsAtRisk, int processFollowed, int processNotFollowed){

        ReleaseNPIDetailsModel npi = new ReleaseNPIDetailsModel(npiName, totalRLIs, RLIsAtRisk, processFollowed, processNotFollowed);
        this.npis.add(npi);
    }



}
