package com.example.cbnu_03_android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ScheduleItemView2 extends LinearLayout {

    TextView textView3;
    // Generate > Constructor

    public ScheduleItemView2(Context context) {
        super(context);

        init(context);
    }

    public ScheduleItemView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    // singer_item.xml을 inflation
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.schedule_item2, this, true);

        textView3 = (TextView) findViewById(R.id.textView3);
    }

    public void setSchedule3(String Schedule3) {
        textView3.setText(Schedule3);
    }
}