package com.vericakaranakova.advocacyapp;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.lang.Object;
import java.util.Dictionary;
import java.util.Map;

public class Officials implements Serializable {

    private String currLoc;
    private String position;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String party;
    private String phone;
    private String email;
    private String photoURL;
    private String url;
    private String facebook;
    private String youtube;
    private String twitter;

    public Officials(String cl, String p, String n, String a, String c, String s, String z, String pa, String ph, String e, String pURL, String u, String fb, String yt, String tw) {
        currLoc = cl;
        position = p;
        name = n;
        address = a;
        city = c;
        state = s;
        zip = z;
        party = pa;
        phone = ph;
        email = e;
        photoURL = pURL;
        url = u;
        facebook = fb;
        youtube = yt;
        twitter = tw;
    }

    public String getCurrLoc() {
        return currLoc;
    }
    public String getPosition() {
        return position;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }
    public String getZip() {
        return zip;
    }
    public String getParty() {
        return party;
    }
    public String getPhone() {
        return phone;
    }
    public String getEmail() {
        return email;
    }
    public String getPhotoURL() {
        return photoURL;
    }
    public String getUrl() {
        return url;
    }
    public String getFacebook() {
        return facebook;
    }
    public String getYoutube() {
        return youtube;
    }
    public String getTwitter() {
        return twitter;
    }

    @NonNull
    @Override
    public String toString() {
        return position + ", " + name;
    }
}
