package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {

    EditText groupNameEdit, groupPurposeEdit;
    Button createBtn;
    String loginUser;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent intent = getIntent();
        loginUser = intent.getStringExtra("userName");



        groupNameEdit = findViewById(R.id.groupNameEdit);
        groupPurposeEdit = findViewById(R.id.groupPurposeEdit);
        createBtn = findViewById(R.id.createButton);
        db = FirebaseDatabase.getInstance().getReference();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String groupName = groupNameEdit.getText().toString();
                String groupPurpose = groupNameEdit.getText().toString();

                Group newGroup = new Group();
                newGroup.setName(groupName);
                newGroup.setPurpose(groupPurpose);
                newGroup.setLeader(loginUser);
                newGroup.getUserArrayList().add(loginUser);

                //이미 그룹 있는경우 처리필요.
                db.child("groupList").child(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Group group = snapshot.getValue(Group.class);

                        if(group == null) {

                            db.child("groupList").child(groupName).setValue(newGroup)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Write was successful!
                                            Toast.makeText( CreateGroupActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            Toast.makeText(CreateGroupActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    });

                            db.child("userList").child(loginUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User user = snapshot.getValue(User.class);

                                    user.setGroup(groupName);
                                    db.child("userList").child(loginUser).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText( CreateGroupActivity.this, "유저 정보를 변경하였습니다..", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.w("FireBaseData", "loadUser:onCancelled");
                                }
                            });


                        }else{
                            Toast.makeText(getApplicationContext(), "이미 있는 그룹입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





//                Intent newIntent = new Intent(getApplicationContext(), GroupActivity.class);
//                startActivity(newIntent);
                    finish();

            }
        });


    }
}