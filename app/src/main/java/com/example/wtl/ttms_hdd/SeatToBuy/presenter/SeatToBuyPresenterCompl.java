package com.example.wtl.ttms_hdd.SeatToBuy.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wtl.ttms_hdd.NetTool.CreateRetrofit;
import com.example.wtl.ttms_hdd.R;
import com.example.wtl.ttms_hdd.SeatToBuy.model.GetTicketModel;
import com.example.wtl.ttms_hdd.SeatToBuy.model.IsBuyTicketModel;
import com.example.wtl.ttms_hdd.SeatToBuy.model.SeatModel;
import com.example.wtl.ttms_hdd.SeatToBuy.presenter.adapter.YouTicketAdapter;
import com.example.wtl.ttms_hdd.SeatToBuy.view.drawView.SeatView;
import com.example.wtl.ttms_hdd.Tool.FileOperate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 座位逻辑实现类
 * Created by WTL on 2018/6/11.
 */

public class SeatToBuyPresenterCompl implements ISeatToBuyPresenter {

    private Context context;

    public SeatToBuyPresenterCompl(Context context) {
        this.context = context;
    }

    /**
     * 初始化行列号
     */
    private int seatColNumber = 0;
    private int seatRowNumber = 0;
    /**
     * 已经售出的行列数
     */
    private List<Integer> rowList = new ArrayList<>();
    private List<Integer> columnList = new ArrayList<>();
    /**
     * 座位表
     */
    private SeatView seats;
    /**
     * 获取选中的座位
     */
    List<IsBuyTicketModel> modelList = new ArrayList<>();
    /**
     * 购买的数量
     */
    private int number = 0;
    /**
     * 显示适配器
     */
    private YouTicketAdapter adapter;

    private Animation up;
    private Animation down;

    @Override
    public void getSeatNumber(String goodId, final SeatView seats, String threaterId, final LinearLayout chooseon, final RecyclerView isbuy, final TextView paymoney, final TextView select_Prompt, final int price) {
        up = AnimationUtils.loadAnimation(context, R.anim.setting_down);
        down = AnimationUtils.loadAnimation(context, R.anim.setting_up);
        GetSeat_Intenerface request = CreateRetrofit.requestRetrofit(FileOperate.readFile(context)).create(GetSeat_Intenerface.class);
        Call<SeatModel> call = request.getSeatDate(Integer.parseInt(threaterId));
        this.seats = seats;
        call.enqueue(new Callback<SeatModel>() {
            @Override
            public void onResponse(final Call<SeatModel> call, Response<SeatModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 200) {
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                boolean status = response.body().getData().get(i).isStatus();
                                seatRowNumber = response.body().getData().get(i).getSeatRowNumber();
                                seatColNumber = response.body().getData().get(i).getSeatColNumber();
                                if (!status) {
                                    rowList.add(seatRowNumber - 1);
                                    columnList.add(seatColNumber - 1);
                                }
                            }
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seats.setData(seatRowNumber, seatColNumber);
                                    seats.setSeatChecker(new SeatView.SeatChecker() {
                                        @Override
                                        public boolean isValidSeat(int row, int column) {
                                            return true;
                                        }

                                        @Override
                                        public boolean isSold(int row, int column) {
                                            for (int i = 0; i < rowList.size(); i++) {
                                                if (row == rowList.get(i) && column == columnList.get(i)) {
                                                    return true;
                                                }
                                            }
                                            return false;
                                        }

                                        @Override
                                        public void checked(int row, int column) {
                                            if (number == 0) {
                                                select_Prompt.setVisibility(View.GONE);
                                                select_Prompt.startAnimation(down);
                                                chooseon.setVisibility(View.VISIBLE);
                                                chooseon.startAnimation(up);
                                                String locat = String.valueOf(row + 1) + "排" + String.valueOf(column + 1) + "座";
                                                IsBuyTicketModel model = new IsBuyTicketModel(locat, String.valueOf(price));
                                                modelList.add(model);
                                                adapter = new YouTicketAdapter(context, modelList, seats);
                                                isbuy.setAdapter(adapter);
                                            } else {
                                                String locat = String.valueOf(row + 1) + "排" + String.valueOf(column + 1) + "座";
                                                IsBuyTicketModel model = new IsBuyTicketModel(locat, String.valueOf(price));
                                                adapter.additem(model);
                                            }
                                            number++;
                                            paymoney.setText(String.valueOf(price * number) + " 确认选座");
                                        }

                                        @Override
                                        public void unCheck(int row, int column) {
                                            number--;
                                            if (number == 0) {
                                                select_Prompt.setVisibility(View.VISIBLE);
                                                select_Prompt.startAnimation(up);
                                                chooseon.setVisibility(View.GONE);
                                                chooseon.startAnimation(down);
                                            }
                                            String locat = String.valueOf(row + 1) + "排" + String.valueOf(column + 1) + "座";
                                            adapter.removeitem(-1, locat);
                                            paymoney.setText(String.valueOf(price * number) + " 确认选座");
                                        }

                                        @Override
                                        public String[] checkedSeatTxt(int row, int column) {
                                            return new String[0];
                                        }
                                    });
                                }
                            });

                        } else {
                            Log.e("onFailure", response.body().getMsg());
                        }
                    } else {
                        Log.e("onFailure", "获取数据为空！！！");
                    }
                } else {
                    Log.e("onFailure", "获取失败！！！");
                }
            }

            @Override
            public void onFailure(Call<SeatModel> call, Throwable t) {
                Log.e("onFailure", t.getMessage() + "失败");
            }
        });

        Call<GetTicketModel> call1 = request.getTicketMessage(Integer.parseInt(goodId));
        call1.enqueue(new Callback<GetTicketModel>() {
            @Override
            public void onResponse(Call<GetTicketModel> call, Response<GetTicketModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 200) {


                        } else {
                            Log.e("onFailure", response.body().getMsg());
                        }
                    } else {
                        Log.e("onFailure", "获取数据为空！！！");
                    }
                } else {
                    Log.e("onFailure", "获取失败！！！");
                }
            }

            @Override
            public void onFailure(Call<GetTicketModel> call, Throwable t) {
                Log.e("onFailure", t.getMessage() + "失败");
            }
        });
    }
}
