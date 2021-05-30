package com.example.cbnu_03_android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ScheduleItemView extends LinearLayout {

    TextView textView;
    TextView textView2;
    // Generate > Constructor

    public ScheduleItemView(Context context) {
        super(context);

        init(context);
    }

    public ScheduleItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // singer_item.xml을 inflation
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.schedule_item, this, true);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
    }

    public void setSchedule(String schedule) {
        textView.setText(schedule);
    }

    public void setSchedule2(String Schedule2) {
        textView2.setText(Schedule2);
    }
}