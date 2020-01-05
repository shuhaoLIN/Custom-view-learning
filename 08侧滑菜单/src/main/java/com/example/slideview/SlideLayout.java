package com.example.slideview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by lenovo on 2020/1/4.
 * 1.我们将通过自定义实现菜单项显示在不可见的状态
 */
public class SlideLayout extends FrameLayout {

    private View contentView;
    private View menuView;

    private int contentWidth;
    private int menuWidth;
    private int bothHeight;


    private Scroller scroller;
    private OnStateChangeListener onStateChangeListener;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        scroller = new Scroller(context);
    }


    /**
     * 这个函数就是inflate之后就会执行的
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    /**
     * 在这个方法中获取到宽高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        bothHeight = contentView.getMeasuredHeight();
        System.out.println("contentwidth == " + contentWidth + "   menuWidth == " + menuWidth + "  bothHeight == " + bothHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        System.out.println("onLayout ===    contentwidth == " + contentWidth + "   menuWidth == " + menuWidth + "  bothHeight == " + bothHeight);

        menuView.layout(contentWidth, 0, contentWidth + menuWidth, bothHeight);
    }

    private int startX;
    private int startY;

    private int lastX;
    private int lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = startX = (int) event.getX();
                lastY = startY = (int) event.getY();
                System.out.println("ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                int distanceX = endX - startX;
                int distanceY = endY - startY;
//                System.out.println("AAAAAAAA    dx === " + dx + "   menuWidth === " +  menuWidth);
//                dx = -dx;
//                System.out.println("BBBBBBBB    dx === " + dx + "   menuWidth === " +  menuWidth);
//
//                if (dx > menuWidth) {
//                    dx = menuWidth;
//                }else if (dx < -menuWidth) {
//                    dx = -menuWidth;
//                }
//                System.out.println("CCCCCCCC    dx === " + dx + "   menuWidth === " +  menuWidth);
//                scrollBy(dx, getScrollY()); // 因为是针对当前的位置进行移动的，这样子判断一直判断不出来是否超届了
                int toScrollX = getScrollX() - distanceX;
                if(toScrollX < 0){
                    toScrollX = 0;
                }else if(toScrollX > menuWidth){
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX,getScrollY());
                startX = endX;
                startY = endY;

                //防拦截
                int DX = Math.abs((int) (event.getX() - lastX));
                int DY = Math.abs((int) (event.getY() - lastY));

                if(DX > DY && DX > 5){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                System.out.println("ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ACTION_UP");
                int totalX = getScrollX(); //当前的偏移量
                if(totalX >= menuWidth / 2) {
                    openMenu();
                }else{
                    closeMenu();
                }

                break;
        }
        return true;
    }

    public void closeMenu() {
        int distanceX = 0 - getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY()); //因为不在竖直方向移动，所以getScrollY一直是0
        invalidate();
        if(onStateChangeListener != null){
            onStateChangeListener.onClose(this);
        }
    }

    public void openMenu() {
        int distanceX = menuWidth - getScrollX();
        scroller.startScroll(getScrollX(),getScrollY(),distanceX,getScrollY() );
        invalidate();
        if(onStateChangeListener != null){
            onStateChangeListener.onOpen(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 返回true就是拦截了，我们要做的就是在滑动的时候拦截，在点击的时候不拦截
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        super.onInterceptTouchEvent(event);
        boolean result = false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = startX = (int) event.getX();
                lastY = startY = (int) event.getY();
                System.out.println("onInterceptTouchEvent -- ACTION_DOWN");
                if(onStateChangeListener != null){
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                int dx = Math.abs(endX - lastX);
                int dy = endY - lastY;
                if(dx > 5){
                    result = true;
                }
                System.out.println("onInterceptTouchEvent -- ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("onInterceptTouchEvent -- ACTION_UP");
                break;
        }
        return result;
    }
    interface OnStateChangeListener{
        void onOpen(SlideLayout slideLayout);
        void onClose(SlideLayout slideLayout);
        void onDown(SlideLayout slideLayout);
    }
    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener){
        this.onStateChangeListener = onStateChangeListener;
    }
}
