package com.example.rentsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.rentsell.C01.MyPREFERENCES;
import static com.example.rentsell.C01.USERID;

//import static com.example.mdrentsell.C01.MyPREFERENCES;
//import static com.example.mdrentsell.C01.USERID;

public class C12 extends C11_C12_C13_AppBar_Navgation {

    EditText EtvEmail;
    EditText EtvName;
    EditText EtvPhone;
    TextView BtnReset;
    Button BtnUpdate,BtnDelete;
    SharedPreferences sharedPreferences;
    Connection connect;             //holds the connection object to database
    String ConnectionResult="";     //holds the progress of inserting data
    protected static String username="";
    protected static String mobile="";
    protected static String email="";
    static String login_type="";
    protected static Integer uid=0;
    String cid="";

    DatabaseReference dbreference;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_c12);
//        toolbar = findViewById(R.id.editMenu);
//        setActionBar(toolbar);
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view=layoutInflater.inflate(R.layout.activity_c12,null,false);
        drawerLayout.addView(view,0);

        EtvEmail = (EditText) findViewById(R.id.c12_editTextEmail);
        EtvName = (EditText) findViewById(R.id.c12_editTextName);
        EtvPhone = (EditText) findViewById(R.id.c12_editTextPhone);
        BtnUpdate = (Button) findViewById(R.id.c12_buttonUpdate);
        BtnReset = (TextView) findViewById(R.id.c12_buttonChange);
        BtnDelete = (Button) findViewById(R.id.c12_buttonDelete);
        username = EtvName.getText().toString();
        email= EtvEmail.getText().toString();
        mobile = EtvPhone.getText().toString();
        BtnUpdate.setVisibility(View.GONE);
        BtnDelete.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(USERID)) {
            /*EtvName.setText(sharedPreferences.getString(NAME, ""));
            EtvEmail.setText(sharedPreferences.getString(EMAIL, ""));
            */cid=sharedPreferences.getString(USERID,"");
            Log.e("cid",cid);

//            try {
//
//                C00_connectionHelper connectionHelper = new C00_connectionHelper();
//                connect = connectionHelper.connectionClass();
//                if (connect != null) {
//                    String query;
//
//                    query = "SELECT * FROM `c_profile` where cid='"+cid+"'";
//                    Statement st = connect.createStatement();
//                    ResultSet rs = st.executeQuery(query);
//                    while (rs.next()){
//                        EtvName.setText(rs.getString("name"));
//                        EtvEmail.setText(rs.getString("email"));
//                        EtvPhone.setText(rs.getString("phone"));
//                        login_type= rs.getString("login_type");
//                        Log.e("login type",login_type);
//
//                    }
//
//
//                    ConnectionResult = "Successful updated";
//                    Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_LONG).show();
//                } else {
//                    ConnectionResult = "Check connection";
//                }
//                connect.close();
//            }catch (Exception ex){
//                Log.e("Error in",ex.getMessage());
//            }
        }


        BtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = EtvName.getText().toString();
                mobile = EtvPhone.getText().toString();
                email = EtvEmail.getText().toString();

