package com.namewu.booksalesuper.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.namewu.booksalesuper.R;
import com.namewu.booksalesuper.Utils.L;
import com.namewu.booksalesuper.Utils.MySdcard;
import com.namewu.booksalesuper.Utils.SharePreferenceUtil;
import com.namewu.booksalesuper.Utils.StringLegalUtil;
import com.namewu.booksalesuper.Utils.T;
import com.namewu.booksalesuper.customView.XuProcessDialog2;
import com.namewu.booksalesuper.main.MainActivity;
import com.namewu.booksalesuper.onlinedata.Alldata;
import com.namewu.booksalesuper.onlinedata.Talkdata;
import com.namewu.booksalesuper.onlinedata.WZCLUser;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by Administrator on 2017/3/16.
 */

public class LoginActivity extends Activity implements View.OnClickListener{
    private String TAG="LoginActivity";
    private EditText edit_userid;
    private EditText edit_password;
    private Button button_ok;
    private Button button_sign;
    private ImageView img_on;
    private boolean button_status=true;
    private Context mcontext;
    private XuProcessDialog2 xuloginprocess;
    private MySdcard mySdcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mcontext=this;
        initView();
        mbmobinitdata();
        mySdcard=new MySdcard();
        mySdcard.initWuSdcard(mcontext);
    }

    private void initView() {
        button_ok= (Button) findViewById(R.id.button_ok);
        img_on= (ImageView) findViewById(R.id.login_img_on);
        edit_userid= (EditText) findViewById(R.id.login_uesrid);
        edit_password= (EditText) findViewById(R.id.login_password);
        img_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!button_status){
                    img_on.setImageResource(R.mipmap.switch_off);
                    SharePreferenceUtil.putSettingDataBoolean(mcontext,SharePreferenceUtil.AUTOLOGIN,false);
                    button_status=true;
                }else {
                    img_on.setImageResource(R.mipmap.switch_on);
                    button_status=false;
                    SharePreferenceUtil.putSettingDataBoolean(mcontext,SharePreferenceUtil.AUTOLOGIN,true);
                }

            }
        });
        button_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ok:gologin();break;
        }
    }

    private void gologin() {
        //uploadtest();
        button_ok.setEnabled(false);
        boolean jundge_legal=true;
        String id=edit_userid.getText().toString().trim();
        String password=edit_password.getText().toString().trim();
        if(!StringLegalUtil.isHaveLength(id)){
            edit_userid.setError("请输入手机号！");
            jundge_legal=false;
        }else if(!StringLegalUtil.isCorrectPhonenum(id)){
            edit_userid.setError("请输入正确的手机号！");
            jundge_legal=false;
        }
        if(jundge_legal){
            xuloginprocess=new XuProcessDialog2(mcontext);xuloginprocess.show();
            WZCLUser.loginByAccount(id,password, new LogInListener<WZCLUser>() {
                @Override
                public void done(WZCLUser xuUser, BmobException e) {
                    if(xuUser!=null) {
                        Intent it=new Intent(LoginActivity.this,MainActivity.class);startActivity(it);
                        LoginActivity.this.finish();
                    }else {
                        T.showShot(mcontext,"登录失败！");
                        button_ok.setEnabled(true);
                        L.i(TAG,e.toString());
                    }
                    xuloginprocess.dismiss();
                }
            });
        }else {
            button_ok.setEnabled(true);
        }
    }

    private void uploadtest() {
//        Bookdata fooddata=new Bookdata("西游记","传统文化四大名著",true,97,4,new ArrayList<String>());
//        fooddata.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if(e==null){
//                    L.i(TAG,"成功上传更新");
//                }
//            }
//        });
        Alldata alldata=new Alldata();
        ArrayList<String> list=new ArrayList<>();
        list.add("7wFr555D");
        list.add("nNi52223");
        list.add("Fm0S111i");
        list.add("e26247a93e");
        alldata.setList_book(list);
        alldata.setList_talk(list);
        alldata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
        Talkdata talkdata=new Talkdata("18249028972","这本书还是挺好挺实惠的",0,4,"哈尔滨");
        talkdata.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
//        Hoteldata hoteldata=new Hoteldata("易蓝大酒店","地下创业园楼下快来吧",true,180,2,new ArrayList<String>());
//        hoteldata.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//            }
//        });
//        Traveldata traveldata=new Traveldata("上海一日游","地下创业园楼下快来吧",true,new ArrayList<String>());
//        traveldata.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//            }
//        });
//        Remakdata remakdata=new Remakdata("看样子挺好的样子","18249028972");
//        remakdata.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//            }
//        });
//       Alldata alldata=new Alldata();
//        ArrayList<String> list_spot=new ArrayList<>();
//        list_spot.add("99d05919b6");
//        list_spot.add("40bbf211d6");
//        list_spot.add("5ad99134f8");
//        list_spot.add("ddd0c4a445");
//        list_spot.add("5bf37ec134");
//        list_spot.add("f707e19325");
//        list_spot.add("3a1f70cc3e");
//        list_spot.add("dd356d5a9a");
//        list_spot.add("9c142046ec");
//        alldata.setList_food(new ArrayList<String>());
//        alldata.setList_hotel(new ArrayList<String>());
//        alldata.setList_spot(list_spot);
//        alldata.setList_travel(new ArrayList<String>());
//        alldata.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if(e==null){
//                    L.i(TAG,"上传成功！");
//                }
//            }
//        });
    }

    private void mbmobinitdata() {
        Bmob.initialize(this, "f2cbab816413a772b63041885cff816b");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("f2cbab816413a772b63041885cff816b")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }
    private boolean checkuser() {
        WZCLUser bmobUser = BmobUser.getCurrentUser(WZCLUser.class);
        if(bmobUser != null){
            // 允许用户使用应用
            //  String name= (String) BmobUser.getObjectByKey("treename");
            //  text_username.setText(name);
            userrun();
            return true;
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
            return false;
        }
    }

    private void userrun() {
        Intent it=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(it);
        this.finish();
    }

    @Override
    protected void onResume() {
        if(SharePreferenceUtil.getSettingDataBoolean(mcontext,SharePreferenceUtil.AUTOLOGIN)){
            if(checkuser()){

            }
        }else {
            img_on.setImageResource(R.mipmap.switch_off);
        }
        super.onResume();
    }
}
