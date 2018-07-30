package com.example.intern.sessionsapp;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Session {

    public static String JSONDateformat = "yyyy-MM-dd'T'HH:mm:ssXXX";

    @SerializedName("SessionId")
    private double sessionID;

    @SerializedName("ActivityStartDate")
    private String startDate;

    @SerializedName("ActivityEndDate")
    private String endDate;

    @SerializedName("Subject")
    private String subject;

    @SerializedName("ActivityType")
    private String activityType;

    @SerializedName("Location")
    private String location;

    @SerializedName("Owner")
    private String owner;

    @SerializedName("OwnerEmail")
    private String ownerEmail;

    @SerializedName("OwnerContactNumber")
    private String ownerContactNumber;

    @SerializedName("PrimaryContactName")
    private String primaryContactName;

    @SerializedName("LeadName")
    private String leadName;

    @SerializedName("AccountName")
    private String accountName;

    @SerializedName("OpportunityName")
    private String opportunityName;

    @SerializedName("Description")
    private String description;

    @SerializedName("invitees")
    private List<Invitee> invitees;


    /*---------------Helper Method------------------*/

    public boolean isSessionUpcoming() {

        //Converting Start date string to object
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(JSONDateformat);
            date = format.parse(startDate);
        } catch (Exception e) {

            e.printStackTrace();
        }

        Date currentDate = new Date();

        return date.after(currentDate) || date.equals(currentDate);


    }

    /*----------------GETTERS-----------------------*/

    public double getSessionID() {
        return sessionID;
    }

    public String getStartDate() {
        return startDate;
    }


    public String getSubject() {
        return subject;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getActivityType() {
        return activityType;
    }


    public String getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getOwnerContactNumber() {
        return ownerContactNumber;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public String getLeadName() {
        return leadName;
    }


    public String getAccountName() {
        return accountName;
    }


    public String getOpportunityName() {
        return opportunityName;
    }


    public String getDescription() {
        return description;
    }

    public List<Invitee> getInvitees() {
        return invitees;
    }


}
