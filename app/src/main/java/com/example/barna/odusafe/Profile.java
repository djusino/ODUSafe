package com.example.barna.odusafe;

import java.util.Random;
import java.util.StringTokenizer;


/**
 * Created by Claus on 11/21/2017.
 */

public class Profile {

    private int id;
    private String fullName;
    private String firstName;
    private String lastName;
    private String Address;
    private String phoneNumber;
    private String email;

    public Profile(){
        this.setid(0);
        this.setFullName(null);
        this.setFirstName(null);
        this.setLastName(null);
        this.setEmail(null);
        this.setAddress(null);
        this.setPhoneNumber(null);
    }

    public Profile(String full, String first, String last, String address, String number , String email){

        Random r = new Random();
        int x = r.nextInt(10000000);
        this.setid(x);
        this.setFullName(full);
        this.setFirstName(first);
        this.setLastName(last);
        this.setEmail(address);
        this.setAddress(number);
        this.setPhoneNumber(email);
    }

    public String getFullName() {return fullName; }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return Address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public int getid() { return id;}
    public void setid(int id) {
        this.id = id;
    }


    public void setFullName(String s) { this.fullName = s; }

    public void setFirstName(String s) {
        StringTokenizer tokens = new StringTokenizer(s, " ");
        String firstName = tokens.nextToken();
        this.firstName = firstName;
    }

    public void setLastName(String s) {
        StringTokenizer tokens = new StringTokenizer(s, " ");
        String firstName = tokens.nextToken();
        String secondName = tokens.nextToken();
        this.lastName = secondName;
    }

    public void setEmail(String s) { this.email = s; }

    public void setAddress(String s) {
        this.Address = s;
    }

    public void setPhoneNumber(String s) {
        this.phoneNumber = s;
    }



}
