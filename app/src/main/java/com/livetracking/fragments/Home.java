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
    Button btnService,btnShare,btnFind;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnService = rootView.findViewById(R.id.id_service);
        btnShare = rootView.findViewById(R.id.btnShare);
        btnFind = rootView.findViewById(R.id.btnFind);
        btnService.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        sp=getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
        if(sp.getString("service","").equals("on")){
            btnService.setBackgroundColor(parseColor("#00c853"));
        }
        else {
            btnService.setBackgroundColor(parseColor("#cfd8dc"));
        }

        return  rootView;
    }

    @Override
    public void onClick(View v) {
        if(v== btnService){

            if(sp.getString("service","").equals("on")){
                //if on then turn off
                btnService.setBackgroundColor(parseColor("#cfd8dc"));

                editor=sp.edit();
                editor.putString("service","off");
                editor.apply();

                Toast.makeText(getContext(), "Service stopped", Toast.LENGTH_SHORT).show();

                ((Main)getActivity()).call_by_home();

            }
            else {
                //if off then turn on

                btnService.setBackgroundColor(parseColor("#00c853"));

                editor=sp.edit();
                editor.putString("service","on");
                editor.apply();

                Toast.makeText(getContext(), "Service started", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v == btnShare){
            
        }
        else if (v == btnFind){

        }
    }
}
