package com.example.cbnu_03_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * // DB 에서 해당 id에 대한 같은 date 가진 일정을 모두 Intent를 통해 반환해주는 service
 */

public class GetMemobyDateService extends Service {

    private DatabaseReference db;
    ArrayList<ScheduleItem> resultArray;
    Long longDateTime;

    public GetMemobyDateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return Service.START_STICKY; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            String id = intent.getStringExtra("id");
            String date = intent.getStringExtra("date");

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            try {
                longDateTime = dateFormat.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            db = FirebaseDatabase.getInstance().getReference();

            db.child(id).orderByChild("date").equalTo(longDateTime).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (snapshot.getValue() != null) {
                            int i = 0;
                            //snapshot의 정보, schedule 객체로 변환
                            ScheduleItem scheduleItem = postSnapshot.getValue(ScheduleItem.class);
                            resultArray.add(i, scheduleItem);
//                            resultArray.get(i).setToken();
                            i++;
                        } else {
                            Log.w("FireBaseData", "loadPost:onCancelled");
                        }
                    }

                    Intent returnIntent = new Intent(getApplicationContext(), ViewSchedule.class);

                    /**
                     화면이 띄워진 상황에서 다른 액티비티를 호출하는 것은 문제가없으나,
                     지금은 따로 띄워진 화면이 없는 상태에서 백그라운드에서 실행중인 서비스가 액티비티를 호출하는 상황이다.
                     이러한 경우에는 FLAG_ACTIVITY_NEW_TASK 옵션(플래그)을 사용해줘야 한다.
                     **/
                    returnIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    returnIntent.putExtra("ScheduleList", resultArray);
                    // *** 이제 완성된 인텐트와 startActivity()메소드를 사용하여 MainActivity 액티비티를 호출한다. ***
                    startActivity(returnIntent);
                    stopSelf();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("FireBaseData", "loadPost:onCancelled", error.toException());
                    stopSelf();
                }
            });


            return super.onStartCommand(intent, flags, startId);
        }
    }
}