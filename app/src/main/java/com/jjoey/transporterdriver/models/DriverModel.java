package com.jjoey.transporterdriver.models;

import java.util.List;

public class DriverModel {

    public String fullName;
    public String emailAddr;
    public String imgURL;
    public String phoneNumber;
    public String dob;
    public String contactName;
    public String contactNumber;
    public int age;
    public List<Trips> numTrips;

    public DriverModel() {
    }

    public DriverModel(String fullName, String emailAddr, String imgURL, String phoneNumber, String dob, String contactName, String contactNumber, int age, List<Trips> numTrips) {
        this.fullName = fullName;
        this.emailAddr = emailAddr;
        this.imgURL = imgURL;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.age = age;
        this.numTrips = numTrips;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Trips> getNumTrips() {
        return numTrips;
    }

    public void setNumTrips(List<Trips> numTrips) {
        this.numTrips = numTrips;
    }
}
