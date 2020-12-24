package com.juniper.npitracker.activereleases;

/**
 * Created by koteswara on 26/12/16.
 */

public class ActiveRelModel {
    private String relname;
    private String overallpas;
    private String firstpass;
    private String openblocker;
    public String getFirstpass() {
        return firstpass;
    }

    public void setFirstpass(String firstpass) {
        this.firstpass = firstpass;
    }

    public String getOpenblocker() {
        return openblocker;
    }

    public void setOpenblocker(String openblocker) {
        this.openblocker = openblocker;
    }

    public String getOverallpas() {
        return overallpas;
    }

    public void setOverallpas(String overallpas) {
        this.overallpas = overallpas;
    }

    public String getRelname() {
        return relname;
    }

    public void setRelname(String relname) {
        this.relname = relname;
    }


}
