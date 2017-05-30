package com.namewu.booksalesuper.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namewu.booksalesuper.R;
import com.namewu.booksalesuper.Utils.L;
import com.namewu.booksalesuper.Utils.SharePreferenceUtil;
import com.namewu.booksalesuper.Utils.T;
import com.namewu.booksalesuper.login.LoginActivity;
import com.namewu.booksalesuper.onlinedata.Orderdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.namewu.booksalesuper.R.layout.list_context;

public class MainActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener,View.OnClickListener{
    private String TAG="MainActivity";
    private Context mcontext;
    private ArrayList<Orderdata> listdata;
    private RecyclerView rv;
    private OrderRecyclerViewAdapter adapter;
    private boolean status=false;
    private SwipeRefreshLayout refresh;
    private TextView text_done;
    private TextView text_will;
    private ImageView imghead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext=this;
        initView();
    }

    private void initView() {
        rv= (RecyclerView) findViewById(R.id.recycle_order);
        refresh= (SwipeRefreshLayout) findViewById(R.id.main_refresh);
        text_done= (TextView) findViewById(R.id.order_done);
        text_will= (TextView) findViewById(R.id.order_will);
        imghead= (ImageView) findViewById(R.id.order_imghead);
        refresh.setColorSchemeResources(R.color.purple_level,R.color.green_level,
                R.color.blue_level, R.color.orange_level);
        refresh.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        //swipeRefreshLayout.setProgressBackgroundColor(R.color.red); // 设定下拉圆圈的背景
        refresh.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
        text_done.setOnClickListener(this);
        text_will.setOnClickListener(this);
        imghead.setOnClickListener(this);
    }
    private boolean updataContext() {
        L.i(TAG,"排序");
        final SimpleDateFormat sdf=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        final Date[] data1 = {null};
        final Date[] data2 = {null};
        for (int i=0;i<listdata.size();i++){
            Log.i(TAG,listdata.get(i).getCreatedAt()+"日期");
        }
        Comparator<Orderdata> comparator = new Comparator<Orderdata>(){
            public int compare(Orderdata s1, Orderdata s2) {
                //排序日期
                try {
                    data1[0] =sdf.parse(s1.getCreatedAt());
                    data2[0] =sdf.parse(s2.getCreatedAt());
                } catch (ParseException e) {
                    Log.i(TAG,"wenti");
                    e.printStackTrace();
                }
                if(data1[0].getTime()> data2[0].getTime()){
                    return -1;
                }else {
                    return 1;
                }
            }
        };
        if(listdata.size()>1){
            Collections.sort(listdata,comparator);
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(listdata==null){
          updataListData();
        }
    }

    private void updataListData() {
        BmobQuery<Orderdata> query=new BmobQuery<>();
        query.addWhereEqualTo("status",status);
        query.setLimit(50);
        query.findObjects(new FindListener<Orderdata>() {
            @Override
            public void done(List<Orderdata> list, BmobException e) {
                if(e==null){
                    T.showShot(mcontext,"加载完成！");
                    listdata= (ArrayList<Orderdata>) list;
                    updataContext();
                    adapter=new OrderRecyclerViewAdapter(mcontext,listdata);
                    StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                    rv.setLayoutManager(staggeredGridLayoutManager);
                    adapter.setItemContextOnclickListenner(new OrderRecyclerViewAdapter.ItemContextnclickListenner() {
                        @Override
                        public void onitemclickcontext(OrderRecyclerViewAdapter.MyViewHolder viewHolder, int postion) {
                            Intent it=new Intent(MainActivity.this,DetailOrderActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("order",listdata.get(postion));
                            it.putExtras(bundle);
                            startActivity(it);
                        }
                    });
                    rv.setAdapter(adapter);
                }else {

                }
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        updataListData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_will:
                if(status){
                    text_will.setTextColor(getResources().getColor(R.color.gold_yellow));
                    text_will.setBackgroundResource(R.drawable.reward_wallet_top_bg_normal2);
                    text_done.setTextColor(getResources().getColor(R.color.white_smoke));
                    text_done.setBackgroundResource(R.drawable.reward_wallet_top_bg_press);
                    status=false;
                    refresh.post(new Runnable() {
                        @Override
                        public void run() {
                            refresh.setRefreshing(true);
                        }
                    });
                    updataListData();
                };break;
            case R.id.order_done:
                if(!status){
                    text_done.setTextColor(getResources().getColor(R.color.gold_yellow));
                    text_done.setBackgroundResource(R.drawable.reward_wallet_top_bg_press2);
                    text_will.setTextColor(getResources().getColor(R.color.white_smoke));
                    text_will.setBackgroundResource(R.drawable.reward_wallet_top_bg_normal);
                    status=true;
                    refresh.post(new Runnable() {
                        @Override
                        public void run() {
                            refresh.setRefreshing(true);
                        }
                    });
                    updataListData();
                };break;
            case R.id.order_imghead: SharePreferenceUtil.putSettingDataBoolean(mcontext,SharePreferenceUtil.AUTOLOGIN,false);
                BmobUser.logOut();
                Intent it=new Intent(MainActivity.this, LoginActivity.class);startActivity(it);MainActivity.this.finish();break;
        }
    }
}
