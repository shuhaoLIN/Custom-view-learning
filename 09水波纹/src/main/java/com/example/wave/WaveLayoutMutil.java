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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2020/1/5.
 */
public class WaveLayoutMutil extends View {
    private static final float MAXRADIUS = 150;
    Paint paint;
    int[] colors = new int[]{Color.BLUE, Color.GRAY, Color.GREEN, Color.RED};
    private ArrayList<Wave> waves;

    private Set<Wave> deleteSet;

    private final int BEGINRADIUS = 5;
    private final int BEGINAPHLA = 255;
    public WaveLayoutMutil(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE); //画笔画空心圆
        paint.setStrokeWidth(8);
        paint.setAntiAlias(true);

        waves = new ArrayList<>();
        deleteSet = new HashSet<>();
    }

    private float downX;
    private float downY;
    private int lastAphla;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            for (Wave wave : waves){
                int radius = wave.getRadio();
                int aphla = wave.getAphla();
                radius += 5;
                aphla -= 5;
                if (aphla < 0) {
                    aphla = 0;
                }
                wave.setRadio(radius);
                wave.setAphla(aphla);
            }
//            radius += 5;
//            int aphla = paint.getAlpha();
//            lastAphla = aphla;
//            System.out.println(aphla);
//            aphla -= 5;
//            if (aphla < 0) {
//                aphla = 0;
//            }
//            paint.setColor(colors[3]);
//            paint.setAlpha(aphla);
            invalidate();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                downX = event.getX();
                downY = event.getY();
                System.out.println("down++++" + paint.getAlpha());

                waves.add(new Wave(downX,downY,BEGINRADIUS, BEGINAPHLA));

                handler.sendEmptyMessageDelayed(0, 50);
                break;
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (waves.size() > 0){
            for (Wave wave : waves){
                if (wave.getAphla() > 0){
                    paint.setAlpha(wave.getAphla());
                    canvas.drawCircle(wave.getX(), wave.getY(), wave.getRadio(),paint);
                }else{
                    deleteSet.add(wave);
                }
            }
            for (Wave wave : deleteSet){
                waves.remove(wave);
            }
            deleteSet.clear();
            handler.sendEmptyMessageDelayed(0, 50);
        }

//        if (paint.getAlpha() > 0) {
//            canvas.drawCircle(downX, downY, radius, paint);
//            canvas.drawCircle(downX + 100, downY + 100, radius, paint);
//            handler.sendEmptyMessageDelayed(0, 50);
//        } else {
//            radius = 20;
//            paint.setColor(colors[3]);
//        }
    }

    class Wave {
        public float X;
        public float Y;
        public int radio;
        public int aphla;

        public Wave(float x, float y, int radio, int aphla) {
            X = x;
            Y = y;
            this.radio = radio;
            this.aphla = aphla;
        }

        public float getX() {
            return X;
        }

        public void setX(float x) {
            X = x;
        }

        public float getY() {
            return Y;
        }

        public void setY(float y) {
            Y = y;
        }

        public int getRadio() {
            return radio;
        }

        public void setRadio(int radio) {
            this.radio = radio;
        }

        public int getAphla() {
            return aphla;
        }

        public void setAphla(int aphla) {
            this.aphla = aphla;
        }
    }
}
