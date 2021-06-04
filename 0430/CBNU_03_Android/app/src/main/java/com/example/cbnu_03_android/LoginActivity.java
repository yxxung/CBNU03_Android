package com.example.cbnu_03_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    Button loginButton, signUpButton;
    EditText idInputText, pwInputText;
    private DatabaseReference db;
    public User loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        signUpButton = (Button)findViewById(R.id.signUpButton);
        loginButton = (Button)findViewById(R.id.loginButton);
        idInputText = (EditText)findViewById(R.id.idInputText);
        pwInputText = (EditText)findViewById(R.id.pwInputText);
        db = FirebaseDatabase.getInstance().getReference();


        signUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                login();

            }
        });
    }

    public void login(){
        final String id = idInputText.getText().toString();
        final String password = pwInputText.getText().toString();


        if(id.length() > 0 && password.length() > 0){
            db.child("userList").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);

                    if(user == null){
                        Toast.makeText(getApplicationContext(), "로그인 정보가 정확하지 않습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else if(user.getPassword().equals(password)){
                        loginUser = user;
                        Toast.makeText(getApplicationContext(), String.format("%s님 가입을 환영합니다.", loginUser.id), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("userName", loginUser.id);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(this, "비밀번호가 일치하지 않음!", Toast.LENGTH_SHORT).show();
        }
    }

    }