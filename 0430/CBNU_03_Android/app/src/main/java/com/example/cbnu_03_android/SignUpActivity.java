package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    Button signUpButton;
    EditText idText, pwText, pwCheckText, nameText, contactText;

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        idText = (EditText)findViewById(R.id.idEdit);
        pwText = (EditText)findViewById(R.id.pwInputText);
        pwCheckText = (EditText)findViewById(R.id.pwCheckEdit);
        nameText = (EditText)findViewById(R.id.nameEdit);
        contactText = (EditText)findViewById(R.id.pnEdit);
        signUpButton = (Button)findViewById(R.id.signUpButton);

        db = FirebaseDatabase.getInstance().getReference();


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {

        String id = idText.getText().toString();


        db.child("userList").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user == null){
                    String id = idText.getText().toString();
                    String password = pwText.getText().toString();
                    String name = nameText.getText().toString();
                    String phoneNumber = contactText.getText().toString();
                    String passwordCheck = pwCheckText.getText().toString();
                    User newUser = new User(id, password, name, phoneNumber);
                    if(id.length() > 0 && password.length() > 0 && passwordCheck.length() > 0){
                        if(password.equals(passwordCheck)){
                            db.child("userList").child(id).setValue(newUser);
                            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else{
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않음!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "동일한 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}

