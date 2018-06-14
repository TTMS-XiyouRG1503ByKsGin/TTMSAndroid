package com.example.wtl.ttms_hdd.TheHome.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wtl.ttms_hdd.R;
import com.example.wtl.ttms_hdd.TheHome.model.HotSowModel;
import com.example.wtl.ttms_hdd.TheHome.presenter.ITheHomePresenter;
import com.example.wtl.ttms_hdd.TheHome.presenter.TheHomePresenterCompl;
import com.example.wtl.ttms_hdd.TheHome.presenter.adapter.HotShowAdapter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页fragment
 * Created by WTL on 2018/6/8.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{

    private Banner broadcast;
    private View view;
    private ITheHomePresenter presenter;
    /**
    * 正在热映
    * */
    private RecyclerView nowhot_show;
    /**
    * 即将上映
    * */
    private RecyclerView will_show;
    /**
    * 跳转入具体展示
    * */
    private TextView show_number;
    private TextView will_number;
    /**
    * 广播
    * */
    private Intent intent;

    private SwipeRefreshLayout refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        Montior();
        if(presenter == null) {
           presenter = new TheHomePresenterCompl(getContext());
        }
        presenter.setHotAdapter(nowhot_show,show_number,broadcast);
        presenter.setWillAdapter(will_show,will_number);
        intent = new Intent("com.example.wtl.ttms_hdd.home_number");
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDate();
            }
        });
        return view;
    }

    private void Montior() {
        broadcast = view.findViewById(R.id.broadcast);
        nowhot_show = view.findViewById(R.id.nowhot_show);
        will_show = view.findViewById(R.id.will_show);
        show_number = view.findViewById(R.id.show_number);
        will_number = view.findViewById(R.id.will_number);
        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeResources(android.R.color.holo_red_light);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        nowhot_show.setLayoutManager(manager);

        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        will_show.setLayoutManager(manager1);

        show_number.setOnClickListener(this);
        will_number.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_number:
                intent.putExtra("state","now");
                getContext().sendBroadcast(intent);
                break;
            case R.id.will_number:
                intent.putExtra("state","will");
                getContext().sendBroadcast(intent);
                break;
        }
    }

    private void refreshDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(presenter == null) {
                            presenter = new TheHomePresenterCompl(getContext());
                        }
                        presenter.setHotAdapter(nowhot_show,show_number,broadcast);
                        presenter.setWillAdapter(will_show,will_number);
                        refresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}