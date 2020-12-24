package com.juniper.npitracker.model;

import java.util.ArrayList;

/**
 * Created by sarahcs on 2/8/2017.
 */

public class NPIDetailsModel implements java.io.Serializable{

    private String NPIId;
    private String NPIName;
    private String status;

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    private String statusReason;
    private String versionNo;

    private int actualExecuted;
    private int totalExecution;
    private int actualPassRate;
    private int actualScripted;
    private int totalScripted;
    private int executionPercentage;
    private int passRatePercentage;
    private int scriptedPercentage;

    public int getClBlocker() {
        return clBlocker;
    }

    public void setClBlocker(int clBlocker) {
        this.clBlocker = clBlocker;
    }

    public int getClClosed() {
        return clClosed;
    }

    public void setClClosed(int clClosed) {
        this.clClosed = clClosed;
    }

    public int getClNotClosed() {
        return clNotClosed;
    }

    public void setClNotClosed(int clNotClosed) {
        this.clNotClosed = clNotClosed;
    }

    public int getCl1() {
        return cl1;
    }

    public void setCl1(int cl1) {
        this.cl1 = cl1;
    }

    public int getCl2() {
        return cl2;
    }

    public void setCl2(int cl2) {
        this.cl2 = cl2;
    }

    public int getCl3() {
        return cl3;
    }

    public void setCl3(int cl3) {
        this.cl3 = cl3;
    }

    public int getCl4() {
        return cl4;
    }

    public void setCl4(int cl4) {
        this.cl4 = cl4;
    }

    private int clBlocker;
    private int clClosed;
    private int clNotClosed;
    private int cl1;
    private int cl2;
    private int cl3;
    private int cl4;

    private int testProgress;
    private int RLIsAtRisk;
    private int RLIsMissingFSApproval;
    private int RLIsMissingUTPApproval;
    private int RLIsMissingFTPApproval;
    private int RLIsMissingFCCApproval;
    private int RLIsWithRejectedFCC;

    private ArrayList<RLIRiskModel> RLIsAtRiskArray;

    public ArrayList<RLIRiskModel> getRLIsMissingUTPApprovalArray() {
        return RLIsMissingUTPApprovalArray;
    }

    public void setRLIsMissingUTPApprovalArray(ArrayList<RLIRiskModel> RLIsMissingUTPApprovalArray) {
        this.RLIsMissingUTPApprovalArray = RLIsMissingUTPApprovalArray;
    }

    public ArrayList<RLIRiskModel> getRLIsMissingFSApprovalArray() {
        return RLIsMissingFSApprovalArray;
    }

    public void setRLIsMissingFSApprovalArray(ArrayList<RLIRiskModel> RLIsMissingFSApprovalArray) {
        this.RLIsMissingFSApprovalArray = RLIsMissingFSApprovalArray;
    }

    public ArrayList<RLIRiskModel> getRLIsMissingFTPApprovalArray() {
        return RLIsMissingFTPApprovalArray;
    }

    public void setRLIsMissingFTPApprovalArray(ArrayList<RLIRiskModel> RLIsMissingFTPApprovalArray) {
        this.RLIsMissingFTPApprovalArray = RLIsMissingFTPApprovalArray;
    }

    public ArrayList<RLIRiskModel> getRLIsMissingFCCApprovalArray() {
        return RLIsMissingFCCApprovalArray;
    }

    public void setRLIsMissingFCCApprovalArray(ArrayList<RLIRiskModel> RLIsMissingFCCApprovalArray) {
        this.RLIsMissingFCCApprovalArray = RLIsMissingFCCApprovalArray;
    }

    public ArrayList<RLIRiskModel> getRLIsWithRejectedFCCArray() {
        return RLIsWithRejectedFCCArray;
    }

    public void setRLIsWithRejectedFCCArray(ArrayList<RLIRiskModel> RLIsWithRejectedFCCArray) {
        this.RLIsWithRejectedFCCArray = RLIsWithRejectedFCCArray;
    }

    private ArrayList<RLIRiskModel> RLIsMissingFSApprovalArray;
    private ArrayList<RLIRiskModel> RLIsMissingUTPApprovalArray;
    private ArrayList<RLIRiskModel> RLIsMissingFTPApprovalArray;
    private ArrayList<RLIRiskModel> RLIsMissingFCCApprovalArray;
    private ArrayList<RLIRiskModel> RLIsWithRejectedFCCArray;
    private ArrayList<PRSeverityGroupModel> prSeverityGroups;

