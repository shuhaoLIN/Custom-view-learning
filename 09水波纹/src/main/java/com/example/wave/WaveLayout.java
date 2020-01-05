package com.example.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by lenovo on 2020/1/5.
 */
public class WaveLayout extends View {
    private static final float MAXRADIUS = 150;
    Paint paint;
    int[] colors = new int[]{Color.BLUE,Color.GRAY,Color.GREEN,Color.RED};
    private float radius = 5;

    public WaveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE); //画笔画空心圆
        paint.setStrokeWidth(8);
        paint.setAntiAlias(true);
    }

    private float downX;
    private float downY;
    private int lastAphla;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            radius += 5;
            int aphla = paint.getAlpha();
            lastAphla = aphla;
            System.out.println(aphla);
            aphla -= 5;
            if(aphla < 0) {
                aphla = 0;
            }
            paint.setColor(colors[3]);
            paint.setAlpha(aphla);
            invalidate();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                System.out.println("down++++" + paint.getAlpha());
                handler.sendEmptyMessageDelayed(0,50);
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint.getAlpha() > 0){
            canvas.drawCircle(downX,downY,radius,paint);
            handler.sendEmptyMessageDelayed(0,50);
        }else{
            radius = 20;
            paint.setColor(colors[3]);
        }
    }
}
