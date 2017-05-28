package com.namewu.booksalesuper.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.namewu.booksalesuper.R;
import com.namewu.booksalesuper.Utils.L;
import com.namewu.booksalesuper.Utils.T;
import com.namewu.booksalesuper.onlinedata.Orderdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.namewu.booksalesuper.R.layout.list_context;

public class MainActivity extends Activity {
    private String TAG="MainActivity";
    private Context mcontext;
    private ArrayList<Orderdata> listdata;
    private RecyclerView rv;
    private OrderRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mcontext=this;
        initView();
    }

    private void initView() {
        rv= (RecyclerView) findViewById(R.id.recycle_order);
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
        BmobQuery<Orderdata> query=new BmobQuery<>();
        query.addWhereEqualTo("status",false);
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

                        }
                    });
                    rv.setAdapter(adapter);
                }else {

                }
            }
        });
    }
}
