package com.example.cbnu_03_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewSchedule2 extends Activity {

    private DatabaseReference db;
    ScheduleAdapter2 adapter2;
    EditText editText3;
    String defaultId;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * @Author 최제현
         * DB레퍼런스 추가
         */
        try{
            db = FirebaseDatabase.getInstance().getReference();
        }catch (Exception e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_view_schedule2);

        TextView tx1 = (TextView)findViewById(R.id.textView6);
        TextView tx2 = (TextView)findViewById(R.id.textView7);

        Intent intent2 = getIntent();

        String schedule = intent2.getExtras().getString("schedule");
        tx1.setText(schedule);

        String schedule2 = intent2.getExtras().getString("schedule2");
        tx2.setText(schedule2);

        String position = intent2.getStringExtra("position");
        int position2 = intent2.getIntExtra("position2", 0);

        String key = intent2.getExtras().getString("key");

        editText3 = (EditText) findViewById(R.id.editText3);
        ListView listView2 = (ListView) findViewById(R.id.listView2);

        adapter2 = new ScheduleAdapter2();

        listView2.setAdapter(adapter2);
        String id = key;
        String date = "2021/" + (position2+1) +  "/"  + position;
        Long longDateTime = null;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            longDateTime = dateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        db = FirebaseDatabase.getInstance().getReference();
            //DB 탐색
            db.child(id).orderByChild("longDate").equalTo(longDateTime).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adapter2.clearList();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot != null) {
                            if (snapshot.getValue() != null) {
                                int i = 0;
                                //snapshot의 정보, schedule 객체로 변환
                                ScheduleItem2 scheduleItem2 = postSnapshot.getValue(ScheduleItem2.class);
                                adapter2.addItem(scheduleItem2);
                                i++;
                            } else {
                                Log.w("FireBaseData", "loadPost:onCancelled");
                            }
                        }
                    }
                    adapter2.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("FireBaseData", "loadPost:onCancelled", error.toException());

                }
            });


        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView parent, View v, int i, long id3){
                Log.d("LONG CLICK", "OnLongClickListener");
                //변경 내용 반영 함수
                adapter2.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "롱클릭", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSchedule2.this);

                builder.setTitle("선택한 일정을 삭제하겠습니까?");

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
                    /**
                     * @author 최제현
                     * @param dialog
                     * @param id3
                     *
                     * 해당 일를 삭제하기 위해 DB접근
                     */

                    @Override
                    public void onClick(DialogInterface dialog, int id3)
                    {
                        db.child(key).child(adapter2.items.get(i).getScheduleKey()).removeValue();
                        adapter2.items.remove(i);
                        adapter2.notifyDataSetChanged();
                    }
                });

                /**
                 * 일정 삭제
                 */

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                return true;
            }
        });
        // 버튼 눌렀을 때 일정, 상세일정이 리스트뷰에 포함되도록 처리
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = "2021";
                int month = position2+1;
                String date = position;

                //db에 저장하기 위한 id기본값 나중에 확장을 위해 추가.
                defaultId = key;
                String schedule3 = editText3.getText().toString();

                if(editText3.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"댓글을 입력해주세요",Toast.LENGTH_SHORT).show();
                }else {




                    /**
                     * @author 최제현
                     *
                     * 입력한 문자열 Parse후 저장
                     */


                    try {
                        String makeStringDate = String.format("%s/%d/%s",
                                year, month, date);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

                        Date addDatetime = dateFormat.parse(makeStringDate);
                        //db 키생성
                        String key = db.child(defaultId).push().getKey();
                        //addDateTime.getTime long으로 Datetime 변경*/
                        ScheduleItem2 scheduleItem2 = new ScheduleItem2(addDatetime.getTime(), makeStringDate, schedule3);
                        scheduleItem2.setMonth(month);
                        scheduleItem2.setScheduleKey(key);
                        scheduleItem2.setIsAddedGoogleAPI(0);
                        editText3.setText(null);

                        /**
                         * @author 일정 생성시 자동으로 데이터 베이스 저장
                         */
                        //해당 키 위치에 데이터 삽입
                        db.child(defaultId).child(key).setValue(scheduleItem2)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        Toast.makeText(ViewSchedule2.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        Toast.makeText(ViewSchedule2.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                });
                        /**
                         * DB저장종료
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

        TextView exit;
        exit = (TextView) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    class ScheduleAdapter2 extends BaseAdapter {
        ArrayList<ScheduleItem2> items = new ArrayList<ScheduleItem2>();


        // Generate > implement methods
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(ScheduleItem2 item) {
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
            ScheduleItemView2 view2 = null;
            if (convertView == null) {
                view2 = new ScheduleItemView2(getApplicationContext());
            } else {
                view2 = (ScheduleItemView2) convertView;
            }

            ScheduleItem2 item = items.get(position);

            view2.setSchedule3(item.getSchedule3());


            return view2;
        }

        public void clearList(){
            items.clear();
        }
    }
}