    public NPIDetailsModel(){

        RLIsAtRiskArray = new ArrayList<RLIRiskModel>();
        RLIsMissingFSApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsMissingUTPApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsMissingFTPApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsMissingFCCApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsWithRejectedFCCArray = new ArrayList<RLIRiskModel>();

        prSeverityGroups = new ArrayList<PRSeverityGroupModel>();
    }

    public NPIDetailsModel(String NPIId, String NPIName,String versionNo){

        this.NPIId = NPIId;
        this.NPIName = NPIName;
        this.versionNo = versionNo;

        RLIsAtRiskArray = new ArrayList<RLIRiskModel>();
        RLIsMissingFSApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsMissingUTPApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsMissingFTPApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsMissingFCCApprovalArray = new ArrayList<RLIRiskModel>();
        RLIsWithRejectedFCCArray = new ArrayList<RLIRiskModel>();

        prSeverityGroups = new ArrayList<PRSeverityGroupModel>();
    }

    /*
     * GETTER METHODS
     */
    public String getNPIId(){
        return NPIId;
    }

    public String getNPIName(){
        return NPIName;
    }

    public String getStatus() {
        return status;
    }

    public String getVersionNumber(){
        return versionNo;
    }

    public int getTestProgress(){
        return testProgress;
    }

    public int getRLIsAtRisk(){
        return RLIsAtRisk;
    }

    public int getRLIsMissingFSApproval(){
        return RLIsMissingFSApproval;
    }

    public int getRLIsMissingUTPApproval(){
        return RLIsMissingUTPApproval;
    }

    public int getRLIsMissingFTPApproval(){
        return RLIsMissingFTPApproval;
    }

    public int getRLIsMissingFCCApproval(){
        return RLIsMissingFCCApproval;
    }

    public int getRLIsWithRejectedFCC(){
        return RLIsWithRejectedFCC;
    }

    public int getActualExecuted() {
        return actualExecuted;
    }

    public int getTotalExecution() {
        return totalExecution;
    }

    public int getActualPassRate() {
        return actualPassRate;
    }

    public int getActualScripted() {
        return actualScripted;
    }

    public int getTotalScripted() {
        return totalScripted;
    }

    public int getExecutionPercentage() {
        return executionPercentage;
    }

    public int getPassRatePercentage() {
        return passRatePercentage;
    }

    public int getScriptedPercentage() {
        return scriptedPercentage;
    }

    /*
     * SETTER METHODS
     */
    public void setNPIId(String NPIId){
        this.NPIId = NPIId;
    }

