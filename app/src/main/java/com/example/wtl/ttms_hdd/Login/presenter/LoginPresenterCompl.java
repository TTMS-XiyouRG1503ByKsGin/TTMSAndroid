package com.example.wtl.ttms_hdd.Login.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wtl.ttms_hdd.HDDMain.view.Activity.MainActivity;
import com.example.wtl.ttms_hdd.Login.model.LoginResultModel;
import com.example.wtl.ttms_hdd.Login.model.ValidateModel;
import com.example.wtl.ttms_hdd.R;
import com.example.wtl.ttms_hdd.Register.view.RegisterActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登陆业务逻辑层
 * 接口功能实现
 * Created by WTL on 2018/6/4.
 */

public class LoginPresenterCompl implements ILoginPresenter {

    private Context context;

    public LoginPresenterCompl(Context context) {
        this.context = context;
    }
    /**
    * 获取服务器发来的请求头
    * */
    private String sessionId = null;

    @Override
    public void doLogin(String account, String password, String verCode) {
        if (account.equals("") || password.equals("") || verCode.equals("")) {
            Toast.makeText(context, "登陆失败!账号/密码/验证码错误!", Toast.LENGTH_SHORT).show();
        } else {
            /*
            * 将数据封装成map
            * */
            Map<String, String> loginMap = new HashMap<>();
            loginMap.put("account",account);
            loginMap.put("password",password);
            loginMap.put("verCode",verCode);
            /*
            * 将map封装成json数据
            * */
            Gson gson = new Gson();
            String jsonData = gson.toJson(loginMap);
            /*
            * 创建数据body
            * */
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonData);
            /*
            * 向服务器发送数据
            * */
            Call<LoginResultModel> call = CreateRetrofit.requestRetrofit(sessionId).postUser(body);
            /*
            * 异步网络请求
            * */
            call.enqueue(new Callback<LoginResultModel>() {
                @Override
                public void onResponse(Call<LoginResultModel> call, Response<LoginResultModel> response) {
                    if (response.isSuccessful()) {
                        if(response.body()!=null) {
                            if(response.body().getResult()==200 && response.body().getMsg().equals("successful")) {
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                                ((Activity) context).overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
                            } else {
                                Toast.makeText(context, "登陆失败!!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("onFailure", "请求数据为空");
                            Toast.makeText(context, "登陆失败!账号,密码,验证码错误!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("onFailure", "请求失败");
                        Toast.makeText(context, "登陆失败!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResultModel> call, Throwable t) {
                    Log.e("onFailure", t.getMessage() + "失败");
                }
            });
        }
    }

    @Override
    public void doRegister() {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.activity_left_in, R.anim.activity_left_out);
    }

    @Override
    public void clear(EditText edit) {
        edit.setText("");
    }

    @Override
    public void addTextEdit(final EditText edit, final ImageView delete) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                * 不为空时显示清除
                * */
                if (!edit.getText().toString().equals("")) {
                    delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                /*
                * 为空时不显示清除
                * */
                if (edit.getText().toString().equals("")) {
                    delete.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void showValidate(final ImageView image) {
        /*
        * 发送请求
        * */
        Call<ValidateModel> call = CreateRetrofit.requestRetrofit(sessionId).getValidate();
        /*
        * 异步网络请求
        * */
        call.enqueue(new Callback<ValidateModel>() {
            @Override
            public void onResponse(Call<ValidateModel> call, Response<ValidateModel> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    /*
                    * 从请求头获取用户cookie
                    * */
                    sessionId = response.headers().get("Set-Cookie");
                    ValidateModel validate = response.body();
                    if (validate != null) {
                        byte[] arry = Base64.decode(validate.getBase64(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(arry, 0, arry.length);
                        image.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onFailure(Call<ValidateModel> call, Throwable t) {
                Log.e("onFailure", t.getMessage() + "失败");
            }
        });
    }

}
