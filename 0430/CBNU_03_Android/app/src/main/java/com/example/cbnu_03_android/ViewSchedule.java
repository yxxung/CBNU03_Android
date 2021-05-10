package com.example.cbnu_03_android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ViewSchedule extends Activity {

    ListView listView1;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItem;

    EditText editText1;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Popup의 Title을 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_view_schedule);
        Intent intent = getIntent();

        editText1 = findViewById(R.id.editText1);
        button1 = findViewById(R.id.button1);

        listItem = new ArrayList<String>();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText1.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"계획을 입력해주세요",Toast.LENGTH_LONG).show();
                }else {
                    listItem.add(editText1.getText().toString());
                    adapter.notifyDataSetChanged();
                    editText1.setText("");
                }
            }
        });
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,listItem);
        listView1 = findViewById(R.id.listview1);
        listView1.setAdapter(adapter);


        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView parent, View v, int i, long id){
                Log.d("LONG CLICK", "OnLongClickListener");
                listItem.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int i, long id){
                Log.d("CLICK", "OnClickListener");
            }
        });

    }
}
