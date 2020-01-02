package com.example.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Scroller;
import android.widget.Toast;

/**
 * Created by lenovo on 2020/1/1.
 */
public class MyViewpager extends ViewGroup {

    private static final String TAG = MyViewpager.class.getSimpleName();
    /**
     * 手势识别器
     * 1.定义出来
     * 2.实例化：把想要的方法给重写
     * 3。在onTouchEvent()把事件传递给手势识别器
     */
    private GestureDetector detector;
    private Scroller scroller;
    private OnPagerChangeListener myOnPagerChangeListener;

    public MyViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(final Context context) {
        scroller = new Scroller(context);
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                Toast.makeText(context, "长按", Toast.LENGTH_SHORT).show();
                super.onLongPress(e);
            }

            /**
             *
             * @param e1 表示滑动的第一个点，即是按下的点
             * @param e2 表示滑动的第二个点，即是松开的点
             * @param distanceX 滑动的X距离
             * @param distanceY 滑动的Y距离
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Toast.makeText(context, "滑动", Toast.LENGTH_SHORT).show();
                /**
                 * 根据距离进行滑动
                 * 查看源码可以发现是调用了scrollTo方法
                 * 就是getScrollX + distanceX
                 */
                scrollBy((int)distanceX,getScrollY()); //getScrollY就是起始值，就不会进行上下滑动

                return true;
            }
        });
    }

    /**
     * 对子view进行重写
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //给每个孩子指定在屏幕的坐标位置
        for (int i=0;i<getChildCount();i++){
            View childView = getChildAt(i);
            childView.layout(i*getWidth(),0,(i+1)*getWidth(),getHeight());
        }
    }

    /**
     * 重写onMeasure，实现对子布局的测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 手指第一次按下的坐标
     */
    private int startX = 0;
    /**
     * 当前页面下标
     */
    private int curIndex = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("onTouchEvent == ACTION_DOWN");
                //1.记录坐标
                startX = (int)event.getX();
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onTouchEvent == ACTION_UP");

                //2.记录松开坐标
                int endX = (int) event.getX();
                //3.计算坐标值，并且移动
                int tempIndex = curIndex;
                if(endX - startX > getWidth() / 2){
                    //移动到上一个页面
                    tempIndex--;
                }else if(startX - endX > getWidth() /2 ){
                    //移动到下一个页面
                    tempIndex++;
                }
                //4,移动到某一个页面
                scrollToPager(tempIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("onTouchEvent == ACTION_MOVE");
                break;
        }
        return true;
    }

    /**
     * 根据下标位置进行定位页面，并且处理越界问题
     * @param tempIndex
     */
    public void scrollToPager(int tempIndex) {
        if(tempIndex < 0){
            tempIndex = 0;
        }
        if(tempIndex >= getChildCount() - 1){
            tempIndex = getChildCount() - 1;
        }
        //在这里保证curIndex是合法的
        curIndex = tempIndex;

        //在这里响应回调传输位置信息
        if(myOnPagerChangeListener != null)
            myOnPagerChangeListener.onPagerChange(curIndex);

        int distanceX = curIndex * getWidth() - getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(),distanceX,0,Math.abs(distanceX));

        //刷新
        invalidate(); //onDraw， computeScroll方法执行
//        scrollTo(curIndex * getWidth(), 0);
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()){
            float curX = scroller.getCurrX();
            scrollTo((int) curX,0);
            invalidate();
        }
    }

    interface OnPagerChangeListener {
        void onPagerChange(int position);
    }
    public void setOnPagerChangeLinstener(OnPagerChangeListener linstener){
        myOnPagerChangeListener = linstener;
    }

    private float firstX;
    private float firstY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        //因为这个函数会消耗掉down消息，如果只是下面的逻辑的话，那么onTouchEvent接收不到dowm消息，那么就会出现跳动的情况
        //如果是横向滑动，就响应viewpager，拦截
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("onOnterceptTouchEvent == ACTION_DOWN");
                firstX = ev.getX();
                firstY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("onOnterceptTouchEvent == ACTION_MOVE");
                //因为滑动期间就要页面的转动，所以还是要在move中进行响应
                float endX = ev.getX();
                float endY = ev.getY();
                float disX = Math.abs(endX - firstX);
                float disY = Math.abs(endY - firstY);
                if(disX > disY && disX > 5) {
                    return true;
                }else{
                    scrollToPager(curIndex);
                }
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onOnterceptTouchEvent == ACTION_UP");
                break;

        }
        //否则就false，不拦截
        return false;
    }
}
