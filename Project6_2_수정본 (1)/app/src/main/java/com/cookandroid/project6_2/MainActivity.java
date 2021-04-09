package com.cookandroid.project6_2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    EditText edtUrl;
    Button btnGo, btnBack;
    WebView web;
    private long backBtnTime = 0;
    private ProgressBar progressBar;
    LinearLayout linearLayoutTeam;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.web);

        edtUrl = (EditText) findViewById(R.id.edtUrl);
        btnGo = (Button) findViewById(R.id.btnGo);
        btnBack = (Button) findViewById(R.id.btnBack);
        web = (WebView) findViewById(R.id.webView1);
        progressBar = (ProgressBar) findViewById(R.id.loading); // 새로 작성
        linearLayoutTeam = (LinearLayout)findViewById(R.id.linearLayoutTeam);

        web.setWebViewClient(new CookWebViewClient());
        WebSettings webSet = web.getSettings();
        webSet.setBuiltInZoomControls(true);
        webSet.setJavaScriptEnabled(true);

        btnGo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                web.loadUrl(edtUrl.getText().toString());
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long curTime = System.currentTimeMillis();
                long gapTime = curTime - backBtnTime;
                if(web.canGoBack()){
                    web.goBack();
                }else if (0 <= gapTime && 2000 >= gapTime)  {
                    MainActivity.super.onBackPressed();
                }else{
                    backBtnTime = curTime;
                    //Toast.makeText(this,"한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    class CookWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.setVisibility(View.GONE); //새로 추가한 기능
            linearLayoutTeam.setVisibility(View.GONE); //새로 추가한 기능
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
    }

