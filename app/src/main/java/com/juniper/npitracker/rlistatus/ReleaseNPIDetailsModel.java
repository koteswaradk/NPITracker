package com.juniper.npitracker.rlistatus;

/**
 * Created by sarahcs on 2/28/2017.
 */

public class ReleaseNPIDetailsModel {

    private String npiName;
    private int totalRLIs;
    private int RLIsAtRisk;
    private int processFollowed;

    public int getProcessNotFollowed() {
        return processNotFollowed;
    }

    public void setProcessNotFollowed(int processNotFollowed) {
        this.processNotFollowed = processNotFollowed;
    }

    public String getNpiName() {
        return npiName;
    }

    public void setNpiName(String npiName) {
        this.npiName = npiName;
    }

    public int getTotalRLIs() {
        return totalRLIs;
    }

    public void setTotalRLIs(int totalRLIs) {
        this.totalRLIs = totalRLIs;
    }

    public int getRLIsAtRisk() {
        return RLIsAtRisk;
    }

    public void setRLIsAtRisk(int RLIsAtRisk) {
        this.RLIsAtRisk = RLIsAtRisk;
    }

    public int getProcessFollowed() {
        return processFollowed;
    }

    public void setProcessFollowed(int processFollowed) {
        this.processFollowed = processFollowed;
    }

    private int processNotFollowed;

    public ReleaseNPIDetailsModel(String npiName, int totalRLIs, int RLIsAtRisk, int processFollowed, int processNotFollowed){
        this.npiName = npiName;
        this.totalRLIs = totalRLIs;
        this.RLIsAtRisk = RLIsAtRisk;
        this.processFollowed = processFollowed;
        this.processNotFollowed = processNotFollowed;
    }

}
