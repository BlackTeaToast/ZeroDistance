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
        SeekBar seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        final TextView textView17 = (TextView) v.findViewById(R.id.textView17);
        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() { int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) { progress = progresValue; }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,

                //if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Display the value in textview
                textView17.setText(progress + "/" + seekBar.getMax());
            }
        });
        return v;}
}