package com.example.cbnu_03_android;

public class Memo {



    String maintext; //메모
    String subtext; //날짜
    String date;
    int isdone; //메모의 완료여부

    public Memo(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public Memo(String date,String maintext, String subtext, int isdone) {
        this.date = date;
        this.maintext = maintext;
        this.subtext = subtext;
        this.isdone = isdone;
    }

    public Memo(String maintext, String subtext, int isdone) {
        this.maintext = maintext;
        this.subtext = subtext;
        this.isdone = isdone;
    }

    public String getMaintext() {
        return maintext;
    }

    public void setMaintext(String maintext) {
        this.maintext = maintext;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public int getIsdone() {
        return isdone;
    }

    public void setIsdone(int isdone) {
        this.isdone = isdone;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
