package com.example.cbnu_03_android;

import java.util.ArrayList;

public class Group {

    public String name;
    public String purpose;
    public ArrayList<String> userArrayList;
    public String leader;

    public Group(){
        userArrayList = new ArrayList<String>();
    }


    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public ArrayList<String> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<String> userList) {
        this.userArrayList = userList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
