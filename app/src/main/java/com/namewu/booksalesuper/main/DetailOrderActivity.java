package com.namewu.booksalesuper.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.namewu.booksalesuper.R;
import com.namewu.booksalesuper.Utils.L;
import com.namewu.booksalesuper.Utils.T;
import com.namewu.booksalesuper.login.LoginActivity;
import com.namewu.booksalesuper.onlinedata.Orderdata;
import com.namewu.booksalesuper.onlinedata.WZCLUser;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/28.
 */

public class DetailOrderActivity extends Activity implements View.OnClickListener{
    private String TAG="DetailOrderActivity";
    private Context mcontext;
    private Orderdata order;
    private ListView listview;
    private TextView all;
    private OrderListAdapter orderadapter;
    private TextView text_ok;
    private TextView text_name;
    private TextView text_sex;
    private TextView text_phone;
    private TextView text_address;
    private TextView text_spueruser;
    private ArrayList<String> data_name=new ArrayList<>();
    private ArrayList<Integer> data_preice=new ArrayList<>();
    private ArrayList<Integer> data_count=new ArrayList<>();
    private ArrayList<String> data_address=new ArrayList<>();
    private WZCLUser bmobUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderbuy);
        mcontext=this;
        recivedata();
        initView();
        updataView();
    }

    private void updataView() {
        text_name.setText(order.getName());
        text_sex.setText(order.getSex());
        text_address.setText(order.getAddress());
        text_phone.setText(order.getPhone());
        all.setText(order.getAllmoney());
        String doid=order.getDoid();
        if(doid!=null&&doid.length()>0&&!doid.equals("0")){
            text_spueruser.setText(doid);
        }
        ArrayList<String> listorder=order.getList_info();
        for(int i=0;i<listorder.size();i++){
            String one=listorder.get(i);
            String[] two=one.split("\\*");
            data_name.add(two[0]);
            data_preice.add(Integer.valueOf(two[1]));
            data_count.add(Integer.valueOf(two[2]));
        }
        orderadapter=new OrderListAdapter(mcontext,data_name,data_preice,data_count);
        listview.setAdapter(orderadapter);
    }

    private void initView() {
        listview= (ListView) findViewById(R.id.activity_orderbuy_listview);
        all= (TextView) findViewById(R.id.activity_orderbuy_allmoney);
        text_name= (TextView) findViewById(R.id.activity_orderbuy_oname);
        text_sex= (TextView) findViewById(R.id.activity_orderbuy_osir);
        text_phone= (TextView) findViewById(R.id.activity_orderbuy_ophonenum);
        text_address= (TextView) findViewById(R.id.activity_orderbuy_oaddress);
        text_ok= (TextView) findViewById(R.id.activity_orderbuy_ok);
        text_spueruser= (TextView) findViewById(R.id.activity_orderbuy_superuser);
        text_ok.setOnClickListener(this);
    }

    private void recivedata() {
        Intent it=getIntent();
        Bundle bundle=it.getExtras();
        order= (Orderdata) bundle.getSerializable("order");
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
    }

    private void checkUser() {
        bmobUser = BmobUser.getCurrentUser(WZCLUser.class);
        if(bmobUser!=null){

        }else {
            Intent it=new Intent(DetailOrderActivity.this, LoginActivity.class);
            DetailOrderActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_orderbuy_ok:
                Orderdata neworder=new Orderdata();
                neworder.setStatus(true);
                neworder.setDoid(bmobUser.getUsername());
                String address=order.getObjectId();
                neworder.update( address,new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            T.showShot(mcontext,"处理完毕！");
                        }else {
                            T.showShot(mcontext,"处理失败！");
                            L.i(TAG,e.toString());
                        }
                    }
                })
                ;break;
        }
    }
}
