package com.cookandroid.project10_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("메인 액티비티");

        final RadioButton radioButton1 = (RadioButton) findViewById(R.id.radioButton1);

        Button btnNewActivity = (Button) findViewById(R.id.btnNewActivity);
        btnNewActivity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;


                if (radioButton1.isChecked() == true)
                    intent = new Intent(getApplicationContext(),
                            SecondActivity.class);
                else
                    intent = new Intent(getApplicationContext(),
                            ThirdActivity.class);

                startActivity(intent);
            }
        });
    }

}
