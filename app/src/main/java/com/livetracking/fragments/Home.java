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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.livetracking.R;
import com.livetracking.Main;

import static android.graphics.Color.parseColor;

public class Home extends Fragment implements View.OnClickListener{
    Button btnTrack,btnShare,btnFind, btnDistanceYaya, btnDistanceSurucu;
    EditText etDistanceYaya, etDistanceSurucu;
    DatabaseReference liveSurucuReference = FirebaseDatabase.getInstance().getReference("liveSurucu");
    DatabaseReference liveYayaReference = FirebaseDatabase.getInstance().getReference("liveYaya");
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
        etDistanceYaya = rootView.findViewById(R.id.etDistanceYaya);
        btnDistanceYaya = rootView.findViewById(R.id.btnDistanceYaya);
        etDistanceSurucu = rootView.findViewById(R.id.etDistanceSurucu);
        btnDistanceSurucu = rootView.findViewById(R.id.btnDistanceSurucu);
        btnTrack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnDistanceYaya.setOnClickListener(this);
        btnDistanceSurucu.setOnClickListener(this);

        sp=getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
        editor=sp.edit();
        boolean track = sp.getString("track","").equals("on");
        boolean share = sp.getString("share","").equals("on");
        boolean find = sp.getString("find","").equals("on");
        buttonColor(track, share, find);


        return  rootView;
    }

    private void buttonColor(boolean track, boolean share, boolean find) {
        if(track)
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
                editor.putString("mapFrom","direct");
                editor.apply();

            }
            else{
                btnTrack.setBackgroundColor(parseColor("#00c853"));
                btnFind.setBackgroundColor(parseColor("#cfd8dc"));
                btnShare.setBackgroundColor(parseColor("#cfd8dc"));

                editor.putString("track","on");
                editor.putString("share","off");
                editor.putString("find","off");
                editor.apply();

                ((Main)getActivity()).callBy("track");
            }
        }
        else if (v == btnShare){
            if(sp.getString("share","").equals("on")){
                btnShare.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("share","off");
                editor.putString("mapFrom","direct");
                editor.apply();

                liveSurucuReference.child(sp.getString("username","")).removeValue();
            }
            else{
                btnShare.setBackgroundColor(parseColor("#00c853"));
                btnTrack.setBackgroundColor(parseColor("#cfd8dc"));
                btnFind.setBackgroundColor(parseColor("#cfd8dc"));
                etDistanceSurucu.setVisibility(View.VISIBLE);
                btnDistanceSurucu.setVisibility(View.VISIBLE);

                editor.putString("share","on");
                editor.putString("track","off");
                editor.putString("find","off");
                editor.apply();

            }

        }
        else if (v == btnFind){
            if(sp.getString("find","").equals("on")){
                btnFind.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("find","off");
                editor.putString("mapFrom","direct");
                editor.apply();

                liveYayaReference.child(sp.getString("username","")).removeValue();
            }
            else{
                btnFind.setBackgroundColor(parseColor("#00c853"));
                btnShare.setBackgroundColor(parseColor("#cfd8dc"));
                btnTrack.setBackgroundColor(parseColor("#cfd8dc"));
                etDistanceYaya.setVisibility(View.VISIBLE);
                btnDistanceYaya.setVisibility(View.VISIBLE);

                editor.putString("find","on");
                editor.putString("track","off");
                editor.putString("share","off");
                editor.apply();

            }
        }
        else if(v == btnDistanceYaya){
            if(etDistanceYaya.getText() != null) {
                editor.putInt("distanceYaya", Integer.parseInt(etDistanceYaya.getText().toString()));
                editor.apply();
                ((Main) getActivity()).callBy("find");
            }
        }
        else if(v == btnDistanceSurucu){
            if(etDistanceSurucu.getText() != null) {
                editor.putInt("distanceSurucu", Integer.parseInt(etDistanceSurucu.getText().toString()));
                editor.apply();
                ((Main) getActivity()).callBy("share");
            }
        }
    }
}
