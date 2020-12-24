package com.juniper.npitracker.activereleases;

/**
 * Created by koteswara on 27/10/16.
 */

public class TestRportModel {
    private String releasenumber;
    private String percnetagepass;
    private String totalscriptsplanned;
    private String totalscriptsexecuted;
    private String totalscriptspending;
    private String totalscriptspassed;
    private String falsefailure;
    private String pendingdebug;

    public String getFalsefailure() {
        return falsefailure;
    }

    public void setFalsefailure(String falsefailure) {
        this.falsefailure = falsefailure;
    }

    public String getPendingdebug() {
        return pendingdebug;
    }

    public void setPendingdebug(String pendingdebug) {
        this.pendingdebug = pendingdebug;
    }

    public String getPercnetagepass() {
        return percnetagepass;
    }

    public void setPercnetagepass(String percnetagepass) {
        this.percnetagepass = percnetagepass;
    }

    public String getReleasenumber() {
        return releasenumber;
    }

    public void setReleasenumber(String releasenumber) {
        this.releasenumber = releasenumber;
    }

    public String getTotalscriptsexecuted() {
        return totalscriptsexecuted;
    }

    public void setTotalscriptsexecuted(String totalscriptsexecuted) {
        this.totalscriptsexecuted = totalscriptsexecuted;
    }

    public String getTotalscriptspassed() {
        return totalscriptspassed;
    }

    public void setTotalscriptspassed(String totalscriptspassed) {
        this.totalscriptspassed = totalscriptspassed;
    }

    public String getTotalscriptspending() {
        return totalscriptspending;
    }

    public void setTotalscriptspending(String totalscriptspending) {
        this.totalscriptspending = totalscriptspending;
    }

    public String getTotalscriptsplanned() {
        return totalscriptsplanned;
    }

    public void setTotalscriptsplanned(String totalscriptsplanned) {
        this.totalscriptsplanned = totalscriptsplanned;
    }


}
