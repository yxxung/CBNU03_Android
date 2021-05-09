package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    Button btnAdd;

    List<com.example.cbnu_03_android.Memo> memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        memoList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MemoActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerAdapter(memoList);
        recyclerView.setAdapter(recyclerAdapter);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //새로운 메모 작성
                Intent intent = new Intent(MemoActivity.this, com.example.cbnu_03_android.AddActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    //startActivityForResult로 실행한 액티비티가 끝났을 때 여기서 데이터를 받음
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //startActivityForResult에 넣은 requestCode와 같으면 다음 조건문을 수행
        if(requestCode == 0) {
            //데이터를 받아온다
            //@Nullable Intent data로 아까 넣은 Intent가 들어옴
            //data type에 맞춰 데이터를 가져옴
            String strMain = data.getStringExtra("main");
            String strSub = data.getStringExtra("sub");

            //받아온 데이터로 메모만듦
            com.example.cbnu_03_android.Memo memo = new com.example.cbnu_03_android.Memo(strMain, strSub, 0);
            //adapter의 addItem으로 생성된 메모 추가
            recyclerAdapter.addItem(memo);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{

        private List<com.example.cbnu_03_android.Memo> listdata;

        public RecyclerAdapter(List<com.example.cbnu_03_android.Memo> listdata){
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
            boolean attachToRoot; // 주의
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, attachToRoot = false); // 주의
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount(){
            return listdata.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i){
            com.example.cbnu_03_android.Memo memo = listdata.get(i);

            itemViewHolder.maintext.setText(memo.getMaintext());
            itemViewHolder.subtext.setText(memo.getSubtext());

            if (memo.getIsdone() == 0){
                itemViewHolder.img.setBackgroundColor(Color.LTGRAY);
            }else{
                itemViewHolder.img.setBackgroundColor(Color.GREEN);
            }
        }

        void addItem(com.example.cbnu_03_android.Memo memo){
            listdata.add(memo);
        }

        void removeItem(int position){
            listdata.remove(position);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            private TextView maintext;
            private TextView subtext;
            private ImageView img;

            public ItemViewHolder(@NonNull View itemView){
                super(itemView);

                maintext = itemView.findViewById(R.id.item_maintext);
                subtext = itemView.findViewById(R.id.item_subtext);
                img = itemView.findViewById(R.id.item_image);
            }
        }
    }

}