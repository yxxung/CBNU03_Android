package com.example.project10_3calculate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * @author 최제현
 * @date 2021/05/01
 */
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("SecondActivity");

        Intent intent = getIntent();
        String action = (intent.getStringExtra("action"));
        Button btnReturn = (Button) findViewById(R.id.btnReturn);

        int temp = 0;
        int num1 = intent.getIntExtra("num1", 0);
        int num2 = intent.getIntExtra("num2", 0);

        // mainactivity에서 보낸 엑션 판별
        switch (action){
            case "+":
                temp = num1 + num2;
                break;

            case "-":
                temp = num1 - num2;
                break;

            case "*":
                temp = num1 * num2;
                break;

            case "/":
                temp = num1 / num2;
                break;

            default:
                Toast.makeText(getApplicationContext(), "something went wrong...", Toast.LENGTH_SHORT).show();
                break;
        }

        //outintent 에 보내려면 final 해야함....
        final int result = temp;

        btnReturn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //mainActivity로 보내자.
                Intent outIntent = new Intent(getApplicationContext(),
                        MainActivity.class);
                outIntent.putExtra("result", result);
                //setResult 사용하면, mainactivity의 onActivityResult 메소드 실행
                setResult(RESULT_OK, outIntent);
                //엑티비티 종료
                finish();

            }
        });



    }
}
