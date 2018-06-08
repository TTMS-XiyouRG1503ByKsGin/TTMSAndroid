package com.example.wtl.ttms_hdd.Main.view;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.wtl.ttms_hdd.Film.view.mainFragment.FilmShowFragment;
import com.example.wtl.ttms_hdd.R;
import com.example.wtl.ttms_hdd.Tool.HideScreenTop;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationBar bottom_navigation;
    /**
     * 电影碎片
     */
    private FilmShowFragment showFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideScreenTop.HideScreenTop(getWindow());
        bottom_navigation = findViewById(R.id.bottom_navigation);
        /*
        * 设置样式
        * */
        bottom_navigation.setMode(BottomNavigationBar.MODE_FIXED)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setBarBackgroundColor(R.color.white)//背景颜色
                .setInActiveColor(R.color.nav_gray)//未选中时的颜色
                .setActiveColor(R.color.changeclick)//选中时的颜色
                .addItem(new BottomNavigationItem(R.mipmap.home, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.movie, "影片"))
                .addItem(new BottomNavigationItem(R.mipmap.hallitem, "影厅"))
                .addItem(new BottomNavigationItem(R.mipmap.my, "我的"))
                .initialise();

        change(bottom_navigation);

    }

    private void change(BottomNavigationBar bottom_navigation) {
        bottom_navigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                hideFragment(transaction);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        if (showFragment == null) {
                            showFragment = new FilmShowFragment();
                        }
                        transaction.replace(R.id.add_fragment, showFragment);
                        transaction.show(showFragment);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (showFragment != null) {
            transaction.hide(showFragment);
        }
    }

}
