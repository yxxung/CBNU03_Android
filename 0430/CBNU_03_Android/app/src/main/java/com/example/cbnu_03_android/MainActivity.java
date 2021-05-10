package com.example.cbnu_03_android;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public int MONTH_COUNT = 0;
    private TextView tvDate;
    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    private Button left_press, right_press, goMemo;
    private Button btnApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDate = (TextView)findViewById(R.id.tv_date);
        gridView = (GridView)findViewById(R.id.gridview);

        //api 연동 엑티비티로 가기위한 버튼
        Button btnApi = (Button) findViewById(R.id.api_share);

        // 오늘에 날짜를 세팅 해준다.
        left_press = (Button)findViewById(R.id.left_press);
        right_press = (Button)findViewById(R.id.right_press);

        goMemo = (Button)findViewById(R.id.goMemo);


        left_press.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MONTH_COUNT=MONTH_COUNT-1;
                //new_month(MONTH_COUNT);
            }
        });

        right_press.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MONTH_COUNT=MONTH_COUNT+1;
                //new_month(MONTH_COUNT);
            }
        });

        goMemo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                startActivity(intent);
            }
        });

        new_month(MONTH_COUNT);
        /**
         * @author 최제현
         * @date 2021/05/05
         *
         * 버튼 클릭시, api연동 엑티비티로 이동
         */

        btnApi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),
                        CalendarAPIActivity.class);

                startActivity(intent);


            }
        });
    }

    private void new_month(int cnt)
    {

        long now = System.currentTimeMillis();
        final Date date = new Date(now);


        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        final SimpleDateFormat curMonthFormat2 = new SimpleDateFormat("MMMM", Locale.US);

        tvDate.setText(curMonthFormat2.format(date));

        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

        //0508 이명국 수정 _ 기존 alertDialog-> Gridview 클릭시 ViewSchedule activity(popup)로 이동
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, ViewSchedule.class);
                startActivityForResult(intent, 1);
            }
        });
        //0508 이명국_수정 끝


    }

    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }

    }

    private class GridAdapter extends BaseAdapter {

        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));



            mCal = Calendar.getInstance();

            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);

            if (position<7)
            {
                holder.tvItemGridView.setTextColor(Color.parseColor("#9966CC"));
            }
            else if((position+1)%7==0)
            {
                holder.tvItemGridView.setTextColor(Color.parseColor("#3300CC"));
            }
            else if(position%7==0)
            {
                holder.tvItemGridView.setTextColor(Color.parseColor("#FF0033"));
            }

            if (sToday.equals(getItem(position))) {
                holder.tvItemGridView.setTextColor(Color.parseColor("#663399"));
            }



            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

}