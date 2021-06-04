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
import android.widget.EditText;
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

public class FindGroupActivity extends AppCompatActivity {


    EditText inputText;
    Button searchButton;


    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Group> groupList;
    String loginUser;


    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);


        Intent intent = getIntent();
        loginUser = intent.getStringExtra("userName");

        db = FirebaseDatabase.getInstance().getReference();

        inputText = (EditText)findViewById(R.id.InputText);
        searchButton = (Button)findViewById(R.id.searchButton);


        recyclerView = findViewById(R.id.findGroupRecyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindGroupActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        groupList = new ArrayList<Group>();

        recyclerAdapter = new RecyclerAdapter(groupList);

        recyclerView.setAdapter(recyclerAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchData = inputText.getText().toString();
                recyclerAdapter.clearList();
                recyclerAdapter.notifyDataSetChanged();



                    db.child("groupList").child(searchData).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                try {
                                    Group group = snapshot.getValue(Group.class);
                                    if(group != null) {
                                        recyclerAdapter.addItem(group);
                                        recyclerAdapter.notifyDataSetChanged();
                                        Toast.makeText(getApplicationContext(), "검색완료!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "해당 이름의 그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "해당 이름의 그룹이 없습니다.", Toast.LENGTH_SHORT).show();
                                }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



            }
        });




    }


    //RecyclerAdapter
    class RecyclerAdapter extends RecyclerView.Adapter<FindGroupActivity.RecyclerAdapter.ItemViewHolder> {

        private List<Group> groupList;

        public RecyclerAdapter(List<Group> groupList) {
            this.groupList = groupList;
        }

        @NonNull
        @Override
        public FindGroupActivity.RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            boolean attachToRoot; // !
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_group, viewGroup, attachToRoot = false); // !
            return new FindGroupActivity.RecyclerAdapter.ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
                    return groupList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Group group = groupList.get(position);

            holder.listGroupName.setText("그룹이름: "+ group.getName());
            holder.listGroupLeader.setText("그룹리더: "+ group.getLeader());
            holder.listGroupPurpose.setText("그룹목적: "+ group.getPurpose());
            holder.groupTotalNumber.setText("그룹원수 : " + group.userArrayList.size() +"/ 6");
        }

        //리스트 추가, 제거
        void addItem(Group group) {
            groupList.add(group);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView listGroupName;
            private TextView listGroupPurpose;
            private TextView listGroupLeader;
            private ImageView img;
            private TextView groupTotalNumber;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);


                listGroupName = itemView.findViewById(R.id.listGroupName);
                listGroupLeader = itemView.findViewById(R.id.listGroupLeader);
                listGroupPurpose = itemView.findViewById(R.id.listGroupPurpose);
                groupTotalNumber = itemView.findViewById(R.id.groupTotalNumber);
                img = itemView.findViewById(R.id.item_image);


                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(FindGroupActivity.this);

                            builder.setTitle("선택한 그룹에 들어가시겠습니까?");

                            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    try{

                                        Group group = groupList.get(pos);
                                        db.child("groupList").child(group.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Group selectedGroup = snapshot.getValue(Group.class);
                                                selectedGroup.userArrayList.add(loginUser);
                                                db.child("groupList").child(group.getName()).setValue(selectedGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "그룹에 참가하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                db.child("userList").child(loginUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        User user = snapshot.getValue(User.class);
                                                        user.setGroup(selectedGroup.getName());
                                                        db.child("userList").child(loginUser).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getApplicationContext(), "사용자 정보를 변경하였습니다.", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            }
                                                        });
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

                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "알 수 없는 에", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
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


            }

        }

        public void clearList() {
            groupList.clear();
        }
    }
}

