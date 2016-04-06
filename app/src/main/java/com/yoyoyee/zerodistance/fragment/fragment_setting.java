package com.yoyoyee.zerodistance.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_setting extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        final TextView textsize = (TextView) v.findViewById(R.id.textsize);
        final float ttsize =textsize.getTextSize();
        textsize.setText(""+ttsize);

        return v;}
}