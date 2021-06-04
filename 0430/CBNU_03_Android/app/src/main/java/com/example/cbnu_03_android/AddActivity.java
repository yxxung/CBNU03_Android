package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    EditText edtText;
    private DatabaseReference db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();


        edtText = findViewById(R.id.edtMemo);

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

        //완료버튼
        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edtText.getText().toString(); //edittext에 입력된 텍스트 가져옴

                if(str.length() > 0) {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");

                    String substr = sdf.format(date);


                    /**
                     * @author 일정 생성시 자동으로 데이터 베이스 저장
                     */
                    //해당 키 위치에 데이터 삽입
                    Intent intent = getIntent();
                    String memoId = intent.getStringExtra("memoId");
                    String author = intent.getStringExtra("userName");
                    String key = db.child(memoId).push().getKey();
                    Memo newMemo = new Memo();
                    newMemo.setMaintext(str);
                    newMemo.setSubtext(substr);
                    newMemo.setDate(date.getTime());
                    newMemo.setKey(key);
                    newMemo.setAuthor(author);

                    db.child(memoId).child(key).setValue(newMemo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    Toast.makeText( AddActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    Toast.makeText(AddActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            });
                    /**
                     * DB저장종료
                     */

                    //AddActivity에서 입력받은 문자열을 MainActivity로 보내는 작업을 한다
                    intent = new Intent();
                    //Intent에 putExtra로 데이터를 넣음. key값과 value값을 넣어줌
                    intent.putExtra("main", str);
                    intent.putExtra("sub", substr);
                    //setResult에 intent를 넣어주면 onActivityResult에서 이 intent를 받음
                    setResult(RESULT_OK, intent);

                    finish();
                }
            }
        });

        //취소버튼
        findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } //finish로 바로 종료
        });

   }
}