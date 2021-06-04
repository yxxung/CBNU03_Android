package com.example.cbnu_03_android;

import java.util.ArrayList;

public class Group {

    public String name;
    public String purpose;
    public ArrayList<String> userList;
    public String leader;

    public Group(){
        userList = new ArrayList<String>();
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
        return userList;
    }

    public void setUserArrayList(ArrayList<String> userList) {
        this.userList = userList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
