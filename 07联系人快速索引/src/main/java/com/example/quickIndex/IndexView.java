package com.example.quickIndex;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lenovo on 2020/1/4.
 * 1.首先计算出怎么画出这26个字母
 * 2.
 */
public class IndexView extends View {

    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private int itemWidth;
    private int itemHeight; //每一个矩形的宽和高
    private int wordWidth;
    private int wordHeight;//每一个文字的宽和高
    //还要计算每一个文字的位置

    Paint paint;
    private OnChoiceListener onChoiceListener;

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.DEFAULT_BOLD); //设置加粗
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画出26个字母
        int curX = itemWidth / 2 - wordWidth / 2;
        int curY = itemHeight / 2 + wordHeight / 2;

        paint.setTextSize(wordWidth);

        for(int i=0;i<words.length;i++){
            if(i == chioceIndex){
                paint.setColor(Color.GRAY);
            }else{
                paint.setColor(Color.WHITE);
            }
            canvas.drawText(words[i],curX,curY,paint);
            curY += itemHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;

        wordWidth = itemWidth / 3;
        wordHeight = itemHeight / 3;//定义文字的大小
    }

    int chioceIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int downIndex = (int) (event.getY() / itemHeight);
                if(downIndex < 0 ){
                    downIndex = 0;
                }else if(downIndex >= words.length){
                    downIndex = words.length - 1;
                }
                chioceIndex = downIndex;
                if(onChoiceListener != null){
                    onChoiceListener.onChoice(chioceIndex, words[chioceIndex]);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //up就清空了
                chioceIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    interface OnChoiceListener{
        void onChoice(int chioceIndex,String word);
    }
    public void setOnChoiceListener(OnChoiceListener onChoiceListener){
        this.onChoiceListener = onChoiceListener;
    }
}
