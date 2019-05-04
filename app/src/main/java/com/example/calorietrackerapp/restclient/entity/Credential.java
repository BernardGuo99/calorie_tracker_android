package com.example.calorietrackerapp.restclient.entity;

import java.util.Date;

public class Credential {

    private String credentialId;

    private String userName;

    private String passwordHash;

    private Date signUpDate;

    private AppUser userId;

    public Credential(String credentialId) {
        this.credentialId = credentialId;
    }

    public Credential() {
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }

    public AppUser getUserId() {
        return userId;
    }

    public void setUserId(AppUser userId) {
        this.userId = userId;
    }
}
