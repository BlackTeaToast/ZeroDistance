package com.yoyoyee.zerodistance.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.yoyoyee.zerodistance.R;

/**
 * Created by 楊霖村 on 2016/4/4.
 */
public class fragment_setting extends Fragment {
    int languageuse =0;//0中文 1英文
    private ArrayAdapter<String> adapterPress;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        final TextView textsize = (TextView) v.findViewById(R.id.textsize);
        final float ttsize =textsize.getTextSize();
        textsize.setText(""+ttsize);

        //spinner
            Spinner languageSpinner = (Spinner) v.findViewById(R.id.languageSpinner);
            adapterPress = new ArrayAdapter<String>(getActivity(), R.layout.spinner,getResources().getStringArray(R.array.languageSppingArrary));
            adapterPress.setDropDownViewResource(R.layout.spinner);
            languageSpinner.setAdapter(adapterPress);
            languageSpinner.setAdapter(adapterPress);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  //spinner監聽
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageuse=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner

        return v;}
}