package com.example.cbnu_03_android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ViewSchedule extends Activity {

    private DatabaseReference db;
    ScheduleAdapter adapter;
    EditText editText;
    EditText editText2;
    TextView monthdate;
    String defaultId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // position 값을 받아옴(position 날짜, position2 월-1)
        Intent secondIntent = getIntent();
        String position = secondIntent.getStringExtra("position");
        int position2 = secondIntent.getIntExtra("position2", 0);

        /**
         * @Author 최제현
         * DB레퍼런스 추가
         */
        try{
            db = FirebaseDatabase.getInstance().getReference();
        }catch (Exception e){
            e.printStackTrace();
        }
        /**
         *
         */

        // Popup activity의 Title을 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_view_schedule);


        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        monthdate = (TextView) findViewById(R.id.monthday);
        ListView listView = (ListView) findViewById(R.id.listView);

        monthdate.setText("<"+ (position2+1) + "월 " + position + "일 " + " 일정 "+">");
        // 어댑터 안에 데이터 담기
        adapter = new ScheduleAdapter();

        // 리스트 뷰에 어댑터 설정

        listView.setAdapter(adapter);

        // 이벤트 처리 리스너 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScheduleItem item = (ScheduleItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 :"+ position, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView parent, View v, int i, long id){
                Log.d("LONG CLICK", "OnLongClickListener");
                //변경 내용 반영 함수
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "롱클릭", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // 버튼 눌렀을 때 일정, 상세일정이 리스트뷰에 포함되도록 처리
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = "2021";
                int month = position2+1;
                String date = position;
                //db에 저장하기 위한 id기본값 나중에 확장을 위해 추가.
                defaultId = "1";
                //schedule = 일정, schedule = 상세 일정
                String schedule = editText.getText().toString();
                String schedule2 = editText2.getText().toString();


                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"일정을 입력해주세요",Toast.LENGTH_SHORT).show();
                }else {




                    /**
                     * @author 최제현
                     *
                     * 입력한 문자열 Parse후 저장
                     */

                    String makeStringDate = String.format("%s/%d/%s",
                                    year, month, date);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    try {
                        Date addDatetime = dateFormat.parse(makeStringDate);

                        //addDateTime.getTime long으로 Datetime 변경
                        ScheduleItem scheduleItem= new ScheduleItem(addDatetime.getTime(), makeStringDate, schedule, schedule2);
                        scheduleItem.setMonth(month);
                         adapter.addItem(scheduleItem);
                         adapter.notifyDataSetChanged();

                        /**
                         * @author 일정 생성시 자동으로 데이터 베이스 저장
                         */
                        //키 자동생성, 및 삽입
                        String key = db.child(defaultId).push().getKey();
                        //해당 키 위치에 데이터 삽입
                        db.child(defaultId).child(key).setValue(scheduleItem)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        Toast.makeText(ViewSchedule.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        Toast.makeText(ViewSchedule.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                });
                        /**
                         *
                         */



                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                    /**
                     *
                     */



                }


            }
        });
    }

    class ScheduleAdapter extends BaseAdapter {
        ArrayList<ScheduleItem> items = new ArrayList<ScheduleItem>();


        // Generate > implement methods
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(ScheduleItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 뷰 객체 재사용
            ScheduleItemView view = null;
            if (convertView == null) {
                view = new ScheduleItemView(getApplicationContext());
            } else {
                view = (ScheduleItemView) convertView;
            }

            ScheduleItem item = items.get(position);

            view.setSchedule(item.getSchedule());
            view.setSchedule2(item.getSchedule2());

            return view;
        }
    }
}
