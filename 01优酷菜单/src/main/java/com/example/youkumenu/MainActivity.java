package com.example.youkumenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout level1, level2, level3;
    ImageView icon_home, icon_menu;

    private boolean Level2Showing = true;
    private boolean Level3Showing = true;
    private boolean Level1Showing = true;

    MyOnClickListerener onClickListerener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        level1 = (RelativeLayout)findViewById(R.id.level1);
        level2 = (RelativeLayout)findViewById(R.id.level2);
        level3 = (RelativeLayout)findViewById(R.id.level3);

        icon_home = (ImageView)findViewById(R.id.icon_home);
        icon_menu = (ImageView)findViewById(R.id.icon_menu);

        onClickListerener = new MyOnClickListerener();

        icon_menu.setOnClickListener(onClickListerener);
        icon_home.setOnClickListener(onClickListerener);

    }
    class MyOnClickListerener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.icon_home:
                    //如果2显示了，就隐藏；并且3显示就隐藏
                    if(Level2Showing){
                        Tools.hideView(level2, 100);
                        Level2Showing = false;
                        if(Level3Showing){
                            Tools.hideView(level3);
                            Level3Showing = false;
                        }
                    }else{
                        Tools.showView(level2);
                        Level2Showing = true;
                    }
                    break;
                case R.id.icon_menu:
                    if(Level3Showing){
                        Tools.hideView(level3);
                        Level3Showing = false;
                    }
                    else{
                        Tools.showView(level3);
                        Level3Showing = true;
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU){
            //如果都展示，就都隐藏
            if(Level1Showing){
                Tools.hideView(level1);
                Level1Showing = false;
                if(Level2Showing){
                    Tools.hideView(level2, 100);
                    Level2Showing = false;
                }
                if(Level3Showing){
                    Tools.hideView(level3, 200);
                    Level2Showing = false;
                }
            }else{
                Tools.showView(level1);
                Level1Showing = true;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
