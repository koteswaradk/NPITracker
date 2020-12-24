package com.juniper.npitracker.model;

/**
 * Created by koteswara on 06/02/17.
 */

public class PhaseFourModel {
    private String npiname;
    private String swlead;
    private String testlead;
    private String plm;
    private String npiid;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    public String getNpiid() {
        return npiid;
    }

    public void setNpiid(String npiid) {
        this.npiid = npiid;
    }
    public String getTestlead() {
        return testlead;
    }

    public void setTestlead(String testlead) {
        this.testlead = testlead;
    }

    public String getSwlead() {
        return swlead;
    }

    public void setSwlead(String swlead) {
        this.swlead = swlead;
    }

    public String getPlm() {
        return plm;
    }

    public void setPlm(String plm) {
        this.plm = plm;
    }

    public String getNpiname() {
        return npiname;
    }

    public void setNpiname(String npiname) {
        this.npiname = npiname;
    }
}
