package com.livetracking.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.livetracking.R;
import com.livetracking.Main;

import static android.graphics.Color.parseColor;

public class Home extends Fragment implements View.OnClickListener{
    Button btn_service;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btn_service= rootView.findViewById(R.id.id_service);
        btn_service.setOnClickListener(this);

        sp=getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
        if(sp.getString("service","").equals("on")){
            btn_service.setBackgroundColor(parseColor("#00c853"));
        }
        else {
            btn_service.setBackgroundColor(parseColor("#cfd8dc"));
        }

        return  rootView;
    }

    @Override
    public void onClick(View v) {
        if(v==btn_service){

            if(sp.getString("service","").equals("on")){
                //if on then turn off
                btn_service.setBackgroundColor(parseColor("#cfd8dc"));

                editor=sp.edit();
                editor.putString("service","off");
                editor.apply();

                Toast.makeText(getContext(), "Service stopped", Toast.LENGTH_SHORT).show();

                ((Main)getActivity()).call_by_home();

            }
            else {
                //if off then turn on

                btn_service.setBackgroundColor(parseColor("#00c853"));

                editor=sp.edit();
                editor.putString("service","on");
                editor.apply();

                Toast.makeText(getContext(), "Service started", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
