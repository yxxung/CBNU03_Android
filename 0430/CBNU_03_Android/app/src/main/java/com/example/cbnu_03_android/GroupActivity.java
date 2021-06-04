package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    TextView nameTextView, purposeTextView, dateTextView, numberTextView;
    Button createGroupBtn, exitGroupBtn, findGroupBtn;
    String loginUser;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    ArrayList<User> userList;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        loginUser = intent.getStringExtra("userName");

        nameTextView = (TextView)findViewById(R.id.NameTextView);
        purposeTextView = (TextView)findViewById(R.id.purposeTextView);
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        numberTextView = (TextView)findViewById(R.id.numberTextView);

        createGroupBtn = (Button)findViewById(R.id.createGroupBtn);
        exitGroupBtn = (Button)findViewById(R.id.exitGroupBtn);
        findGroupBtn = (Button)findViewById(R.id.findGroupBtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerAdapter = new RecyclerAdapter(userList);
//        recyclerView.setAdapter(recyclerAdapter);
//
//        db.child("userList").child(loginUser).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//
//                String selectedGroup = user.getGroup();
//
//
//            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        createGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(), CreateGroupActivity.class);
                newIntent.putExtra("userName", loginUser);
                startActivity(newIntent);
            }
        });
    }

    //RecyclerAdapter
    class RecyclerAdapter extends RecyclerView.Adapter<GroupActivity.RecyclerAdapter.ItemViewHolder>{

        private List<User> userList;

        public RecyclerAdapter(List<User> userList){
            this.userList = userList;
        }

        @NonNull
        @Override
        public GroupActivity.RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
            boolean attachToRoot; // !
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_user, viewGroup, attachToRoot = false); // !
            return new RecyclerAdapter.ItemViewHolder(view);
        }

        @Override
        public int getItemCount(){
            return userList.size();
        }

        //onBindViewHolder : 데이터를 레이아웃에 어떻게 넣어줄지 정함
        @Override
        public void onBindViewHolder(@NonNull GroupActivity.RecyclerAdapter.ItemViewHolder itemViewHolder, int i){
            User user = userList.get(i);


            //메인텍스트에 Memo의 메인텍스트, 서브텍스트에 Memo의 서브텍스트 삽입
            itemViewHolder.listUserName.setText(user.getName());
            itemViewHolder.listUserContact.setText(user.getPhoneNumber());

        }

        //리스트 추가, 제거
        void addItem(User user){
            userList.add(user);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            private TextView listUserName;
            private TextView listUserContact;
            private ImageView img;

            public ItemViewHolder(@NonNull View itemView){
                super(itemView);

                listUserName = itemView.findViewById(R.id.listUserName);
                listUserContact = itemView.findViewById(R.id.listUserContact);
                img = itemView.findViewById(R.id.item_image);

            }

        }

        public void clearList(){
            userList.clear();
        }
    }

}



