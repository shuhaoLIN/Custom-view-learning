package com.example.autoattrs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lenovo on 2019/12/25.
 */
public class MyAttrsView extends View {
    String text;
    Bitmap image;
    /**
     * 这个如果还是需要在xml中使用，必须实现
     * @param context
     * @param attrs  这个就是我们的属性集合了，通过遍历即可实现对属性的控制
     */
    public MyAttrsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //1.通过命名空间获取
        int age2 = Integer.parseInt(attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","my_age"));
        String name2 = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","my_name");
        System.out.println("age == " + age2 + "name == " + name2);
        //2.遍历
        for (int i=0;i<attrs.getAttributeCount();i++){
            String name1 = attrs.getAttributeValue(i);
            String value1 = attrs.getAttributeValue(i);
            System.out.println("name = " + name1 + "value == " + value1);
        }
        //3.使用TypedArray进行遍历
        //第一个实例化的参数属性集合，第二个参数是定义在attrs.xml文件的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyAttrsView);
        int indexCount = array.getIndexCount();
        for (int i =0; i< indexCount;i++){
            int index = array.getIndex(i);
            switch (index){
                case R.styleable.MyAttrsView_my_age:
                    int age = array.getInt(index, 10);
                    System.out.println("age == " + age);
                    break;
                case R.styleable.MyAttrsView_my_name:
                    String name = array.getString(index);
                    System.out.println("name == " + name);
                    break;
                case R.styleable.MyAttrsView_my_github:
                    String github = array.getString(index);
                    text = github;
                    System.out.println("my github ==" + github);
                    break;
                case R.styleable.MyAttrsView_my_bg:
                    Drawable drawable = array.getDrawable(index);
                    image = ((BitmapDrawable)drawable).getBitmap(); //Drawable转换为bitmap
                    System.out.println("drawable == " + drawable);
                    break;
                default:
                    break;
            }
        }
        //需要进行回收
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Paint paint = new Paint();
        canvas.drawText(text, 40,40,paint);
        canvas.drawBitmap(image, 50,50,paint);
    }
}
