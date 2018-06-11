package com.example.wtl.ttms_hdd.BuyTicket.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wtl.ttms_hdd.BuyTicket.presenter.BuyTicketPresentCompl;
import com.example.wtl.ttms_hdd.BuyTicket.presenter.IBuyTicketPresenter;
import com.example.wtl.ttms_hdd.R;
import com.example.wtl.ttms_hdd.Tool.HideScreenTop;

public class BuyTicketActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 返回
     * */
    private ImageView buyback;
    /**
     * 展示海报
     * */
    private ImageView ticket_img;
    /**
     * 电影名
     * */
    private TextView buy_name;
    /**
     * 类型
     * */
    private TextView buy_type;
    /**
     * 时长
     * */
    private TextView buy_durtion;
    /**
    * 初始化接口
    * */
    private IBuyTicketPresenter presenter = null;
    /**
    * 详情
    * */
    private TextView text_details;
    /**
    * 背景图
    * */
    private ImageView showback;
    /**
    * 蒙层
    * */
    private RelativeLayout top;
    /**
    * 展示时间
    * */
    private RecyclerView data_show;
    /**
    * 展示计划
    * */
    private RecyclerView show_plan;

    private String Id;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        HideScreenTop.HideScreenTop(getWindow());
        String name = getIntent().getStringExtra("name");
        Id = getIntent().getStringExtra("Id");
        time = getIntent().getStringExtra("time");
        Montior();
        if(presenter == null) {
            presenter = new BuyTicketPresentCompl(this);
        }
        presenter.showDetail(name,ticket_img,buy_name,buy_type,buy_durtion,text_details,showback);
    }

    private void Montior() {
        ticket_img = (ImageView) findViewById(R.id.ticket_img);
        Glide.with(this)
                .load("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=178339431,3551923999&fm=27&gp=0.jpg")
                .into(ticket_img);
        buy_name = (TextView) findViewById(R.id.buy_name);
        buy_type = (TextView) findViewById(R.id.buy_type);
        buy_durtion = (TextView) findViewById(R.id.buy_durtion);
        buyback = (ImageView) findViewById(R.id.buyback);
        text_details = (TextView) findViewById(R.id.text_details);
        showback = (ImageView) findViewById(R.id.showback);
        top = (RelativeLayout) findViewById(R.id.top);
        data_show = (RecyclerView) findViewById(R.id.data_show);
        show_plan = (RecyclerView) findViewById(R.id.show_plan);

        top.getBackground().setAlpha(240);

        buyback.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        data_show.setLayoutManager(manager);

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        show_plan.setLayoutManager(manager1);

        if(presenter == null) {
            presenter = new BuyTicketPresentCompl(this);
        }
        presenter.showPlanText(data_show,show_plan,Integer.parseInt(Id),time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buyback:
                finish();
                overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        overridePendingTransition(R.anim.activity_right_out, R.anim.activity_right_in);
        return super.onKeyDown(keyCode, event);
    }
}
