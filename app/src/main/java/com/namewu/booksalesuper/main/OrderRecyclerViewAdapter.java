package com.namewu.booksalesuper.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.namewu.booksalesuper.R;
import com.namewu.booksalesuper.Utils.MyUpload;
import com.namewu.booksalesuper.onlinedata.Orderdata;

import java.util.ArrayList;



/**
 * Created by Administrator on 2017/1/31.
 */

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.MyViewHolder>{
    private String TAG="WangContextRecyclerViewAdapter";
    private final int NORMAL_TYPE=0;
    private final int FOOT_TYPE=1;
    MyUpload myUpload;
    private LayoutInflater mlayoutInflater;
    private ArrayList<Orderdata> list_context;
    private int should_max=0;
    private Context mcontext;
    private ItemContextnclickListenner itemContextOnclickListenner=null;
//    public WuContextRecyclerViewAdapter(Context mcontext, ArrayList<String> list_context, ArrayList<String> list_time, ArrayList<Integer> list_level, ArrayList<String> list_writer, ArrayList<Integer> list_num, ArrayList<String> list_numURL){
//        mlayoutInflater=LayoutInflater.from(mcontext);
//        this.mcontext=mcontext;
//        this.list_context=list_context;
//        this.list_time=list_time;
//        this.list_level=list_level;
//        this.list_writer=list_writer;
//        this.list_num=list_num;
//        this.list_numURL=list_numURL;
//        myUpload=new MyUpload(mcontext);
//        should_max=list_context.size();
//    }
    public OrderRecyclerViewAdapter(Context mcontext, ArrayList<Orderdata> list_context){
        mlayoutInflater=LayoutInflater.from(mcontext);
        this.mcontext=mcontext;
        this.list_context=list_context;
        myUpload=new MyUpload(mcontext);
        should_max=list_context.size();
    }
    public interface ItemContextnclickListenner{
        public void onitemclickcontext(MyViewHolder viewHolder, int postion);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==NORMAL_TYPE){
            View view=LayoutInflater.from(mcontext).inflate(R.layout.listitem_recyler_order,null);
            return new MyViewHolder(view,NORMAL_TYPE);
        }
        return null;
//        View footview= LayoutInflater.from(mcontext).inflate(R.layout.tab_remark_list_bottom,parent,false);
//        return new MyViewHolder(footview,FOOT_TYPE);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder wuViewHolder, final int position) {
        wuViewHolder.username.setText(list_context.get(position).getName());
        wuViewHolder.address.setText(list_context.get(position).getAddress());
        wuViewHolder.time.setText(list_context.get(position).getCreatedAt().substring(0,16));
        myUpload.download_asynchronous_head("booksalesystem", "headimg/" + list_context.get(position).getSubmitid(),wuViewHolder.img_head);
        if(itemContextOnclickListenner!=null){
            wuViewHolder.address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemContextOnclickListenner.onitemclickcontext(wuViewHolder,position);
                }
            });
        }
//        }
    }

    public ItemContextnclickListenner getItemContextOnclickListenner() {
        return itemContextOnclickListenner;
    }

    public void setItemContextOnclickListenner(ItemContextnclickListenner itemContextOnclickListenner) {
        this.itemContextOnclickListenner = itemContextOnclickListenner;
    }

    @Override
    public int getItemCount() {
        return should_max;
    }
    @Override
    public int getItemViewType(int position) {
//        if (position == should_max-1) {
//            return FOOT_TYPE;
//        }
        return NORMAL_TYPE;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView img_head;
        private TextView time;
        private TextView username;
        private TextView address;
        public MyViewHolder(View itemView,int viewtype) {
            super(itemView);
            if(viewtype==NORMAL_TYPE){
                img_head= (ImageView) itemView.findViewById(R.id.header_headimg);
                time= (TextView) itemView.findViewById(R.id.order_time);
                username= (TextView) itemView.findViewById(R.id.order_username);
                address= (TextView) itemView.findViewById(R.id.order_address);
            }
        }
    }
}
