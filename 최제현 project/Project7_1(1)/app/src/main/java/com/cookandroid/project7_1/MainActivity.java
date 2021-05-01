package com.cookandroid.project7_1;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    EditText editText1;
    ImageView imageContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("2019038106");
        imageContext =(ImageView)findViewById(R.id.imageContext);
        editText1 = (EditText)findViewById(R.id.EditText1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemTurn:
                imageContext.setRotation(Integer.parseInt(editText1.getText().toString()));
                return true;
            case R.id.item1:
                item.setChecked(true);
                imageContext.setImageResource(R.drawable.hanra);
                return true;

            case R.id.item2:
                item.setChecked(true);
                imageContext.setImageResource(R.drawable.hanra);//사진추가
                return true;

            case R.id.item3:
                item.setChecked(true);
                imageContext.setImageResource(R.drawable.hanra);//사진추가
                return true;
        }
        return false;
    }

}
