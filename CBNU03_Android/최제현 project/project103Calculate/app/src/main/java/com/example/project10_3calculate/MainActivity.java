package com.example.project10_3calculate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("메인 엑티비티");

        EditText edtNum1 = (EditText) findViewById(R.id.edtNum1);
        EditText edtNum2 = (EditText) findViewById(R.id.edtNum2);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        Button btnCal = (Button) findViewById(R.id.btnCal);

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),
                        SecondActivity.class);

                switch (radioGroup.getCheckedRadioButtonId()){

                    case R.id.radioAdd:
                        intent.putExtra("action", "+");
                        break;
                    case R.id.radioSub:
                        intent.putExtra("action", "-");
                        break;
                    case R.id.radioDiv:
                        intent.putExtra("action", "/");
                        break;
                    case R.id.radioMul:
                        intent.putExtra("action", "*");
                        break;

                }

                //parseInt 해줘야 int로 return되어서 전달된다..
                intent.putExtra("num1", Integer.parseInt(edtNum1.getText().toString()));
                intent.putExtra("num2", Integer.parseInt(edtNum2.getText().toString()));

                //return값 받기위해 사용
                startActivityForResult(intent, 0);
            }



        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            int result = data.getIntExtra("result", 0);
            Toast.makeText(getApplicationContext(), "result :" + result, Toast.LENGTH_SHORT).show();
        }


    }
}