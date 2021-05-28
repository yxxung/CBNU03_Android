package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    Button btnAdd;

    private DatabaseReference db;

    List<Memo> memoList;

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


        /**
         * @author 최제현
         * DB접근 후 Adapter의 List<Memo>에 항목 추가.
         */

        //date를 받아와야함.
        String id = "memo"+"1";

        db = FirebaseDatabase.getInstance().getReference();


        db.child(id).orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recyclerAdapter.clearList();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (snapshot.getValue() != null) {
                        int i = 0;
                        //snapshot의 정보, schedule 객체로 변환
                        Memo memo = postSnapshot.getValue(Memo.class);
                        recyclerAdapter.addItem(memo);
                        i++;
                    } else {
                        Log.w("FireBaseData", "loadPost:onCancelled");
                    }
                }
               recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("FireBaseData", "loadPost:onCancelled", error.toException());
            }
        });

        /**
         * DB 활동 종료.
         */


        //새로운 메모 작성
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MemoActivity.this, com.example.cbnu_03_android.AddActivity.class);
                startActivityForResult(intent, 0); //입력한 텍스트를 MemoActivity로 가져옴
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

//            //받아온 데이터로 메모만듦
//            com.example.cbnu_03_android.Memo memo = new com.example.cbnu_03_android.Memo(strMain, strSub, 0);
//            //adapter의 addItem으로 생성된 메모 추가
//            recyclerAdapter.addItem(memo);
//            recyclerAdapter.notifyDataSetChanged();
        }
    }

    //RecyclerAdapter
    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{

        private List<com.example.cbnu_03_android.Memo> listdata;

        public RecyclerAdapter(List<com.example.cbnu_03_android.Memo> listdata){
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
            boolean attachToRoot; // !
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, attachToRoot = false); // !
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount(){
            return listdata.size();
        }

        //onBindViewHolder : 데이터를 레이아웃에 어떻게 넣어줄지 정함
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i){
            com.example.cbnu_03_android.Memo memo = listdata.get(i);

            //메인텍스트에 Memo의 메인텍스트, 서브텍스트에 Memo의 서브텍스트 삽입
            itemViewHolder.maintext.setText(memo.getMaintext());
            itemViewHolder.subtext.setText(memo.getSubtext());

            //이미지뷰 0,1일 때 색깔 체인지
            if (memo.getIsdone() == 0){
                itemViewHolder.img.setBackgroundColor(Color.LTGRAY);
            }else{
                itemViewHolder.img.setBackgroundColor(Color.GREEN);
            }
        }

        //리스트 추가, 제거
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

                /**
                 * @author 최제현
                 * recyclerview 내에서, 메모를 삭제하기 위한 listner
                 * longclick시 발
                 */

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){


                            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);

                            builder.setTitle("선택한 메모 삭제하겠습니까?");

                            builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    try{
                                        Memo memo = listdata.get(pos);
                                        db.child("memo"+"1").child(memo.getKey()).removeValue();
                                        listdata.remove(pos);
                                        recyclerAdapter.notifyDataSetChanged();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Toast.makeText(MemoActivity.this, "알 수 없는 에", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }

                        return true;

                    }
                });
                /**
                 * 삭제 완료
                 */
            }

        }

        public void clearList(){
            listdata.clear();
        }
    }

}