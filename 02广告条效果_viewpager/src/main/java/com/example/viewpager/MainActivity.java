package com.example.viewpager;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName(); // 这样子写的好处是，我们要是改了类名了，这个肯定报错，那么我们就可以改过来，不用刻意去记住
    ViewPager viewpager;
    TextView textview;
    LinearLayout point_group;

    ArrayList<ImageView> imageViews;

    private final int[] imageIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d
    };
    private final String[] imageDec = {
            "图片一",
            "图片二",
            "图片三",
            "图片四"
    };

    private  int prePosition = 0; //这个prePosition是针对group中第几个子View定义的

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = viewpager.getCurrentItem() + 1 ;
            viewpager.setCurrentItem(item);

            handler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager =  (ViewPager) findViewById(R.id.viewpager);
        textview = (TextView) findViewById(R.id.textview);
        point_group = (LinearLayout) findViewById(R.id.point_group);

        imageViews = new ArrayList<>();

        /**
         * viewpager的使用方式
         * 1.在配置文件中定义
         * 2.在实例化
         * 3.准备数据
         * 4.设置适配器-item布局-绑定数据
         */
        for(int i=0;i<imageIds.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);
            imageViews.add(imageView);

            //添加点（用图片来做）
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15,15);
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            if(i == prePosition){
                point.setEnabled(true);
            }else{
                point.setEnabled(false);
                params.leftMargin = 15;
            }
            point.setLayoutParams(params);
            point_group.addView(point);
        }

        viewpager.setAdapter(new MyPagerAdapter());
        //设置监听viewpager的页面改变
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
        textview.setText(imageDec[prePosition]);

         int currentItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageViews.size();
        //使得第一个item是中间item，那么就可以保证能够左右滑动了
        viewpager.setCurrentItem(currentItem);

        handler.sendEmptyMessageDelayed(0, 2000);
    }
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        /**
         * 当页面滚动了回调这个方法
         * @param position 当前页面的位置
         * @param positionOffset 滑动页面的百分比
         * @param positionOffsetPixels 在屏幕上滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当某个页面被选中的时候回调
         * @param position 被选中的页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int realPosition = position % imageViews.size();

            //设置对应页面的文本信息
            textview.setText(imageDec[realPosition]);
            //把上一次的高亮设置为灰色
            point_group.getChildAt(prePosition).setEnabled(false); // 因为这个是针对这个group的，所以要用realPosition
            //这一次选中的为高亮
            point_group.getChildAt(realPosition).setEnabled(true);

            prePosition = realPosition;
        }

        /**
         * 当页面的滚动状态变化的时候回调
         * 静止 --》 滑动
         * 滑动 --》 静止
         * 静止 --》 拖拽
         * @param state
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING){
                handler.removeCallbacksAndMessages(null);
            }else if(state == ViewPager.SCROLL_STATE_SETTLING){

            }else if (state == ViewPager.SCROLL_STATE_IDLE){
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0, 2000);
            }
        }
    }
    class MyPagerAdapter extends PagerAdapter{

        /**
         * 返回数量
         * @return
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        /**
         * 相当于listview中的getview
         * @param container 这个就是viewpager自身了
         * @param position 这个就是当前的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = position % imageViews.size();
            ImageView imageView = imageViews.get(realPosition);
            Log.e(TAG, "instantiateItem: ==" + position + "  iamgeview == " + imageView + " -- realPosition == " + realPosition);

            //设置点击事件
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN: //手指按下去
                            Log.e(TAG, "onTouch: down");
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_MOVE: //手指移动
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                        case MotionEvent.ACTION_UP: //手指离开
                            Log.e(TAG, "onTouch: UP");
                            handler.sendEmptyMessageDelayed(0, 2000);
                            break;

                    }
                    return false;
                }
            });
            //让View去携带信息
            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag() % imageViews.size();

                    String text = imageDec[position];
                    Toast.makeText(MainActivity.this, "text ==" + text, Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(imageView);
            return imageView;
        }

        /**
         * 比较view跟object是否是同一个实例
         * @param view 页面
         * @param object 就是instantiateItem返回的实例
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 这个是做回收的
         * @param container
         * @param position 要释放的位置
         * @param object 要释放的资源
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.e(TAG, "destroyItem: ==" + position + "  iamgeview == " + object);
            container.removeView((View)object);
        }
    }
}
