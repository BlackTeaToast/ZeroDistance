package com.yoyoyee.zerodistance.helper;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/4/6.
 */
public class QAAdapter  extends RecyclerView.Adapter<QAAdapter.ViewHolder> {
    private String[] q_Q_Titletext, q_Qtimetext, q_Qnametext, q_Qcontenttext, a_A_Titletext, a_Atimetext, a_Acontenttext;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView q_Q_Title, q_Qname, q_Qtime, q_Qcontent;
        public TextView a_A_Title,  a_Atime, a_Acontent;
        public ViewHolder(View v) {
            super(v);

            q_Q_Title = (TextView) v.findViewById(R.id.q_Q_Title);
            q_Qname = (TextView) v.findViewById(R.id.q_Qname);
            q_Qtime = (TextView) v.findViewById(R.id.q_Qtime);
            q_Qcontent = (TextView) v.findViewById(R.id.q_Qcontent);
            a_A_Title = (TextView) v.findViewById(R.id.a_A_Title);
         //   a_Aname = (TextView) v.findViewById(R.id.a_Aname);
            a_Atime = (TextView) v.findViewById(R.id.a_Atime);
            a_Acontent = (TextView) v.findViewById(R.id.a_Acontent);
        }
    }

    public QAAdapter(String[] q_Q_Titletext ,String[] q_Qtimetext ,String[] q_Qnametext ,String[] q_Qcontenttext, String[] a_A_Titletext,String[] a_Atimetext,String[] a_Acontenttext) {
        this.q_Q_Titletext = q_Q_Titletext;
        this.q_Qtimetext = q_Qtimetext;
        this.q_Qnametext = q_Qnametext;
        this.q_Qcontenttext = q_Qcontenttext;
        this.a_A_Titletext = a_A_Titletext;
        this.a_Atimetext = a_Atimetext;
        this.a_Acontenttext = a_Acontenttext;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.q_Q_Title.setText(q_Q_Titletext[position]);
        holder.q_Qname.setText(q_Qtimetext[position]);
        holder.q_Qtime.setText(q_Qnametext[position]);
        holder.q_Qcontent.setText(q_Qcontenttext[position]);
        holder.a_A_Title.setText(a_A_Titletext[position]);
       // holder.a_Aname.setText(a_Atimetext[position]);
        holder.a_Atime.setText(a_Atimetext[position]);
        holder.a_Acontent.setText(a_Acontenttext[position]);
    }

    @Override
    public int getItemCount() {
        return q_Qnametext.length;
    }
}