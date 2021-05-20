package com.example.cbnu_03_android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ConnectDBActivity extends Activity {

    private DatabaseReference db;
    Button dbTestBtn;
    Button dbAddBtn;
    EditText textMemo;
    EditText textSubMemo;
    DatePicker datePicker;
    Memo newMemo;
    TextView mainStatus;
    TextView mainResult;
    ArrayList<Memo> resultArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_db);
        resultArray = new ArrayList<Memo>();
        db = FirebaseDatabase.getInstance().getReference();
        dbTestBtn = findViewById(R.id.button_DB_Test);
        dbAddBtn = findViewById(R.id.button_add_to_DB);
        datePicker = findViewById(R.id.input_date);
        textMemo = findViewById(R.id.input_memo);
        textSubMemo = findViewById(R.id.input_submemo);
        mainResult = findViewById(R.id.DB_main_result);
        mainStatus = findViewById(R.id.DB_main_status);


        dbAddBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int isDone = 0;
                String id = "1";
                String addMemo = textMemo.getText().toString();
                String addSubMemo = textSubMemo.getText().toString();

                String addDate = String.format("%d/%d/%d",
                        datePicker.getYear(),
                        datePicker.getMonth()+1,
                        datePicker.getDayOfMonth());
                newMemo = new Memo(addDate ,addMemo, addSubMemo, isDone);


                //키 자동생성, 및 삽입
                String key = db.child(id).push().getKey();
                //해당 키 위치에 데이터 삽입
                db.child(id).child(key).setValue(newMemo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(ConnectDBActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(ConnectDBActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

            }
        });

        dbTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = "1";

                db.child(id).orderByChild("date").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot : snapshot.getChildren()){
                            if (snapshot.getValue() != null) {
                                //snapshot의 정보, memo 객체로 변환
                                Memo memo = postSnapshot.getValue(Memo.class);
                                resultArray.add(memo);
                            } else {
                                Toast.makeText(ConnectDBActivity.this, "no data", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("FireBaseData", "loadPost:onCancelled", error.toException());
                    }
                });
            }
        });



    }
}
