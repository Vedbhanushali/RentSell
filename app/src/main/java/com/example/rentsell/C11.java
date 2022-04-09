package com.example.rentsell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import static com.example.rentsell.C01.EMAIL;
import static com.example.rentsell.C01.MyPREFERENCES;
import static com.example.rentsell.C01.NAME;
import static com.example.rentsell.C01.USERID;
import static com.example.rentsell.C11_Filter.sellFilterPref;
import static com.example.rentsell.C11_Sort.sortId;
import static com.example.rentsell.C11_Sort.sortPrefernce;



public class C11 extends C11_C12_C13_AppBar_Navgation {

    FloatingActionButton floatingActionButtonAdd;
    SharedPreferences sharedPreferences;
        TextView Name,Username;
    View headerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView filterBtn,sortBtn;
    private long pressedTime;



    ConstraintLayout splashScreenConstrain,c11_constrainLayout,internetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view=layoutInflater.inflate(R.layout.activity_c11,null,false);
        drawerLayout.addView(view,0);


        //initialize layout variables
        floatingActionButtonAdd=(FloatingActionButton)findViewById(R.id.c112_floatingActionButtonAdd);
        headerView=navigationView.getHeaderView(0);
        Username=(TextView)headerView.findViewById(R.id.c11_txtUserName);
        Name =(TextView)headerView.findViewById(R.id.c11_txtName);

        tabLayout=(TabLayout)findViewById(R.id.c11_tabLayout);
        viewPager=(ViewPager)findViewById(R.id.c11_viewPager);
        splashScreenConstrain=(ConstraintLayout)findViewById(R.id.progressAnimation);
        c11_constrainLayout=(ConstraintLayout)findViewById(R.id.c11_constrainLayout);
        internetLayout =(ConstraintLayout)findViewById(R.id.noInternetAnimation);

        c11_constrainLayout.setVisibility(View.GONE);
        splashScreenConstrain.setVisibility(View.VISIBLE);
//        SwipeRefreshLayout swiperef = rootView.findViewById(R.id.c11_f1_swipeRefresh);

//        filterBtn=(TextView) findViewById(R.id.c11_f1_filterBtn);
//        sortBtn=(TextView) findViewById(R.id.c11_sortBtn);

        if(!isConnected()){
            internetLayout.setVisibility(View.VISIBLE);
            c11_constrainLayout.setVisibility(View.GONE);
            splashScreenConstrain.setVisibility(View.GONE);
        }else {
            internetLayout.setVisibility(View.GONE);
            c11_constrainLayout.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    c11_constrainLayout.setVisibility(View.VISIBLE);
                    splashScreenConstrain.setVisibility(View.GONE);
                }

            }, 5000);


            final C11_A c11_A_adapter = new C11_A(this, getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(c11_A_adapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            navigationView.setCheckedItem(R.id.c11_navbarHome);
//        //if user exist then set it name
        if (sharedPreferences.contains(USERID)) {
            Username.setText(sharedPreferences.getString(NAME,""));
            Name.setText(sharedPreferences.getString(EMAIL,""));
            Toast.makeText(this, sharedPreferences.getString(USERID,"") + sharedPreferences.getString(NAME,"") + sharedPreferences.getString(EMAIL,""), Toast.LENGTH_SHORT).show();
        }

//        filterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                C11_Filter bottomSheet = new C11_Filter();
//                bottomSheet.show(getSupportFragmentManager(),
//                        "FilterBottomSheet");
//
//            }
//        });
//        sortBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                C11_Sort c11_sort=new C11_Sort();
//                c11_sort.show(getSupportFragmentManager(),"SortBottomSheet");
//            }
//        });

            floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sharedPreferences.contains(NAME)) {
                        userName.setText(sharedPreferences.getString(NAME, ""));

                        //Toast.makeText(this, sharedPreferences.getString(NAME,""), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), C32.class);
                        intent.putExtra("USERID", sharedPreferences.getString(USERID, ""));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), C01.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }
    //on click back button of mobile close the current activity
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
//            C11.this.finish();
            moveTaskToBack(true);
            android.os.Process.killProcess(1);
            System.exit(1);
        } else {

            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @Override
    protected void onDestroy() {

        SharedPreferences sellShared = getSharedPreferences(sellFilterPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = sellShared.edit();
        editor.clear();
        editor.apply();
        editor.commit();

        SharedPreferences sortShared = getSharedPreferences(sortPrefernce, MODE_PRIVATE);

        SharedPreferences.Editor editor1 = sortShared.edit();
        editor1.clear();
        editor1.apply();
        editor1.commit();
        Log.e("ondestroy", "deleteshared");
        super.onDestroy();
    }
}