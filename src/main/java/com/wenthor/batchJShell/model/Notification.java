package com.wenthor.batchJShell.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private String userMail;
    private String fileName;
    private LocalDateTime jobStartTime;
    private LocalDateTime jobEndTime;

    public Notification(){}
    public Notification(String userMail, String fileName, LocalDateTime jobStartTime, LocalDateTime jobEndTime) {
        this.userMail = userMail;
        this.fileName = fileName;
        this.jobStartTime = jobStartTime;
        this.jobEndTime = jobEndTime;
    }

    //Getter & Setter:
    public String getUserMail() {
        return userMail;
    }
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getJobStartTime() {
        return jobStartTime;
    }
    public void setJobStartTime(LocalDateTime jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public LocalDateTime getJobEndTime() {
        return jobEndTime;
    }
    public void setJobEndTime(LocalDateTime jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

}
