package com.example.cbnu_03_android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Memo {


    //실질적으로 데이터베이스에 들어가는 부분
    String maintext; //메모
    String subtext; //날짜
    Long date;
    int isdone; //메모의 완료여부
    String key;



    public Memo(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public Memo(Long date, String maintext, String subtext, int isdone) {
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


    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



}
