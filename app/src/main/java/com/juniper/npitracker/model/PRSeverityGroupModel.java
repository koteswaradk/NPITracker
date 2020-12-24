package com.juniper.npitracker.model;

import java.io.Serializable;

/**
 * Created by sarahcs on 2/9/2017.
 */

public class PRSeverityGroupModel implements Serializable {

    public PRSeverityGroupModel(){

    }

    public PRSeverityGroupModel(String groupName, int open, int verified, int closed, int total, int last7Days,
                                int blockersOpen, int blockersVerified,
                                int L1L2Open, int L1L2Verified,
                                int L3L4Open, int L3L4Verified){
        this.groupName = groupName;
        this.open = open;
        this.verified = verified;
        this.closed = closed;
        this.total = total;
        this.last7Days = last7Days;

        this.blockersOpen = blockersOpen;
        this.blockersVerified = blockersVerified;
        this.L1L2Open = L1L2Open;
        this.L1L2Verified = L1L2Verified;
        this.L3L4Open = L3L4Open;
        this.L3L4Verified = L3L4Verified;

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String groupName;

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    private int open;

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    private int verified;

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    private int closed;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    private int total;

    public int getLast7Days() {
        return last7Days;
    }

    public void setLast7Days(int last7Days) {
        this.last7Days = last7Days;
    }

    private int last7Days;

    public int getBlockersOpen() {
        return blockersOpen;
    }

    public void setBlockersOpen(int blockersOpen) {
        this.blockersOpen = blockersOpen;
    }

    private int blockersOpen;

    public int getBlockersVerified() {
        return blockersVerified;
    }

    public void setBlockersVerified(int blockersVerified) {
        this.blockersVerified = blockersVerified;
    }

    private int blockersVerified;

    public int getL1L2Open() {
        return L1L2Open;
    }

    public void setL1L2Open(int l1L2Open) {
        L1L2Open = l1L2Open;
    }

    private int L1L2Open;

    public int getL1L2Verified() {
        return L1L2Verified;
    }

    public void setL1L2Verified(int l1L2Verified) {
        L1L2Verified = l1L2Verified;
    }

    private int L1L2Verified;

    public int getL3L4Open() {
        return L3L4Open;
    }

    public void setL3L4Open(int l3L4Open) {
        L3L4Open = l3L4Open;
    }

    private int L3L4Open;

    public int getL3L4Verified() {
        return L3L4Verified;
    }

    public void setL3L4Verified(int l3L4Verified) {
        L3L4Verified = l3L4Verified;
    }

    private int L3L4Verified;

}
