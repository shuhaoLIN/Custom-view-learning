package com.example.myviewpager;
import android.content.Context;
import android.os.SystemClock;

/**
 * 利用时间来进行移动，就能够通过时间变量进行 控制了
 * 1.具体的长度
 * 2.设定时间变量
 * 3.for进行循环移动即可
 *
 * 最终想要得到的是移动的坐标，然后给到viewpager进行移动即可
 */
class MyScroller{
    private Context context;
    private float startX;
    private float startY;
    private int distanceX;
    private int distanceY;
    private long startTime;
    /**
     * 用以判断动画是否结束
     */
    private boolean isFinish;
    /**
     * 总时间
     */
    private long totalTime = 500;

    public float getCurrX() {
        return currX;
    }

    /**
     * 这是当前坐标
     */
    private float currX;

    public MyScroller(Context context) {
        this.context = context;
    }
    public void startScroll(float startX, float startY, int distanceX, int distanceY){
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        //开始执行动画的时间
        this.startTime = SystemClock.uptimeMillis(); //系统开机时间
        this.isFinish = false;
    }

    /**
     * 计算偏移量
     * 速度，求移动一小段对应的坐标，时间，距离
     * @return false表示移动结束，true表示移动中
     */
    public boolean computeScrollOffset(){
        if (isFinish == true){
            return false;
        }
        long endTime = SystemClock.uptimeMillis();
        long passTime = endTime - startTime;
        if(passTime < totalTime){
            //移动一小段
            //计算平均速度
            float disatnceSamllX = passTime * distanceX / totalTime;
            currX = startX + disatnceSamllX;
        }else{
            //移动结束
            isFinish = true;
            currX = startX + distanceX;
        }
        return true;
    }
}