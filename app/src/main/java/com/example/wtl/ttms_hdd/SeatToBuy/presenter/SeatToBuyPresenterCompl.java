package com.example.wtl.ttms_hdd.SeatToBuy.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.wtl.ttms_hdd.NetTool.CreateRetrofit;
import com.example.wtl.ttms_hdd.SeatToBuy.model.SeatModel;
import com.example.wtl.ttms_hdd.SeatToBuy.view.SeatView;
import com.example.wtl.ttms_hdd.Tool.FileOperate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private int seatColNumber = 0;
    private int seatRowNumber = 0;

    private List<Integer> rowList = new ArrayList<>();
    private List<Integer> columnList = new ArrayList<>();

    @Override
    public void getSeatNumber(final SeatView seats,String threaterId) {
        GetSeat_Intenerface request = CreateRetrofit.requestRetrofit(FileOperate.readFile(context)).create(GetSeat_Intenerface.class);
        Call<SeatModel> call = request.getSeatDate(Integer.parseInt(threaterId));
        Log.d("qaaaaaaaa",threaterId);
        call.enqueue(new Callback<SeatModel>() {
            @Override
            public void onResponse(Call<SeatModel> call, Response<SeatModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 200) {
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                int seatId = response.body().getData().get(i).getSeatId();
                                int theaterId = response.body().getData().get(i).getTheaterId();
                                boolean status = response.body().getData().get(i).isStatus();
                                seatRowNumber = response.body().getData().get(i).getSeatRowNumber();
                                seatColNumber = response.body().getData().get(i).getSeatColNumber();
                                if(!status) {
                                    rowList.add(seatRowNumber-1);
                                    columnList.add(seatColNumber-1);
                                }
                            }
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seats.setData(seatRowNumber,seatColNumber);
                                    seats.setSeatChecker(new SeatView.SeatChecker() {
                                        @Override
                                        public boolean isValidSeat(int row, int column) {
                                            return true;
                                        }

                                        @Override
                                        public boolean isSold(int row, int column) {
                                            for(int i = 0 ; i < rowList.size() ; i++) {
                                                if(row==rowList.get(i) && column==columnList.get(i)) {
                                                    return true;
                                                }
                                            }
                                            return false;
                                        }

                                        @Override
                                        public void checked(int row, int column) {

                                        }

                                        @Override
                                        public void unCheck(int row, int column) {

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
    }
}
