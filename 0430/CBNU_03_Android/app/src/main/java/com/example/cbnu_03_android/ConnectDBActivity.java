package com.example.cbnu_03_android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ConnectDBActivity extends Activity {

    private DatabaseReference db;
    Button dbTestBtn;
    Button dbAddBtn;
    EditText textMemo;
    EditText textDate;
    Memo newMemo;
    TextView mainStatus;
    TextView mainResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_db);

        db = FirebaseDatabase.getInstance().getReference();
        dbTestBtn = findViewById(R.id.button_DB_Test);
        dbAddBtn = findViewById(R.id.button_add_to_DB);
        textDate = findViewById(R.id.input_date);
        textMemo = findViewById(R.id.input_memo);
        mainResult = findViewById(R.id.DB_main_result);
        mainStatus = findViewById(R.id.DB_main_status);


        dbAddBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                int isDone = 0;
                String id = "1";
                String addMemo = textMemo.getText().toString();
                String addDate = textDate.getText().toString();
                newMemo = new Memo(addMemo, addDate, isDone);

                db.child("memo").child(id).setValue(newMemo)
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



    }
}
