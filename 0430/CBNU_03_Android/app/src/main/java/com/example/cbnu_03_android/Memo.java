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


    //Memo 객체를 좀더 편하게 이용하기 위해 선언된 멤버변수
    String stringDate;
    String parsedDate[];
    String year;
    String month;
    String dayOfMonth;

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }


    //변수들을 더 이용하기 쉽게 parsing하여 변수에 저장해줌.
    public void setToken(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.stringDate = dateFormat.format(date);
        parsedDate = this.stringDate.split("/");
        this.year = parsedDate[0];
        this.month = parsedDate[1];
        this.dayOfMonth = parsedDate[2];
    }



}
