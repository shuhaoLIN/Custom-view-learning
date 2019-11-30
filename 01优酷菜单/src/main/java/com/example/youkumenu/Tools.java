package com.example.youkumenu;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by lenovo on 2019/11/20.
 */
public class Tools {
    public static void hideView(ViewGroup view) {
        hideView(view, 0);
    }

    public static void showView(ViewGroup view) {
//        RotateAnimation ra = new RotateAnimation(180,360,view.getWidth()/2,view.getHeight());
//        ra.setDuration(500); //动画持续时间
//        ra.setFillAfter(true); //保持动画的最后状态
//        view.startAnimation(ra);
//
//        int childCount = view.getChildCount();
//        for(int i=0;i<childCount;i++){
//            view.getChildAt(i).setEnabled(true);
//        }

        //利用属性动画改写该函数
//        view.setRotation();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation",180, 360);
        animator.setDuration(500);
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight());
        animator.start();
    }

    public static void hideView(ViewGroup view, int startOffset) {
//        RotateAnimation ra = new RotateAnimation(0,180,view.getWidth()/2,view.getHeight());
//        ra.setStartOffset(startOffset);
//        ra.setDuration(500); //动画持续时间
//        ra.setFillAfter(true); //保持动画的最后状态
//        view.startAnimation(ra);
//
//        //使用ViewGroup对子view进行遍历设置不可响应
//        int childCount = view.getChildCount();
//        for(int i=0;i<childCount;i++){
//            view.getChildAt(i).setEnabled(false);
//        }

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation",0, 180);
        animator.setDuration(500);
        animator.setStartDelay(startOffset);
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight());
        animator.start();
    }
}
