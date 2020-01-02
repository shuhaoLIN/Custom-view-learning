package com.example.myviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
    MyViewpager myViewpager ;
    int[] ids = {R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,R.drawable.a6};
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewpager = (MyViewpager) findViewById(R.id.my_viepager);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);



        for(int i=0;i < ids.length;i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            //添加到viewpager
            myViewpager.addView(imageView);
        }
        View testview = View.inflate(this,R.layout.test,null);
        myViewpager.addView(testview,2);

        for(int i=0;i<myViewpager.getChildCount();i++){
            RadioButton button = new RadioButton(this);
            button.setId(i);
            radioGroup.addView(button);
            if (i == 0){
                button.setChecked(true);
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                myViewpager.scrollToPager(checkedId);
            }
        });
        myViewpager.setOnPagerChangeLinstener(new MyViewpager.OnPagerChangeListener() {
            @Override
            public void onPagerChange(int position) {
                radioGroup.check(position);
            }
        });
    }
}
