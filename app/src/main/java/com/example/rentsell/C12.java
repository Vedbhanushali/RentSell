package com.example.rentsell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.rentsell.C01.MyPREFERENCES;
import static com.example.rentsell.C01.USERID;

public class C12 extends C11_C12_C13_AppBar_Navgation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view=layoutInflater.inflate(R.layout.activity_c12,null,false);
        drawerLayout.addView(view,0);
    }
}