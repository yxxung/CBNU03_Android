package com.cookandroid.project7_3;

import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText tvName, tvEmail;
    Button button1;
    EditText dlgEdtName, dlgEdtEmail;
    TextView toastText;
    View dialogView, toastView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("사용자 정보 입력");

        tvName = (EditText) findViewById(R.id.tvName);
        tvEmail = (EditText) findViewById(R.id.tvEmail);
        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialogView = (View) View.inflate(MainActivity.this,
                        R.layout.dialog1, null);

                AlertDialog.Builder dlg = new AlertDialog.Builder(
                        MainActivity.this);

                dlgEdtName = (EditText) dialogView
                        .findViewById(R.id.dlgEdt1);
                dlgEdtEmail = (EditText) dialogView
                        .findViewById(R.id.dlgEdt2);

                dlgEdtName.setText(tvName.getText());
                dlgEdtEmail.setText(tvEmail.getText());

                dlg.setTitle("사용자 정보 입력");
                dlg.setIcon(R.drawable.ic_menu_allfriends);
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                tvName.setText(dlgEdtName.getText());
                                tvEmail.setText(dlgEdtEmail.getText());
                            }
                        });

                dlg.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Toast toast = new Toast(MainActivity.this);

                                toastView = (View) View.inflate(
                                        MainActivity.this,
                                        R.layout.toast1, null);
                                toastText = (TextView) toastView
                                        .findViewById(R.id.toastText1);
                                toastText.setText("취소했습니다");
                                toast.setView(toastView);
                                Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
                                int coordX =(int)(Math.random()*display.getWidth());
                                int coordY = (int)(Math.random()*display.getHeight());
                                toast.setGravity(Gravity.TOP,coordX,coordY);
                                toast.show();
                            }
                        });
                dlg.show();

            }
        });

    }
}
