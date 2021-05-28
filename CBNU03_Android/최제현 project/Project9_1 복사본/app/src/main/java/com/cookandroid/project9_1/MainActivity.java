package com.cookandroid.project9_1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    final static int LINE = 1, CIRCLE = 2, SQAURE= 3, COLORRED = 4, COLORGREEN=5;
    static int curShape = LINE;
    static int curColor = Color.RED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyGraphicView(this));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        setTitle("간단 그림판");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "선 그리기");
        menu.add(0, 2, 0, "원 그리기");
        menu.add(0, 3, 0, "사각형그리기");
        SubMenu sub=menu.addSubMenu("색깔변경");
        sub.add(0, 4, 0, "빨간색");
        sub.add(0, 5, 0, "초록");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                curShape = LINE; // 선
                return true;
            case 2:
                curShape = CIRCLE; // 원
            case 3:
                curShape = SQAURE;
            case 4:
                curColor = COLORRED;
            case 5:
                curColor = COLORGREEN;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //커스텀 뷰 선언
   private static class MyGraphicView extends View {
        int startX = -1, startY = -1, stopX = -1, stopY = -1, preX = -1, preY = -1;  //시작점 선
        public MyGraphicView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(startX != -1 && startY != -1){
                        preX = startX;
                        preY = startY;
                    }
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    stopX = (int) event.getX();
                    stopY = (int) event.getY();
                    this.invalidate(); // 화면 재표
                    break;
            }
            return true;
        }
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(curColor);
//            startX = -1;
//            startY = -1;

            switch (curShape) {
                case LINE:
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                    break;
                case CIRCLE:
                    int radius = (int) Math.sqrt(Math.pow(stopX - startX, 2)
                            + Math.pow(stopY - startY, 2));
                    canvas.drawCircle(startX, startY, radius, paint);
                    break;
                case SQAURE:
                    //문제에서 이걸 원한거인지를 모르겠음.
                    // 좌표 2개 어케저장할까
                    if(preX != -1 && preY != -1) {
                        canvas.drawRect(new Rect(preX, preY, stopX, stopY), paint);
                        startX = -1;
                        startY = -1;
                        preX = -1;
                        preY = -1;
                        //해결?
                    }

                    break;
            }

            switch (curColor){
                case COLORRED:
                    curColor = Color.RED;
                    break;
                case COLORGREEN:
                    curColor = Color.GREEN;
                    break;

            }
        }
    }
}
