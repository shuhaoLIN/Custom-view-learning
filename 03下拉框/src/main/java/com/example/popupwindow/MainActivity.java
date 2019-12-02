package com.example.popupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText et_input;
    private ImageView iv_down_arrow;

    private PopupWindow popupWindow;
    private ListView listview;

    private ArrayList<String> msgs;
    private MyAdapter adapter;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_down_arrow = (ImageView) findViewById(R.id.iv_down_arrow);
        et_input = (EditText) findViewById(R.id.et_input);

        iv_down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow == null){
                    popupWindow = new PopupWindow(MainActivity.this);
                    popupWindow.setWidth(et_input.getWidth());
                    int height = DensityUtil.dip2px(MainActivity.this, 200); //两百dp换成px
                    Toast.makeText(MainActivity.this, height+"", Toast.LENGTH_SHORT).show();
                    popupWindow.setHeight(height); //像素 px ，所以不适配
                    popupWindow.setContentView(listview);
                    popupWindow.setFocusable(true); //设置焦点，不然不响应
                }
                popupWindow.showAsDropDown(et_input,0,0);
            }
        });

        //准备数据
        listview = new ListView(this);
        listview.setBackgroundResource(R.drawable.listview_background);
        msgs = new ArrayList<>();
        for(int i=0;i<500;i++){
            msgs.add(i+"00000000000"+i);
        }
        adapter = new MyAdapter();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = msgs.get(position);
                et_input.setText(msg);
                //释放popuwindow资源
                if (popupWindow != null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                ImageView iv_delete = (ImageView)convertView.findViewById(R.id.iv_delete);
                TextView tv_msg = (TextView)convertView.findViewById(R.id.tv_msg);

                holder = new ViewHolder();
                holder.tv_msg = tv_msg;
                holder.iv_delete = iv_delete;

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_msg.setText(msgs.get(position));
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msgs.remove(position);
                    adapter.notifyDataSetChanged(); // getCount()->getView()
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView tv_msg;
            ImageView iv_delete;
        }
    }
}
