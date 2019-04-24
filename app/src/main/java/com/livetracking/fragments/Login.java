package com.livetracking.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.livetracking.Main;
import com.livetracking.R;
import com.livetracking.User;


public class Login extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference userReference;
    LinearLayout layoutSignUp,layoutLogin;
    Button btnSignUp, btnLogin;
    TextView changeSignUp, changeLogin;
    ColorStateList original,purple;
    EditText etSignUsername, etSignPassword, etLoginUsername, etLoginPassword;
    String strUsername, strPassword;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    final static int MY_ACCESS_FINE_LOCATION = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userReference = FirebaseDatabase.getInstance().getReference("users");

        //Initialize all views
        changeSignUp = findViewById(R.id.id_change_signup);
        changeLogin = findViewById(R.id.id_change_login);

        layoutSignUp= findViewById(R.id.id_layout_signup);
        layoutLogin= findViewById(R.id.id_layout_login);

        //SignUp
        etSignUsername = findViewById(R.id.id_new_username);
        etSignPassword = findViewById(R.id.id_new_password);
        btnSignUp = findViewById(R.id.id_new_signup);

        //Login
        etLoginUsername = findViewById(R.id.id_username);
        etLoginPassword = findViewById(R.id.id_password);
        btnLogin = findViewById(R.id.id_signup);

        //setting onclick listener
        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        changeLogin.setOnClickListener(this);
        changeSignUp.setOnClickListener(this);

        sp = getSharedPreferences("myData", Context.MODE_PRIVATE);

        purple = changeSignUp.getTextColors();
        original =  changeLogin.getTextColors();

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }
        
        Runtime_permission();

    }

    @Override
    public void onClick(View v) {
        View view =this.getCurrentFocus();

        if(view!=null){
            InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

        if(v== changeSignUp){
            changeSignUp.setTextColor(purple);
            changeLogin.setTextColor(original);
            layoutSignUp.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.GONE);
        }
        else if(v== changeLogin){
            changeSignUp.setTextColor(original);
            changeLogin.setTextColor(purple);
            layoutSignUp.setVisibility(View.GONE);
            layoutLogin.setVisibility(View.VISIBLE);
        }
        else if(v== btnSignUp){

            if(check_permission()){
                String id = userReference.push().getKey();
                strUsername = etSignUsername.getText().toString();
                strPassword = etSignPassword.getText().toString();

                User user = new User(id,strUsername,strPassword);
                userReference.child(id).setValue(user);

                Runtime_permission();

            }

        }
        else if(v== btnLogin){

            if(check_permission()){
                strUsername = etLoginUsername.getText().toString();
                strPassword = etLoginPassword.getText().toString();
            }

        }
    }
    
    /*public void check_username(){

        if(//Username yoksa) {
            //EGER USERNAME YOKSA BURADA KAYDET
            Toast.makeText(Login.this,"Account created!",Toast.LENGTH_SHORT).show();

            editor=sp.edit();
            editor.putBoolean("login",true);
            editor.putString("username", strUsername);
            editor.apply();
            startActivity(new Intent(Login.this, Main.class));
        }

    }*/

    public void login(){
        String temp_password = "";

            if(strPassword.equals(temp_password)){
                //PASSWORD UYUSUYORSA
                editor=sp.edit();
                editor.putBoolean("login",true);
                editor.putString("username", strUsername);
                editor.apply();
                startActivity(new Intent(Login.this,Main.class));
            }
            else
                Toast.makeText(Login.this,"Wrong Password.",Toast.LENGTH_SHORT).show();
    }

    public void Runtime_permission(){
        if(check_permission() && (sp.getBoolean("login",false))){
            startActivity(new Intent(Login.this,Main.class));
        }
    }

    public boolean check_permission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else
            requestFineLocation();

        return false; //when above code do not execute
    }

    private void requestFineLocation(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
        }

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Runtime_permission();
                }
                else
                    requestFineLocation();
                break;
        }
    }
}
