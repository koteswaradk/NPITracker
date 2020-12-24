package com.juniper.npitracker.activereleases;

/**
 * Created by koteswara on 18/01/17.
 */

public class FunctionDetailsModel {
    private String number;
    private String arrivaldate;
    private String reportedin;
    private String state;
    private String hit;
    private String problemlevel;

    public String getArrivaldate() {
        return arrivaldate;
    }

    public void setArrivaldate(String arrivaldate) {
        this.arrivaldate = arrivaldate;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProblemlevel() {
        return problemlevel;
    }

    public void setProblemlevel(String problemlevel) {
        this.problemlevel = problemlevel;
    }

    public String getReportedin() {
        return reportedin;
    }

    public void setReportedin(String reportedin) {
        this.reportedin = reportedin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