    public void setNPIName(String NPIName){
        this.NPIName = NPIName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setVersionNumber(String versionNo){
        this.versionNo = versionNo;
    }

    public void setTestProgress(int testProgress){
        this.testProgress = testProgress;
    }

    public void setRLIsAtRisk(int RLIsAtRisk){
        this.RLIsAtRisk = RLIsAtRisk;
    }

    public void setRLIsMissingFSApproval(int RLIsMissingFSApproval){
        this.RLIsMissingFSApproval = RLIsMissingFSApproval;
    }

    public void setRLIsMissingUTPApproval(int RLIsMissingUTPApproval){
        this.RLIsMissingUTPApproval = RLIsMissingUTPApproval;
    }

    public void setRLIsMissingFTPApproval(int RLIsMissingFTPApproval){
        this.RLIsMissingFTPApproval = RLIsMissingFTPApproval;
    }

    public void setRLIsMissingFCCApproval(int RLIsMissingFCCApproval){
        this.RLIsMissingFCCApproval = RLIsMissingFCCApproval;
    }

    public void setRLIsWithRejectedFCC(int RLIsWithRejectedFCC){
        this.RLIsWithRejectedFCC = RLIsWithRejectedFCC;
    }

    public void setActualExecuted(int actualExecuted) {
        this.actualExecuted = actualExecuted;
    }

    public void setTotalExecution(int totalExecution) {
        this.totalExecution = totalExecution;
    }

    public void setActualPassRate(int actualPassRate) {
        this.actualPassRate = actualPassRate;
    }

    public void setActualScripted(int actualScripted) {
        this.actualScripted = actualScripted;
    }

    public void setTotalScripted(int totalScripted) {
        this.totalScripted = totalScripted;
    }

    public void setExecutionPercentage(int executionPercentage) {
        this.executionPercentage = executionPercentage;
    }

    public void setPassRatePercentage(int passRatePercentage) {
        this.passRatePercentage = passRatePercentage;
    }

    public void setScriptedPercentage(int scriptedPercentage) {
        this.scriptedPercentage = scriptedPercentage;
    }


    /*
     * Handling the ArrayList of RLIsAtRiskArray
     */

    public ArrayList<RLIRiskModel> getRLIsAtRiskArray() {
        return RLIsAtRiskArray;
    }

    public String getRLIRiskReason(String RLIId){

        for(RLIRiskModel rli: RLIsAtRiskArray){
            if(rli.getRLIId().equals(RLIId)){
                return rli.getReason();
            }
        }

        return null;
    }

    public RLIRiskModel getRLIAtPos(int index){
        return RLIsAtRiskArray.get(index);
    }

    public void addRLIRisksToList(String RLIId, String synopsis, String reason){
        RLIRiskModel rliRisk = new RLIRiskModel(RLIId, synopsis, reason);
        this.RLIsAtRiskArray.add(rliRisk);
    }

    /*
     * Handling the ArrayList of FS Approval
     */

    public RLIRiskModel getRLIMissingFSapprovalAtPos(int index){
        return RLIsMissingFSApprovalArray.get(index);
    }

    public void addRLIsMissingFSApprovalToList(String RLIId, String synopsis){
        RLIRiskModel rliRisk = new RLIRiskModel(RLIId, synopsis, null);
        this.RLIsMissingFSApprovalArray.add(rliRisk);
    }

    /*
     * Handling the ArrayList of UTP Approval
     */

    public RLIRiskModel getRLIMissingUTPapprovalAtPos(int index){
        return RLIsMissingUTPApprovalArray.get(index);
    }

    public void addRLIsMissingUTPApprovalToList(String RLIId, String synopsis){
        RLIRiskModel rliRisk = new RLIRiskModel(RLIId, synopsis, null);
        this.RLIsMissingUTPApprovalArray.add(rliRisk);
    }

    /*
     * Handling the ArrayList of FTP Approval
     */

    public RLIRiskModel getRLIMissingFTPapprovalAtPos(int index){
        return RLIsMissingFTPApprovalArray.get(index);
    }

    public void addRLIsMissingFTPApprovalToList(String RLIId, String synopsis){
        RLIRiskModel rliRisk = new RLIRiskModel(RLIId, synopsis, null);
        this.RLIsMissingFTPApprovalArray.add(rliRisk);
    }

    /*
     * Handling the ArrayList of UTP Approval
     */

    public RLIRiskModel getRLIMissingFCCapprovalAtPos(int index){
        return RLIsMissingFCCApprovalArray.get(index);
    }

    public void addRLIsMissingFCCApprovalToList(String RLIId, String synopsis){
        RLIRiskModel rliRisk = new RLIRiskModel(RLIId, synopsis, null);
        this.RLIsMissingFCCApprovalArray.add(rliRisk);
    }

    /*
     * Handling the ArrayList of FCC Rejected
     */

    public RLIRiskModel getRLIsRejectedFCCAtPos(int index){
        return RLIsWithRejectedFCCArray.get(index);
    }

    public void addRLIsRejectedFCCToList(String RLIId, String synopsis){
        RLIRiskModel rliRisk = new RLIRiskModel(RLIId, synopsis, null);
        this.RLIsWithRejectedFCCArray.add(rliRisk);
    }

    /*
     * Handling the ArrayList of PR Severities
     */

    public PRSeverityGroupModel getPRSeverityAtPos(int index){
        return prSeverityGroups.get(index);
    }

    public void addPRSeverityToList(String groupName, int open, int verified, int closed, int total, int last7Days,
                                    int blockersOpen, int blockersVerified,
                                    int L1L2Open, int L1L2Verified,
                                    int L3L4Open, int L3L4Verified){
        PRSeverityGroupModel prSeverityGroup = new PRSeverityGroupModel(groupName, open, verified, closed, total, last7Days,
                blockersOpen, blockersVerified, L1L2Open, L1L2Verified, L3L4Open, L3L4Verified);
        this.prSeverityGroups.add(prSeverityGroup);
    }
    public ArrayList<PRSeverityGroupModel> getPrSeverityGroups() {
        return prSeverityGroups;
    }

    public void setPrSeverityGroups(ArrayList<PRSeverityGroupModel> prSeverityGroups) {
        this.prSeverityGroups = prSeverityGroups;
    }
}
