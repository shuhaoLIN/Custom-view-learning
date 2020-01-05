package com.example.quickIndex;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    TextView showChioce;
    IndexView indexView;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showChioce.setVisibility(View.GONE);
        }
    };
    ListView listView ;
    MyAdapter adapter;
    private ArrayList<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showChioce = (TextView) findViewById(R.id.show_choice);
        indexView = (IndexView) findViewById(R.id.index_view);
        indexView.setOnChoiceListener(new MyOnChoiceListener());


        listView = (ListView) findViewById(R.id.listview);
        initData();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

    }
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return persons.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(MainActivity.this,R.layout.item_layout,null);
                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.name);
                holder.pinyin = convertView.findViewById(R.id.pinyin);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            //设置内容啥的
            holder.name.setText(persons.get(position).getName());
            holder.pinyin.setText(persons.get(position).getPinyin().substring(0,1));

            if(position != 0){
                if(persons.get(position).getPinyin().substring(0,1).equals(
                        persons.get(position - 1).getPinyin().substring(0,1)
                )){
                    holder.pinyin.setVisibility(View.GONE);
                }else{
                    holder.pinyin.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }
    }
    class ViewHolder{
        TextView name;
        TextView pinyin;
    }
    class MyOnChoiceListener implements IndexView.OnChoiceListener{

        @Override
        public void onChoice(int chioceIndex, String word) {
            showChoiceIndex(word);
            showChoiceName(word);
        }
    }
    private void showChoiceName(String word){
        for(int i=0;i<persons.size();i++){
            if(persons.get(i).getPinyin().substring(0,1).equals(word)){
                listView.setSelection(i);
                break;
            }
        }
    }
    private void showChoiceIndex(String word) {
        showChioce.setVisibility(View.VISIBLE);
        showChioce.setText(word);
        handler.sendEmptyMessageDelayed(0,3000);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        persons = new ArrayList<>();
        persons.add(new Person("张啊啊啊啊啊"));
        persons.add(new Person("杨啊啊啊啊啊"));
        persons.add(new Person("胡啊啊啊啊啊"));
        persons.add(new Person("刘啊啊啊啊啊"));

        persons.add(new Person("钟啊啊啊啊啊"));
        persons.add(new Person("尹啊啊啊啊啊"));
        persons.add(new Person("安啊啊啊啊啊"));
        persons.add(new Person("张啊啊啊啊啊"));

        persons.add(new Person("温啊啊啊啊啊"));
        persons.add(new Person("李啊啊啊啊啊"));
        persons.add(new Person("刘啊啊啊啊啊"));
        persons.add(new Person("娄啊啊啊啊啊"));
        persons.add(new Person("张啊啊啊啊啊"));

        persons.add(new Person("王啊啊啊啊啊"));
        persons.add(new Person("李啊啊啊啊啊"));
        persons.add(new Person("孙啊啊啊啊啊"));
        persons.add(new Person("唐啊啊啊啊啊"));
        persons.add(new Person("牛啊啊啊啊啊"));
        persons.add(new Person("姜啊啊啊啊啊"));

        persons.add(new Person("刘啊啊啊啊啊"));
        persons.add(new Person("张啊啊啊啊啊"));
        persons.add(new Person("张啊啊啊啊啊"));
        persons.add(new Person("侯啊啊啊啊啊"));
        persons.add(new Person("刘啊啊啊啊啊"));

        persons.add(new Person("乔啊啊啊啊啊"));
        persons.add(new Person("徐啊啊啊啊啊"));
        persons.add(new Person("吴啊啊啊啊啊"));
        persons.add(new Person("王啊啊啊啊啊"));

        persons.add(new Person("阿啊啊啊啊啊"));
        persons.add(new Person("李啊啊啊啊啊"));


        //排序
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person lhs, Person rhs) {
                return lhs.getPinyin().compareTo(rhs.getPinyin());
            }
        });

    }
}
