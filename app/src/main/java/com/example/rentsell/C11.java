package com.example.rentsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class C11 extends AppCompatActivity {

    FloatingActionButton floatingActionButtonAdd;
    SharedPreferences sharedPreferences;
    //    TextView userName;
//    View headerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView filterBtn,sortBtn;
    private long pressedTime;



    ConstraintLayout splashScreenConstrain,c11_constrainLayout,internetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c11);
    }
}