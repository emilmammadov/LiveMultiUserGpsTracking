package com.livetracking.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.livetracking.R;
import com.livetracking.Main;

import java.util.Calendar;

import static android.graphics.Color.parseColor;

public class Home extends Fragment implements View.OnClickListener{
    Button btnTrack,btnShare,btnFind, btnDistanceYaya, btnDistanceSurucu,btnPredict,btnLogOut;
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

        btnTrack = rootView.findViewById(R.id.btnTrack);
        btnShare = rootView.findViewById(R.id.btnShare);
        btnFind = rootView.findViewById(R.id.btnFind);
        btnPredict = rootView.findViewById(R.id.btnPredict);
        btnLogOut = rootView.findViewById(R.id.btnLogOut);
        etDistanceYaya = rootView.findViewById(R.id.etDistanceYaya);
        btnDistanceYaya = rootView.findViewById(R.id.btnDistanceYaya);
        etDistanceSurucu = rootView.findViewById(R.id.etDistanceSurucu);
        btnDistanceSurucu = rootView.findViewById(R.id.btnDistanceSurucu);
        btnTrack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnDistanceYaya.setOnClickListener(this);
        btnDistanceSurucu.setOnClickListener(this);
        btnPredict.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        sp=getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
        editor=sp.edit();
        boolean track = sp.getString("track","").equals("on");
        boolean share = sp.getString("share","").equals("on");
        boolean find = sp.getString("find","").equals("on");
        boolean predict = sp.getString("predict","").equals("on");
        buttonColor(track, share, find, predict);


        return  rootView;
    }

    private void buttonColor(boolean track, boolean share, boolean find, boolean predict) {
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
        if(predict)
            btnPredict.setBackgroundColor(parseColor("#00c853"));
        else
            btnPredict.setBackgroundColor(parseColor("#cfd8dc"));
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

                buttonColor(true,false,false,false);

                editor.putString("track","on");
                editor.putString("share","off");
                editor.putString("find","off");
                editor.putString("predict","off");
                editor.apply();

                ((Main)getActivity()).callBy("track");
            }
        }
        else if (v == btnShare){
            if(sp.getString("share","").equals("on")){
                etDistanceSurucu.setVisibility(View.INVISIBLE);
                btnDistanceSurucu.setVisibility(View.INVISIBLE);
                btnShare.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("share","off");
                editor.putString("mapFrom","direct");
                editor.apply();

                liveSurucuReference.child(sp.getString("username","")).removeValue();
            }
            else{

                buttonColor(false,true,false, false);
                etDistanceSurucu.setVisibility(View.VISIBLE);
                btnDistanceSurucu.setVisibility(View.VISIBLE);

                editor.putString("share","on");
                editor.putString("track","off");
                editor.putString("find","off");
                editor.putString("predict","off");
                editor.apply();

            }

        }
        else if (v == btnFind){
            if(sp.getString("find","").equals("on")){
                etDistanceYaya.setVisibility(View.INVISIBLE);
                btnDistanceYaya.setVisibility(View.INVISIBLE);
                btnFind.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("find","off");
                editor.putString("mapFrom","direct");
                editor.apply();

                liveYayaReference.child(sp.getString("username","")).removeValue();
            }
            else{
                buttonColor(false,false,true,false);
                etDistanceYaya.setVisibility(View.VISIBLE);
                btnDistanceYaya.setVisibility(View.VISIBLE);

                editor.putString("find","on");
                editor.putString("track","off");
                editor.putString("share","off");
                editor.putString("predict","off");
                editor.apply();

            }
        }
        else if(v == btnDistanceYaya){
            if(!etDistanceYaya.getText().toString().equals("")) {
                editor.putInt("distanceYaya", Integer.parseInt(etDistanceYaya.getText().toString()));
                editor.apply();
                ((Main) getActivity()).callBy("find");
            }
        }
        else if(v == btnDistanceSurucu){
            if(!etDistanceSurucu.getText().toString().equals("")) {
                editor.putInt("distanceSurucu", Integer.parseInt(etDistanceSurucu.getText().toString()));
                editor.apply();
                ((Main) getActivity()).callBy("share");
            }
        }
        else if(v == btnPredict){
            if(sp.getString("predict","").equals("on")){

                btnPredict.setBackgroundColor(parseColor("#cfd8dc"));
                editor.putString("mapFrom","direct");
                editor.putString("predict","off");
                editor.apply();
            }
            else{
                LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.custom_alert, null);
                final EditText etDistPredict = dialog.findViewById(R.id.etDistPredict);
                final EditText etStartTimePredict = dialog.findViewById(R.id.etStartTimePredict);
                final EditText etTolTimePredict = dialog.findViewById(R.id.etTolTimePredict);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialog);
                builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if((!etDistPredict.getText().toString().equals("")) && (!etStartTimePredict.getText().toString().equals("")) && (!etTolTimePredict.getText().toString().equals(""))) {
                            buttonColor(false, false, false, true);
                            editor.putString("find","off");
                            editor.putString("track","off");
                            editor.putString("share","off");
                            editor.putString("predict","on");
                            editor.putInt("distancePredict", Integer.parseInt(etDistPredict.getText().toString()));
                            editor.putInt("maxTimePredict", Integer.parseInt(etStartTimePredict.getText().toString()));
                            editor.putInt("tolTimePredict", Integer.parseInt(etTolTimePredict.getText().toString()));
                            editor.apply();

                            ((Main) getActivity()).callBy("predict");
                        }
                    }
                }).show();

            }
        }
        else if (v == btnLogOut){
            editor = sp.edit();
            editor.putBoolean("login",false);
            editor.apply();
            Intent intent = new Intent(getContext(),Login.class);
            startActivity(intent);
        }
    }
}
