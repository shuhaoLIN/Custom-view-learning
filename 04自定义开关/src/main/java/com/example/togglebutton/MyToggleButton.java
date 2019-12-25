package com.example.togglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo on 2019/12/2.
 */
public class MyToggleButton extends View implements View.OnClickListener {
    private Bitmap backgroundBitmap;
    private Bitmap slideBitmap;
    private Paint paint;

    /**
     * 两个图片最左距离
     */
    private int slideLeftMax;
    private boolean isOpen;
    private float slideLeftNow;

    private float startX; //记录划了多远
    /**
     * 这个方法：如果需要在xml中去直接定义的话，是肯定要实现这个方法的；否则可以不实现这个构造器
     * @param context
     * @param attrs
     */
    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slideBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        paint = new Paint();
        paint.setAntiAlias(true);
        //初始状态为关闭状态
        isOpen = false;
        slideLeftMax = backgroundBitmap.getWidth() - slideBitmap.getWidth();
        slideLeftNow = 0;
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawBitmap(slideBitmap,slideLeftNow,0,paint);
    }

    /**
     * 标记点击事件是否生效，true生效，false不生效
     */
    boolean isClickEnable = true;
    @Override
    public void onClick(View v) {
        if (isClickEnable){
            isOpen = !isOpen;
            viewFlush();
            invalidate(); //这个会去强制调用onDraw方法
        }
    }

    /**
     * 根据isOpen去跟新view
     */
    private void viewFlush(){
        if (isOpen){
            slideLeftNow = slideLeftMax;
        }else{
            slideLeftNow = 0;
        }
    }

    private float lastX; //记录第一次点击的位置，（即是最开始的位置）便于后面进行判断是否是滑动事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.获取按下的坐标
                lastX = startX = event.getX();
                isClickEnable = true;
                Log.e(TAG, "onTouchEvent: slidelefting = " + startX);
                break;
            case MotionEvent.ACTION_MOVE:
                //2.计算偏移量
                float distance = event.getX() - startX;
                slideLeftNow += distance;
                Log.e(TAG, "onTouchEvent: slideleftnow = " + slideLeftNow);
                //3，屏蔽非法值
                if (slideLeftNow <= 0){
                    slideLeftNow = 0;
                }else if(slideLeftNow >= slideLeftMax){
                    slideLeftNow = slideLeftMax;
                }
                //4.刷新
                invalidate();
                startX = event.getX(); //刷新后就要还原了

                /**
                 * 设置是否响应点击事件
                 */
                if (Math.abs(event.getX() - lastX )> 5){
                    isClickEnable = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isClickEnable){
                    //设置沾边
                    if (slideLeftNow >= slideLeftMax / 2){
                        isOpen = true;
                    }else{
                        isOpen = false;
                    }
                    viewFlush();
                    invalidate();
                }
                break;
        }
        return true;
    }
}
