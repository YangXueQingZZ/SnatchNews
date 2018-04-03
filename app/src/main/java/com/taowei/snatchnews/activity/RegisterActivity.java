package com.taowei.snatchnews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taowei.snatchnews.R;
import com.taowei.snatchnews.bean.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_register;
    private Button btn_back;
    private EditText et_user;
    private EditText et_pass;
    final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.i("获取的返回信息", ReturnMessage);
                final LoginResult result = new Gson().fromJson(ReturnMessage, LoginResult.class);
                Log.i("MSGhahahha", result.toString());
                // final String AA = userBean.getMsg();
                final String status = result.getStatus();
                /***
                 * 在此处可以通过获取到的Msg值来判断
                 * 给出用户提示注册成功 与否，以及判断是否用户名已经存在
                 */
                Log.i("MSGhahahha", status);


                if (status.equals("1")) {

                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();


                } else if (status.equals("0")) {

                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                }else if (status.equals("3")) {

                    Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();

                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inItView();
    }


    private void inItView() {

        btn_register = findViewById(R.id.btn_register);
        et_user = findViewById(R.id.et_users);
        et_pass = findViewById(R.id.et_passs);
        btn_back = findViewById(R.id.btn_back);

        btn_register.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:

                    if(checkEdit()) {
                        postRequest(setJson());
                    }


                break;
            case R.id.btn_back:

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;

        }

    }


    private boolean checkEdit() {
        if (et_user.getText().toString().trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
        } else if (et_pass.getText().toString().trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }


    private String setJson() {

        JSONObject person = new JSONObject();

        try {
            person.put("username", et_user.getText().toString());
            person.put("password", et_pass.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return String.valueOf(person);
    }

    /**
     * post请求后台
     * @param
     * @param
     */
    public void postRequest(String data)  {


        RequestBody requestBody = RequestBody.create(JSON, data);

        Log.i("JSON",data);
        //发起请求
        final Request request = new Request.Builder()
                .url("http://13.5ron.com/index/user/register")
                .post(requestBody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    //回调
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();

                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
