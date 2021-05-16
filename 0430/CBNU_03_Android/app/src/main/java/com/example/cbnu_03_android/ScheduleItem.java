package com.example.cbnu_03_android;

import android.content.Intent;

public class ScheduleItem {
    String schedule;
    String schedule2;
    int month;
    String date;

    // Generate > Constructor
    public ScheduleItem(int month, String date, String schedule, String schedule2) {
        this.date = date;
        this.month = month;
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

    // Generate > toString() : 아이템을 문자열로 출력

    @Override
    public String toString() {
        return "SingerItem{" +
                "month='"+ month + '\''+
                ", date='"+ date + '\''+
                ", schedule='" + schedule + '\'' +
                ", schedule2='" + schedule2 + '\'' +
                '}';
    }
}
