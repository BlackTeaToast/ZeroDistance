package com.yoyoyee.zerodistance.helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.AskActivity;

/**
 * Created by 楊霖村 on 2016/4/6.
 */
public class QAAdapter  extends RecyclerView.Adapter<QAAdapter.ViewHolder> {
    //字型大小
    public  float size;
    private String[] q_Q_Titletext, q_Qtimetext, q_Qnametext, q_Qcontenttext, a_A_Titletext, a_Atimetext, a_Acontenttext;
    private int[] q_a_ID;
    private Boolean publisher,isGroup;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView q_Q_Title, q_Qname, q_Qtime, q_Qcontent;
        public TextView a_A_Title,  a_Atime, a_Acontent;
        public LinearLayout a_Card_all,q_Card_all;
        public LinearLayout layoutcontent;
        public TextView number;
        public ViewHolder(View v) {
            super(v);
            q_Q_Title = (TextView) v.findViewById(R.id.q_Q_Title);
            q_Qname = (TextView) v.findViewById(R.id.q_Qname);
            q_Qtime = (TextView) v.findViewById(R.id.q_Qtime);
            q_Qcontent = (TextView) v.findViewById(R.id.q_Qcontent);
            a_A_Title = (TextView) v.findViewById(R.id.a_A_Title);
            a_Atime = (TextView) v.findViewById(R.id.a_Atime);
            a_Acontent = (TextView) v.findViewById(R.id.a_Acontent);
            number = (TextView) v.findViewById(R.id.textViewNumber);

            a_Card_all = (LinearLayout) v.findViewById(R.id.a_Card_all);
            q_Card_all = (LinearLayout) v.findViewById(R.id.q_Card_all);
            layoutcontent= (LinearLayout) v.findViewById(R.id.q_CardContent);




        }
    }

    public QAAdapter(/*String[] q_Q_Titletext ,*/int[] q_a_ID,float size,Boolean publisher,Boolean isGroup,String[] q_Qtimetext ,String[] q_Qnametext ,String[] q_Qcontenttext, /*String[] a_A_Titletext,*/String[] a_Atimetext,String[] a_Acontenttext) {
      //  this.q_Q_Titletext = q_Q_Titletext;
        this.q_Qtimetext = q_Qtimetext;
        this.q_Qnametext = q_Qnametext;
        this.q_Qcontenttext = q_Qcontenttext;
      //  this.a_A_Titletext = a_A_Titletext;
        this.a_Atimetext = a_Atimetext;
        this.a_Acontenttext = a_Acontenttext;
        this.publisher =publisher;
        this.size =size;
        this.q_a_ID =q_a_ID;
        this.isGroup =isGroup;

     //   this.a_Anametext = a_Anametext;
    }

    @Override
    public QAAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_qanda, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.q_Q_Title.setText(R.string.qanda_Q);
        holder.q_Qname.setText(q_Qnametext[position]);
        holder.q_Qtime.setText(q_Qtimetext[position]);
        holder.q_Qcontent.setText(q_Qcontenttext[position]);
        holder.a_A_Title.setText(R.string.qanda_A);
       // holder.a_Aname.setText(a_Atimetext[position]);
        holder.a_Atime.setText(a_Atimetext[position]);
        holder.a_Acontent.setText(a_Acontenttext[position]);
        holder.number.setText(String.valueOf(position+1));
        if (a_Acontenttext[position].equals("null")){
            holder.a_Card_all.setVisibility(View.GONE);
            holder.q_Card_all.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    layOutClick(v,q_a_ID[position],a_Acontenttext[position]);
                    return false;
                }
            });
        }
        else {
            holder.a_Card_all.setVisibility(View.VISIBLE);
            holder.q_Card_all.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    layOutClick(v, q_a_ID[position], a_Acontenttext[position]);
                    return false;
                }
            });
            holder.a_Card_all.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    layOutClick(v,q_a_ID[position],a_Acontenttext[position]);
                    return false;
                }
            });
        }

        //設定字型大小
        //Q
        holder.q_Q_Title.setTextSize(size + 9);
        //Q者
        holder.q_Qname.setTextSize(size);
        //Q時
        holder.q_Qtime.setTextSize(size);
        //Q內容
        holder.q_Qcontent.setTextSize(size + 5);
         //A
        holder.a_A_Title.setTextSize(size + 9);
        //A時
        holder.a_Atime.setTextSize(size + 0);
        //A內容
        holder.a_Acontent.setTextSize(size + 5);
    }

    @Override
    public int getItemCount() {
    return q_Qnametext.length;
    }

    /**
     * 按下按鈕後，會進行判斷，是否為發文者，是則可以進行回文，不適則否
     * @param v
     */
    public void layOutClick(final View v,int qaID,String a_Acontenttext){
        if (publisher) {
           // Toast.makeText(v.getContext(),"我是發文者",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AskActivity.class);
            intent.putExtra("isGroup",isGroup);
            intent.putExtra("isAsk",false);
            intent.putExtra("q_a_ID",qaID);
            intent.putExtra("content",a_Acontenttext);
            v.getContext().startActivity(intent);
        }
        else{
            //Toast.makeText(v.getContext(), "我是看的人", Toast.LENGTH_SHORT).show();
        }
       /*final String[] items = new String[]{"北京","上海","深圳"};
        new AlertDialog.Builder(v.getContext()).setTitle("城市列表").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        Toast.makeText(v.getContext(), "您选中了："+items[0], Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(v.getContext(), "您选中了："+items[1], Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(v.getContext(), "您选中了："+items[2], Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }).show();*/
    }

}