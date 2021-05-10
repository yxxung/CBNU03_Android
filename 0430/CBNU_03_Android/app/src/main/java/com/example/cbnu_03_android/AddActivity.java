package com.example.cbnu_03_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    EditText edtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edtText = findViewById(R.id.edtMemo);

        //완료버튼
        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edtText.getText().toString(); //edittext에 입력된 텍스트 가져옴

                if(str.length() > 0) {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");

                    String substr = sdf.format(date);

                    //AddActivity에서 입력받은 문자열을 MainActivity로 보내는 작업을 한다
                    Intent intent = new Intent();
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
                finish();
            } //finish로 바로 종료료
        });
   }
}