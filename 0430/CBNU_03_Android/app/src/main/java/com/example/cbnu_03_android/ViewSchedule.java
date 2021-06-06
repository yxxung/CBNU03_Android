package com.example.cbnu_03_android;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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


public class ViewSchedule extends Activity {

    private DatabaseReference db;
    ScheduleAdapter adapter;
    EditText editText;
    EditText editText2;
    TextView monthdate;

    String id;
    ImageView button;
    ImageButton imagebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // position 값을 받아옴(position 날짜, position2 월-1)
        Intent secondIntent = getIntent();
        String position = secondIntent.getStringExtra("position");
        int position2 = secondIntent.getIntExtra("position2", 0);
        String id = secondIntent.getStringExtra("userName");

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

        monthdate.setText( (position2+1) + "월 " + position + "일 " + " 일정" );
        // 어댑터 안에 데이터 담기
        adapter = new ScheduleAdapter();

        // 리스트 뷰에 어댑터 설정

        listView.setAdapter(adapter);

        /**
         * @author 최제현
         * DB접근을 위해 String format 변경
         * DB접근 후 Adapter의 ArrayList<ScheduleItem>에 항목 추가.
         */

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
                    adapter.clearList();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (snapshot.getValue() != null) {
                            int i = 0;
                            //snapshot의 정보, schedule 객체로 변환
                            ScheduleItem scheduleItem = postSnapshot.getValue(ScheduleItem.class);
                            adapter.addItem(scheduleItem);
                            i++;
                        } else {
                            Log.w("FireBaseData", "loadPost:onCancelled");
                        }
                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("FireBaseData", "loadPost:onCancelled", error.toException());
                }
            });

        /**
         * DB 활동 종료.
         */


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView parent, View v, int i, long id2){
                Log.d("LONG CLICK", "OnLongClickListener");
                //변경 내용 반영 함수
                adapter.notifyDataSetChanged();

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSchedule.this);

                builder.setTitle("선택한 일정을 삭제하겠습니까?");

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener(){
                    /**
                     * @author 최제현
                     * @param dialog
                     * @param id
                     *
                     * 해당 일를 삭제하기 위해 DB접근
                     */

                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        db.child(ViewSchedule.this.id).child(adapter.items.get(i).getScheduleKey()).removeValue();
                        adapter.items.remove(i);
                        adapter.notifyDataSetChanged();

                    }
                });

                /**
                 * 일정 삭제
                 */

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id2)
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
        button = (ImageView) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = "2021";
                int month = position2+1;
                String date = position;
                //db에 저장하기 위한 id기본값 나중에 확장을 위해 추가.
                ViewSchedule.this.id = id;
                //schedule = 일정, schedule2 = 상세 일정
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

                            //db 키생성
                            String key = db.child(ViewSchedule.this.id).push().getKey();
                            //addDateTime.getTime long으로 Datetime 변경
                            ScheduleItem scheduleItem = new ScheduleItem(addDatetime.getTime(), makeStringDate, schedule, schedule2);
                            scheduleItem.setMonth(month);
                            scheduleItem.setScheduleKey(key);
                            scheduleItem.setIsAddedGoogleAPI(0);
                            editText.setText(null);
                            editText2.setText(null);

                        /**
                         * @author 일정 생성시 자동으로 데이터 베이스 저장
                         */
                        //해당 키 위치에 데이터 삽입
                        db.child(ViewSchedule.this.id).child(key).setValue(scheduleItem)
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
        // 이벤트 처리 리스너 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position4, long id) {
                ScheduleItem item = (ScheduleItem) adapter.getItem(position4);
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewSchedule.this);

                builder.setTitle( item.schedule + "의 상세 일정");
                builder.setMessage(item.schedule + "의 상세 일정을 확인 및 작성하시겠습니까?");
                builder.setPositiveButton("확인 / 작성", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent intent2 = new Intent(ViewSchedule.this, ViewSchedule2.class);
                        intent2.putExtra("schedule",item.schedule);
                        intent2.putExtra("schedule2",item.schedule2);
                        intent2.putExtra("position",position);
                        intent2.putExtra("position2", position2);
                        intent2.putExtra("key", item.scheduleKey);
                        startActivityForResult(intent2, 1);

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();

                alertDialog.show();

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

        public void clearList(){
            items.clear();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

}
