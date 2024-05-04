package com.example.betterhealthapp;

public class UserProfileInfo {

    private String userID;
    private String fullName, phoneNumber, nextOfKinName, nextOfKinPhoneNumber, address, surname,  idNumber, email;

    public UserProfileInfo() {
        // Default constructor required for Firebase
    }

    public UserProfileInfo(String userID, String fullName, String email, String phoneNumber,
                           String nextOfKinName, String nextOfKinPhoneNumber, String address,
                           String surname, String idNumber) {
        this.userID = userID;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nextOfKinName = nextOfKinName;
        this.nextOfKinPhoneNumber = nextOfKinPhoneNumber;
        this.address = address;
        this.surname = surname;

        this.idNumber = idNumber;
    }

    // Getters and setters for all fields

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }



    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNextOfKinName() {
        return nextOfKinName;
    }

    public void setNextOfKinName(String nextOfKinName) {
        this.nextOfKinName = nextOfKinName;
    }

    public String getNextOfKinPhoneNumber() {
        return nextOfKinPhoneNumber;
    }

    public void setNextOfKinPhoneNumber(String nextOfKinPhoneNumber) {
        this.nextOfKinPhoneNumber = nextOfKinPhoneNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
