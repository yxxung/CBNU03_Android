package com.example.cbnu_03_android;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ScheduleItem2 implements Serializable{


    //실질적으로 DB에 저장되는 부분
    Long longDate;
    String stringDate;
    String schedule3;
    int month;
    String scheduleKey;
    int isAddedGoogleAPI;

// 객체를 좀더 편하게 이용하기 위해 선언된 멤버변수
    String date;
    String year;
    String dayOfMonth;


    public ScheduleItem2(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    // Generate > Constructor
    public ScheduleItem2(Long date, String stringDate, String schedule3) {
        this.longDate = date;
        this.stringDate =stringDate;
        this.schedule3 = schedule3;
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

    public String getSchedule3() {
        return schedule3;
    }

    public void setSchedule3(String schedule3) {
        this.schedule3 = schedule3;
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

