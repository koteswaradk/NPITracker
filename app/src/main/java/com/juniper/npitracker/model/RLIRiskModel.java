package com.juniper.npitracker.model;

/**
 * Created by sarahcs on 2/8/2017.
 */

public class RLIRiskModel implements java.io.Serializable{

    public String getRLIId() {
        return RLIId;
    }

    public void setRLIId(String RLIId) {
        this.RLIId = RLIId;
    }

    private String RLIId;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String reason;

    public RLIRiskModel(){
        // default constructor
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    private String synopsis;

    public RLIRiskModel(String RLIId, String synopsis, String reason){
        this.RLIId = RLIId;
        this.reason = reason;
        this.synopsis = synopsis;
    }
}
