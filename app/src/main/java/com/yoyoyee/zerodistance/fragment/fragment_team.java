package com.yoyoyee.zerodistance.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yoyoyee.zerodistance.R;
import com.yoyoyee.zerodistance.activity.GroupActivity;


/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_team extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team, container, false);
        //漂浮
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //                        .setAction("Action", null).show();
            Intent in = new Intent(getActivity(), GroupActivity.class);

            startActivity(in);
        }
    });

        //漂浮
        return v;
    }
}
