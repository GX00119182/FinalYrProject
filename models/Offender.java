package com.example.gavin_000.fareevasionappnew.models;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by X00119182 on 14/02/2018.
 */
@IgnoreExtraProperties
public class Offender {
    private String fname;
    private String lname;
    private String address;
    private String dateOfBirth;
    private String phoneNo;
    private String email;
    private String stopName;

    public Offender()
    {
    }

    public Offender(String fname, String lname, String address, String dob, String phoneNo, String email, String stopName)
    {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.dateOfBirth = dob;
        this.phoneNo = phoneNo;
        this.email = email;
        this.stopName = stopName;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