//                try {
//
//                    C00_connectionHelper connectionHelper = new C00_connectionHelper();
//                    connect = connectionHelper.connectionClass();
//                    if (connect != null) {
//                        String query;
//
//                        query = "UPDATE `c_profile` SET `name`='"+username+"',`email`='"+email+"', phone='"+mobile+"' where cid='"+cid+"'";
//                        Statement st = connect.createStatement();
//                        st.executeUpdate(query);
//
//                        ConnectionResult = "Successful updated";
//                        Toast.makeText(getApplicationContext(),"Matched",Toast.LENGTH_LONG).show();
//                        BtnUpdate.setVisibility(View.GONE);
//                        BtnDelete.setVisibility(View.GONE);
//                        EtvEmail.setFocusableInTouchMode(false);
//                        EtvPhone.setFocusableInTouchMode(false);
//                    } else {
//                        ConnectionResult = "Check connection";
//                    }
//                    connect.close();
//                }catch (Exception ex){
//                    Log.e("Error in",ex.getMessage());
//                }
            }
        });
        BtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), C03.class);
                startActivity(intent);
            }
        });
        BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try{
//                    C00_connectionHelper connectionHelper = new C00_connectionHelper();
//                    connect = connectionHelper.connectionClass();
//                    Intent intent;
//                    if (connect != null) {
//                        String query;
//
//                        query = "UPDATE `c_profile` SET `status`='D' where cid='"+cid+"'";
//                        Statement st = connect.createStatement();
//                        st.executeUpdate(query);
//
//                        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.clear();
//                        editor.apply();
//                        finish();
//                        intent=new Intent(getApplicationContext(),C11.class);
//                        ConnectionResult = "Successful Delete";
//                        Toast.makeText(getApplicationContext(),"Account Deleted",Toast.LENGTH_LONG).show();
//                    } else {
//                        ConnectionResult = "Check connection";
//                    }
//                    connect.close();
//
//                }catch (Exception ex){
//                    Log.e("Error",ex.getMessage());
//                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.c12_c32_edit,menu);

//        // Associate searchable configuration with the SearchView
        MenuItem editItem = menu.findItem(R.id.edit);
        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BtnDelete.setVisibility(View.GONE);
                Log.e("login type",login_type);

                EtvName.setFocusableInTouchMode(true);
                if(login_type.contains("P")){
                    EtvPhone.setFocusableInTouchMode(false);
                    EtvEmail.setFocusableInTouchMode(true);
                }
                else if (login_type.contains("E")){
                    EtvEmail.setFocusableInTouchMode(false);
                    EtvPhone.setFocusableInTouchMode(true);
                }else if(login_type.contains("G")){
                    EtvPhone.setFocusableInTouchMode(true);
                    EtvEmail.setFocusableInTouchMode(false);
                }
                else{
                    EtvEmail.setFocusableInTouchMode(true);
                    EtvPhone.setFocusableInTouchMode(true);
                }
                BtnUpdate.setVisibility(View.VISIBLE);
                return true;
            }
        });
        MenuItem deleteItem = menu.findItem(R.id.delete);
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                BtnUpdate.setVisibility(View.GONE);
                BtnDelete.setVisibility(View.VISIBLE);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),C11.class));
        finish();
    }
//    private void getLoginData(C01.LoginCallback loginCallback){
//        username = String.valueOf(EtvUsername.getText());
//        password = String.valueOf(EtvPassword.getText());
//
//
//        dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_profile");
//        Query query = dbreference.orderByChild("email").equalTo(username);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
////                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_profile").child(snapshot.getKey());
////                    Query query = reference.child("status").equalTo("A");
////                    query.addListenerForSingleValueEvent(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
////
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                        }
////                    });
//                    for(DataSnapshot ds : snapshot.getChildren()){
//                        for(DataSnapshot d : ds.getChildren()){
//                            String uName="",uCid="";
//                            if(d.getKey().contains("name")){
//                                uName=d.getValue().toString();
//                            }
//                            if(d.getKey().contains("cid")){
//                                uCid=d.getValue().toString();
//                            }
//                            if(d.getKey().contains("password")){
//                                String DatabasePassword= d.getValue().toString();
//                                if(password.equals(DatabasePassword)){
//                                    Toast.makeText(C01.this, "Successful login", Toast.LENGTH_SHORT).show();
//                                    getProfileData(new C01.ProfileCallback() {
//                                        @Override
//                                        public void onCallback(String name, String uid) {
//                                            loginCallback.onCallback(username,name,uid);
//
//                                        }
//                                    });
//
//
//
//                                }else {
//                                    passwordLayout.setError("Wrong Credentials");
//                                    Toast.makeText(C01.this, "Failed try again", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                            Log.e("login data", d.toString());
//                            Log.e("login data2", d.getValue().toString());
//
//                        }
//                    }
//                }else{
//                    usernameLayout.setError("Wrong Credentials");
//                    passwordLayout.setError("Wrong Credentials");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(C01.this, "Database Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }


}