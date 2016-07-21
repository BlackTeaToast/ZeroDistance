package com.yoyoyee.zerodistance.menuDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/7/11.
 */
public class SearchFriend extends Dialog {
    Button domakefri, docancel;
    EditText friname;
    int way;
    public SearchFriend(Context context, int way) {
        super(context);
        this.way = way;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_searchfriend);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        domakefri= (Button)findViewById(R.id.domakefri);
        docancel = (Button)findViewById(R.id.docancel);
        friname = (EditText)findViewById(R.id.friname);
        docancel.setWidth(domakefri.getWidth());
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        if(way==1){
            domakefri.setText(getContext().getResources().getString(R.string.makefri));
        }else if(way==2){
            domakefri.setText(getContext().getResources().getString(R.string.search));
        }
        domakefri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.waitacc),Toast.LENGTH_SHORT).show();
                cancel();
            }
        });
        docancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),getContext().getResources().getString(R.string.cancel),Toast.LENGTH_SHORT).show();
                cancel();
            }
        });
    }
}
