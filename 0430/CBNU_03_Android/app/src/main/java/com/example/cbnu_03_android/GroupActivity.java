package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    TextView nameTextView, purposeTextView, leaderTextView, numberTextView;
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

        db = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        loginUser = intent.getStringExtra("userName");

        nameTextView = (TextView)findViewById(R.id.NameTextView);
        purposeTextView = (TextView)findViewById(R.id.purposeTextView);
        leaderTextView = (TextView)findViewById(R.id.leaderTextView);
        numberTextView = (TextView)findViewById(R.id.numberTextView);

        createGroupBtn = (Button)findViewById(R.id.createGroupBtn);
        exitGroupBtn = (Button)findViewById(R.id.exitGroupBtn);
        findGroupBtn = (Button)findViewById(R.id.findGroupBtn);


        //기본적으로 안보임.
        exitGroupBtn.setVisibility(View.INVISIBLE);

        recyclerView = findViewById(R.id.groupRecyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GroupActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList = new ArrayList<User>();

        recyclerAdapter = new RecyclerAdapter(userList);
        recyclerView.setAdapter(recyclerAdapter);

        db.child("userList").child(loginUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                String selectedGroup = user.getGroup();

                if(selectedGroup != null) {

                    //그룹이 정해져 있다면..
                    //우선 그룹탐색

                    exitGroupBtn.setVisibility(View.VISIBLE);
                    db.child("groupList").child(selectedGroup).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            recyclerAdapter.clearList();
                          Group group = snapshot.getValue(Group.class);

                          ArrayList<String> userList = group.getUserArrayList();

                          //username 기반으로 db에서 User 탐색

                          for(String userName : userList){

                              db.child("userList").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                      User user = snapshot.getValue(User.class);
                                      recyclerAdapter.addItem(user);
                                      recyclerAdapter.notifyDataSetChanged();
                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError error) {

                                  }
                              });
                          }

                            //텍스트 변경
                          nameTextView.setText("그룹명: " + group.getName());
                          purposeTextView.setText("목적: " + group.getPurpose());
                          leaderTextView.setText("리더: " + group.getLeader());
                          numberTextView.setText("인원 수: " + group.userArrayList.size());
                          createGroupBtn.setVisibility(View.INVISIBLE);
                          findGroupBtn.setVisibility(View.INVISIBLE);



                        }

                        //텍스트 변
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }




            }

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

        exitGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                db.child("userList").child(loginUser).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        String searchGroup = user.getGroup();
                        user.setGroup(null);
                        db.child("userList").child(loginUser).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "그룹에서 나왔습니다.", Toast.LENGTH_SHORT).show();
                                createGroupBtn.setVisibility(View.VISIBLE);
                                findGroupBtn.setVisibility(View.VISIBLE);
                                exitGroupBtn.setVisibility(View.INVISIBLE);
                                recyclerAdapter.clearList();

                                nameTextView.setText("그룹에 가입해주세요!");
                                purposeTextView.setText(" ");
                                leaderTextView.setText(" ");
                                numberTextView.setText(" ");


                            }
                        });

                        //그룹 유저리스트에서 삭제.

                        db.child("groupList").child(searchGroup).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Group selectedGroup = snapshot.getValue(Group.class);

                                //원래 한명바꼐 없으면 그냥 그룹 전체삭제.
                                if(selectedGroup.userArrayList.size() == 1){
                                    db.child("groupList").child(selectedGroup.getName()).removeValue();
                                }else{

                                    selectedGroup.userArrayList.remove(loginUser);
                                    //만약.. 나가는 사람이 리더라면?
                                    if(selectedGroup.getLeader().equals(loginUser)){
                                        //가입한 순서대로 리더 양도.
                                        selectedGroup.setLeader(selectedGroup.userArrayList.get(0));
                                    }

                                    db.child("groupList").child(searchGroup).setValue(selectedGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "그룹에서 나왔습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        findGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newIntent = new Intent(getApplicationContext(), FindGroupActivity.class);
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
            itemViewHolder.listUserName.setText("회원이름: "+ user.getName());
            itemViewHolder.listUserContact.setText(user.getPhoneNumber());
            itemViewHolder.listUserId.setText("회원ID: " + user.getId());

        }

        //리스트 추가, 제거
        void addItem(User user){
            userList.add(user);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            private TextView listUserName;
            private TextView listUserContact;
            private TextView listUserId;
            private ImageView img;

            public ItemViewHolder(@NonNull View itemView){
                super(itemView);

                listUserName = itemView.findViewById(R.id.listUserName);
                listUserContact = itemView.findViewById(R.id.listUserContact);
                listUserId = itemView.findViewById(R.id.listUserId);
                leaderTextView = (TextView)findViewById(R.id.leaderTextView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(leaderTextView.getText().toString().split(" ")[1].equals(loginUser)){
                            int pos = getAdapterPosition();
                            if(pos != RecyclerView.NO_POSITION){

                                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);

                                builder.setTitle("선택한 팀원을 내보내겠습니까?");

                                builder.setPositiveButton("내보내기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int pos = getAdapterPosition();
                                        User user = userList.get(pos);
                                        String deleteUserName = user.getId();
                                        if (deleteUserName.equals(loginUser)) {
                                            Toast.makeText(getApplicationContext(), "자기자신은 내보낼 수 없습니다. 나가기를 눌러주세요.", Toast.LENGTH_SHORT).show();
                                            dialogInterface.cancel();
                                        } else {

                                            db.child("userList").child(deleteUserName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    User user = snapshot.getValue(User.class);
                                                    String searchGroup = user.getGroup();
                                                    String deleteUserName = user.getId();
                                                    user.setGroup(null);
                                                    db.child("userList").child(deleteUserName).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getApplicationContext(), "그룹에서 내보냈습니다.", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });

                                                    //그룹 유저리스트에서 삭제.

                                                    db.child("groupList").child(searchGroup).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Group selectedGroup = snapshot.getValue(Group.class);

                                                            //원래 한명바꼐 없으면 그냥 그룹 전체삭제.
                                                            if (selectedGroup.userArrayList.size() == 1) {
                                                                db.child("groupList").child(selectedGroup.getName()).removeValue();
                                                            } else {

                                                                selectedGroup.userArrayList.remove(deleteUserName);

                                                                db.child("groupList").child(searchGroup).setValue(selectedGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(getApplicationContext(), "그룹원목록이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
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
                }

                img = itemView.findViewById(R.id.item_image);

            }

        });
            }
        }

        public void clearList(){
            userList.clear();
        }
    }

}



