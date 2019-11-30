package com.example.viewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        }

        viewpager.setAdapter(new MyPagerAdapter());
    }
    class MyPagerAdapter extends PagerAdapter{

        /**
         * 返回数量
         * @return
         */
        @Override
        public int getCount() {
            return imageViews.size();
        }

        /**
         * 相当于listview中的getview
         * @param container 这个就是viewpager自身了
         * @param position 这个就是当前的位置
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            Log.e(TAG, "instantiateItem: ==" + position + "  iamgeview == " + imageView);
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
