package com.livetracking.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.livetracking.R;
import com.livetracking.Main;

import static android.graphics.Color.parseColor;

public class Home extends Fragment implements View.OnClickListener{
    Button btnTrack,btnShare,btnFind,btnDistance;
    EditText etDistance;
    DatabaseReference liveReference = FirebaseDatabase.getInstance().getReference("live");
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnTrack = rootView.findViewById(R.id.id_service);
        btnShare = rootView.findViewById(R.id.btnShare);
        btnFind = rootView.findViewById(R.id.btnFind);
        etDistance = rootView.findViewById(R.id.etDistance);
        btnDistance = rootView.findViewById(R.id.btnDistance);
        btnTrack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnDistance.setOnClickListener(this);

        sp=getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
        editor=sp.edit();
        boolean service = sp.getString("service","").equals("on");
        boolean share = sp.getString("share","").equals("on");
        boolean find = sp.getString("find","").equals("on");
        buttonColor(service, share, find);


        return  rootView;
    }

    private void buttonColor(boolean service, boolean share, boolean find) {
        if(service)
            btnTrack.setBackgroundColor(parseColor("#00c853"));
        else
            btnTrack.setBackgroundColor(parseColor("#cfd8dc"));
        if(share)
            btnShare.setBackgroundColor(parseColor("#00c853"));
        else
            btnShare.setBackgroundColor(parseColor("#cfd8dc"));
        if(find)
            btnFind.setBackgroundColor(parseColor("#00c853"));
        else
            btnFind.setBackgroundColor(parseColor("#cfd8dc"));

    }

    @Override
    public void onClick(View v) {
        if(v== btnTrack){
            if(sp.getString("track","").equals("on")){

                btnTrack.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("track","off");
                editor.apply();

                Toast.makeText(getContext(), "Service stopped", Toast.LENGTH_SHORT).show();

                ((Main)getActivity()).callBy("track");

            }
            else{
                btnTrack.setBackgroundColor(parseColor("#00c853"));
                btnFind.setBackgroundColor(parseColor("#cfd8dc"));
                btnShare.setBackgroundColor(parseColor("#cfd8dc"));

                editor.putString("track","on");
                editor.putString("share","off");
                editor.putString("find","off");
                editor.apply();

                Toast.makeText(getContext(), "Tracking Started", Toast.LENGTH_SHORT).show();
            }
        }
        else if (v == btnShare){
            if(sp.getString("share","").equals("on")){
                btnShare.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("share","off");
                editor.apply();

                liveReference.child(sp.getString("username","")).removeValue();
            }
            else{
                btnShare.setBackgroundColor(parseColor("#00c853"));
                btnTrack.setBackgroundColor(parseColor("#cfd8dc"));
                btnFind.setBackgroundColor(parseColor("#cfd8dc"));

                editor.putString("share","on");
                editor.putString("track","off");
                editor.putString("find","off");
                editor.apply();

                ((Main)getActivity()).callBy("share");

            }

        }
        else if (v == btnFind){
            if(sp.getString("find","").equals("on")){
                btnFind.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("find","off");
                editor.apply();
            }
            else{
                btnFind.setBackgroundColor(parseColor("#00c853"));
                btnShare.setBackgroundColor(parseColor("#cfd8dc"));
                btnTrack.setBackgroundColor(parseColor("#cfd8dc"));
                etDistance.setVisibility(View.VISIBLE);
                btnDistance.setVisibility(View.VISIBLE);

                editor.putString("find","on");
                editor.putString("track","off");
                editor.putString("share","off");
                editor.apply();

            }
        }
        else if(v == btnDistance){
            if(etDistance.getText() != null) {
                editor.putInt("distance", Integer.parseInt(etDistance.getText().toString()));
                editor.apply();
                ((Main) getActivity()).callBy("find");
            }
        }
    }
}
