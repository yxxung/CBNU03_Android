package com.example.cbnu_03_android;

import android.content.Intent;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScheduleItem implements Serializable {

    //실질적으로 DB에 저장되는 부분
    Long longDate;
    String stringDate;
    String schedule;
    String schedule2;
    int month;
    String scheduleKey;
    int isAddedGoogleAPI;

// 객체를 좀더 편하게 이용하기 위해 선언된 멤버변수

    String date;
    String year;
    String dayOfMonth;


    public ScheduleItem(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    // Generate > Constructor
    public ScheduleItem(Long date, String stringDate, String schedule, String schedule2) {
        this.longDate = date;
        this.stringDate =stringDate;
        this.schedule = schedule;
        this.schedule2 = schedule2;
    }


    public int getMonth() {
        return month;
    }

    public void setNum(int num){
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }


    // Generate > Getter and Setter
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSchedule2() {
        return schedule2;
    }

    public void setSchedule2(String schedule2) {
        this.schedule2 = schedule2;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public Long getLongDate() {
        return longDate;
    }

    public void setLongDate(Long longDate) {
        this.longDate = longDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getScheduleKey() {
        return scheduleKey;
    }

    public void setScheduleKey(String scheduleKey) {
        this.scheduleKey = scheduleKey;
    }


    public int getIsAddedGoogleAPI() {
        return isAddedGoogleAPI;
    }

    public void setIsAddedGoogleAPI(int isAddedGoogleAPI) {
        this.isAddedGoogleAPI = isAddedGoogleAPI;
    }

}
