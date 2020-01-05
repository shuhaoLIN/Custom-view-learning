package com.example.slideview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listview;
    private MyAdapter adapter;
    private List<Content> contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);
        contents = new ArrayList<>();
        initDate();
        adapter = new MyAdapter();
        listview.setAdapter(adapter);
    }

    private void initDate() {
        for(int i=0;i<100;i++){
            contents.add(new Content("content--" + i));
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contents.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(MainActivity.this, R.layout.item_layout,null);

                holder = new ViewHolder();
                holder.content = (TextView)convertView.findViewById(R.id.content);
                holder.menu = (TextView)convertView.findViewById(R.id.menu);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.content.setText(contents.get(position).getContent());

            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, contents.get(position).getContent(), Toast.LENGTH_SHORT).show();
                }
            });
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SlideLayout slideLayout = (SlideLayout)v.getParent();
                    slideLayout.closeMenu();
                    contents.remove(position);
                    notifyDataSetChanged();
                }
            });

            SlideLayout slideLayout = (SlideLayout) convertView;
            slideLayout.setOnStateChangeListener(new MyOnStateChangeListener());
            return convertView;
        }
    }
    SlideLayout slideLayoutStateChange;
    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener{
        @Override
        public void onOpen(SlideLayout slideLayout) {
            if(slideLayoutStateChange != slideLayout){
                if(slideLayoutStateChange != null){
                    slideLayoutStateChange.closeMenu();
                }
                slideLayoutStateChange = slideLayout;
            }
        }

        @Override
        public void onClose(SlideLayout slideLayout) {
            if(slideLayoutStateChange != null){
                slideLayoutStateChange = null;
            }
        }

        @Override
        public void onDown(SlideLayout slideLayout) {
            if(slideLayoutStateChange != null && slideLayoutStateChange != slideLayout){
                slideLayoutStateChange.closeMenu();
            }
        }
    }
    class ViewHolder{
        TextView content;
        TextView menu;
    }
}
