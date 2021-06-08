package com.example.cbnu_03_android;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public int YEAR_COUNT=0;
    public int MONTH_COUNT = now_month();
    private TextView tvDate;
    private TextView userName;
    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    private Button left_press, right_press, goMemo, loginButton, groupButton;
    private String loginUser;

    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDate = (TextView)findViewById(R.id.tv_date);
        userName = (TextView)findViewById(R.id.userName);
        gridView = (GridView)findViewById(R.id.gridview);


        // 오늘에 날짜를 세팅 해준다.
        left_press = (Button)findViewById(R.id.left_press);
        right_press = (Button)findViewById(R.id.right_press);


        goMemo = (Button)findViewById(R.id.goMemo);
        loginButton = (Button)findViewById(R.id.loginBtn);
        groupButton = (Button)findViewById(R.id.getGroupBtn);
        //default group 안보
        groupButton.setVisibility(View.INVISIBLE);

        db = FirebaseDatabase.getInstance().getReference();

        /**
         * @author 최제현임
         * 로그인 유저 인식
         * UI 깔끔하게 고쳐주세요. 일단 임시로 보이고 안보이고 설정하였습니다.
         */

        loginUser = "Guest";

        Intent intent = getIntent();
        if(intent.getStringExtra("userName") != null){

            loginUser = intent.getStringExtra("userName");

        }

        userName.setText("로그인유저: "+ loginUser);

        if(!loginUser.equals("Guest")){
            loginButton.setText("logout");
            groupButton.setVisibility(View.VISIBLE);
        }

        /**
         * 로그인 인식 끝
         */


        left_press.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MONTH_COUNT=MONTH_COUNT-1;
                new_month(MONTH_COUNT);
            }
        });

        right_press.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                MONTH_COUNT=MONTH_COUNT+1;
                new_month(MONTH_COUNT);
            }
        });

        goMemo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                db.child("userList").child(loginUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        if(user != null){
                            if(user.getGroup() != null){
                                Intent intent = new Intent(getApplicationContext(), MemoActivity.class);
                                intent.putExtra("userName", loginUser);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "로그인 후 그룹에 가입해야 이용할 수 있는 서비스입니다."
                                        , Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "로그인 후 그룹에 가입해야 이용할 수 있는 서비스입니다."
                                    , Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(loginButton.getText().toString().equals("LOGIN")){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    //로그아웃 버튼 클릭시
                    loginUser = "Guest";
                    userName.setText("로그인유저: "+ loginUser);
                    groupButton.setVisibility(View.INVISIBLE);
                    loginButton.setText("LOGIN");
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                }


            }
        });


        groupButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(loginUser.equals("Guest")){
                    Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                    intent.putExtra("userName", loginUser);
                    startActivity(intent);
                }

            }
        });

        new_month(MONTH_COUNT);

    }

    private int now_month(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        return month + 1;
    }

    private void new_month(int cnt)
    {

        long now = System.currentTimeMillis();
        final Date date = new Date(now);


        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        Calendar yea = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("MMMM", Locale.US);
        Calendar cal = Calendar.getInstance();

        int month_sum_count=MONTH_COUNT-now_month();

        cal.add ( cal.MONTH, + month_sum_count );


        tvDate.setText(df.format(cal.getTime()));

        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        mCal.set(Integer.parseInt(curYearFormat.format(date)), cnt - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

        //이명국 수정
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, ViewSchedule.class);
                //Toast.makeText(MainActivity.this, dayList.get(position).toString(), Toast.LENGTH_SHORT).show();
                intent.putExtra("position", dayList.get(position));
                intent.putExtra("position2", cal.get(Calendar.MONTH));
                intent.putExtra("userName", loginUser);
                startActivityForResult(intent, 1);

            }
        });
        //이명국_수정 끝


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