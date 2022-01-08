package com.example.rentsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.rentsell.C01.EMAIL;
import static com.example.rentsell.C01.MyPREFERENCES;
import static com.example.rentsell.C01.NAME;
import static com.example.rentsell.C01.USERID;

public class C11_C12_C13_AppBar_Navgation extends AppCompatActivity {
    DrawerLayout drawerLayout;
    MaterialToolbar materialToolbar;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FloatingActionButton floatingActionButtonAdd;
    SharedPreferences sharedPreferences;
    TextView userName,name,support,copyright;

    //    TextView userName;
    View headerView;
    //    TabLayout tabLayout;
//    ViewPager viewPager;
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c11_c12_c13_app_bar_navgation);

        //initialize layout variables
        materialToolbar = (MaterialToolbar) findViewById(R.id.c11_topAppBar);
        drawerLayout = (DrawerLayout) findViewById(R.id.c11_drawer);
        navigationView = (NavigationView) findViewById(R.id.c11_NavigationViewMenu);
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.c112_floatingActionButtonAdd);
        headerView = navigationView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.c11_txtUserName);
        name = (TextView) headerView.findViewById(R.id.c11_txtName);
        support=(TextView) findViewById(R.id.appbar_nav_support);
        copyright=(TextView) findViewById(R.id.appbar_nav_copyright);


        constraintLayout = findViewById(R.id.progressAnimation);
        linearLayout = findViewById(R.id.dashboardLinearLayout);
        linearLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
//        SwipeRefreshLayout swiperef = rootView.findViewById(R.id.c11_f1_swipeRefresh);
        if(!isConnected()){
            constraintLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    linearLayout.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.GONE);
                }

            }, 3000);
            setSupportActionBar(materialToolbar);


            sharedPreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            if (sharedPreferences.contains(USERID)) {

                //if user exist then set it name
                userName.setText(sharedPreferences.getString(EMAIL, ""));
                name.setText(sharedPreferences.getString(NAME, ""));
                Toast.makeText(this, sharedPreferences.getString(USERID, ""), Toast.LENGTH_SHORT).show();

                //add toggle to menu
                toggle = new ActionBarDrawerToggle(this, drawerLayout, materialToolbar, R.string.c11_open, R.string.c11_close);
                toggle.setHomeAsUpIndicator(R.drawable.c11_menu);

                //sync toggle to drawerlayout
                drawerLayout.addDrawerListener(toggle);
                toggle.syncState();
            } else {
                Toast.makeText(getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
            }

            getSupportCopyrightInfo();

            //set default Fragment

//        navigationView.setCheckedItem(R.id.c11_navbarHome);

            //Open Fragment onclick MD RentSell in Navigation Bar
            materialToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), C11.class);
                    startActivity(intent);
                }
            });

            //set onclick events of items in navigationView
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                Intent intent;
                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.c11_dashboard_coordinator);

                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.c11_navbarHome:
                            intent = new Intent(getApplicationContext(), C11.class);     //define activty
                            break;
                        case R.id.c11_navbarProfile:
                            intent = new Intent(getApplicationContext(), C12.class);    //define activty
//                        coordinatorLayout.setVisibility(View.GONE);
                            break;
                        case R.id.c11_navbarProperty:
                            intent = new Intent(getApplicationContext(), C31.class);    //define activty
                            break;
                        case R.id.c11_navbarSupport:
                            intent = new Intent(getApplicationContext(), C13.class);    //define activty
                            break;
                        case R.id.c11_navbarLogout:
//                        showDialogLogout();                          //write code of logout
                            //make shared preference empty and then open login page
                            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            finish();
                            intent = new Intent(getApplicationContext(), C01.class);
                            Toast.makeText(getApplicationContext(), "Logout Succesfull", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + item.getItemId());
                    }
                    drawerLayout.closeDrawer(navigationView);
                    startActivity(intent);           //open activity

                    return true;
                }
            });
        }
    }

    private void getSupportCopyrightInfo() {
//        try {
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query;
//
//                query = "SELECT * FROM `o_parameter` where name='copyrightString' OR name='phoneNo'";
//                Statement statement = connect.createStatement();
//                ResultSet rs = statement.executeQuery(query);
//                while (rs.next()){
//                    if(rs.getString("name").contains("copyrightString"))
//                    {
//                        copyright.setText(rs.getString("value"));
//                    }
//
//                    if(rs.getString("name").contains("phoneNo"))
//                    {
//                        support.setText(rs.getString("value"));
//                    }
//                }
//
//            } else {
//                Toast.makeText(this, "Check Connection", Toast.LENGTH_SHORT).show();;
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in1", ex.getMessage());
//        }
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//    //            MenuInflater inflater = getMenuInflater();
//    //            inflater.inflate(R.menu.c11_search, menu);
//        MenuItem searchViewItem = menu.findItem(R.id.c11_search_menu);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
//             /*   if(list.contains(query)){
//                    adapter.getFilter().filter(query);
//                }else{
//                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
//                }*/
//
//                Toast.makeText(C11_C12_C13_AppBar_Navgation.this, query, Toast.LENGTH_SHORT).show();
//                return true;
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                    adapter.getFilter().filter(newText);
//                Log.d("onQueryTextChange", "query: " + newText);
//                return true;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }


}