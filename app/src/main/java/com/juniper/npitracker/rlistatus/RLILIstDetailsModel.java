package com.juniper.npitracker.rlistatus;

/**
 * Created by koteswara on 15/02/17.
 */

public class RLILIstDetailsModel {
    private String rliid;
    private String synopsis;
    private String processfollowed;
    private String testExecutedPercentage;
    private String testPassedPercentage;
    private String testScriptsPercentage;

    public String getNpiprogram() {
        return npiprogram;
    }

    public void setNpiprogram(String npiprogram) {
        this.npiprogram = npiprogram;
    }

    private String npiprogram;
    public String getProcessfollowed() {
        return processfollowed;
    }

    public void setProcessfollowed(String processfollowed) {
        this.processfollowed = processfollowed;
    }

    public String getRliid() {
        return rliid;
    }

    public void setRliid(String rliid) {
        this.rliid = rliid;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTestExecutedPercentage() {
        return testExecutedPercentage;
    }

    public void setTestExecutedPercentage(String testExecutedPercentage) {
        this.testExecutedPercentage = testExecutedPercentage;
    }

    public String getTestPassedPercentage() {
        return testPassedPercentage;
    }

    public void setTestPassedPercentage(String testPassedPercentage) {
        this.testPassedPercentage = testPassedPercentage;
    }

    public String getTestScriptsPercentage() {
        return testScriptsPercentage;
    }

    public void setTestScriptsPercentage(String testScriptsPercentage) {
        this.testScriptsPercentage = testScriptsPercentage;
    }


}
