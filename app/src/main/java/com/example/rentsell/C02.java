package com.example.rentsell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.util.regex.Pattern;

public class C02 extends AppCompatActivity {

    EditText EtvUsername;   //consider here username can be email or phone number we need to check and then insert accordingly
    EditText EtvName;
    EditText EtvPassword;
    EditText EtvConfirmPassword;
    Button BtnRegister;

    DatabaseReference dbreference;
    SharedPreferences sharedpreferences;


    //use sharedreference for login
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String USERID = "userid";
    public static final String EMAIL = "email";
    public static final String NAME = "name";


    //This is used for password verification
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{8,}" +                // at least 8 characters
                    "$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");
    private static final Pattern DIFF_EMAIL_MOBILE = Pattern.compile("\\d+");
    Connection connect;             //holds the connection object to database
    String ConnectionResult="";     //holds the progress of inserting data

    private static String username="";
    private static String name="";
    private static String password="";
    private static String confirmPassword="";
    private static String email="";
    private static String mobile="";

    boolean user;


    //For Error Showing
    TextInputLayout nameLayout,usernameLayout,passwordLayout,confirmLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c02);


        EtvUsername = (EditText) findViewById(R.id.c02_editTextEmail);
        EtvName = (EditText) findViewById(R.id.c02_editTextName);
        EtvPassword = (EditText) findViewById(R.id.c02_editTextPassword);
        EtvConfirmPassword = (EditText) findViewById(R.id.c02_editTextConfirmPassword);
        BtnRegister = (Button) findViewById(R.id.c02_buttonRegister);

        //For Error Showing
        nameLayout = findViewById(R.id.c02_editTextNameLayout);
        usernameLayout = findViewById(R.id.c02_editTextEmailLayout);
        passwordLayout = findViewById(R.id.c02_editTextPasswordLayout);
        confirmLayout = findViewById(R.id.c02_editTextConfirmPasswordLayout);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = EtvUsername.getText().toString();
                name = EtvName.getText().toString();
                password = EtvPassword.getText().toString();
                confirmPassword = EtvConfirmPassword.getText().toString();

                //Removing Error
                nameLayout.setError("");
                usernameLayout.setError("");
                passwordLayout.setError("");
                confirmLayout.setError("");

                if(!username.isEmpty()){
                    if(DIFF_EMAIL_MOBILE.matcher(username).matches()){      //if username is number format
                        mobile=username;
                        username="M";       //M for mobile
                    } else {                                                //else username is email
                        email=username;
                        username="E";       //E for Email
                    }
                }


                if(!username.isEmpty() && !name.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                    if(validatePassword(password,confirmPassword)){ //if true then only insert data
                        if(username.equals("M")){
                            if(validateMobile(mobile)){

//+name+"','"+mobile+"','"+password+"','P','A

                            dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_profile");
                            Query query = dbreference.child("Count");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Log.e("datasnapshot", String.valueOf(dataSnapshot.getValue()));
                                        String count=dataSnapshot.getValue().toString();
                                        String cid= String.valueOf(Integer.parseInt(count)+1);
                                        Post post = new Post(cid,name,mobile,password,email,"M");
                                        dbreference.child(cid).setValue(post);
                                        dbreference.child("Count").setValue(cid);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(USERID, cid);
                                        editor.putString(NAME, name);
                                        editor.putString(EMAIL,mobile);
                                        editor.commit();
                                        Log.i("username",cid+" , "+name+" , "+username);
                                        Toast.makeText(C02.this, "Data added succesfullty", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),C11.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

//                                dbreference.

//
//                            dbreference.setValue();

//                                Send insertData = new Send();   //This function to start the process of connection
//                                try {
//                                    ConnectionResult = insertData.execute("").get();         //This function to start the process of connection
//                                } catch (ExecutionException e) {
//                                    e.printStackTrace();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                Log.e("connection value",ConnectionResult);
//                                if(ConnectionResult.equals("Exist")){
//                                    usernameLayout.setError("already exist");
//                                }
//                                if(ConnectionResult.equals("Inserted Successfully")){
//                                    startActivity(new Intent(getApplicationContext(),C01.class));
//                                }
                            } else {
                                usernameLayout.setError("Enter valid number");
                                //Toast.makeText(getApplicationContext(), "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if(validateEmail(email)){

                                dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_profile");
                                Query query = dbreference.child("Count");
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            Log.e("datasnapshot", String.valueOf(dataSnapshot.getValue()));
                                            String count=dataSnapshot.getValue().toString();
                                            String cid= String.valueOf(Integer.parseInt(count)+1);
                                            Post post = new Post(cid,name,mobile,password,email,"E");
                                            dbreference.child(cid).setValue(post);
                                            dbreference.child("Count").setValue(cid);
                                            SharedPreferences.Editor editor = sharedpreferences.edit();
                                            editor.putString(USERID, cid);
                                            editor.putString(NAME, name);
                                            editor.putString(EMAIL,email);
                                            editor.commit();
                                            Log.i("username",cid+" , "+name+" , "+username);
                                            Toast.makeText(C02.this, "Data added succesfullty", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),C11.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

//                                Send insertData = new Send();   //This function to start the process of connection
//                                try {
//                                    ConnectionResult = insertData.execute("").get();         //This function to start the process of connection
//                                } catch (ExecutionException e) {
//                                    e.printStackTrace();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                Log.e("connection value",ConnectionResult);
//                                if(ConnectionResult.equals("Exist")){
//                                    usernameLayout.setError("already exist");
//                                }
//                                else if(ConnectionResult.equals("Check connection")){
//                                    usernameLayout.setError("Check Connection");
//                                    passwordLayout.setError("Check Connection");
//                                    nameLayout.setError("Check Connection");
//                                    confirmLayout.setError("Check Connection");
//                                }
//
//                                if(ConnectionResult.equals("Inserted Successfully")){
//                                    startActivity(new Intent(getApplicationContext(),C01.class));
//                                }

                            } else {
                                usernameLayout.setError("Enter valid email");
                                //Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } else {
                    nameLayout.setError("Fill details");
                    usernameLayout.setError("Fill details");
                    passwordLayout.setError("Fill details");
                    confirmLayout.setError("Fill details");
                    //Toast.makeText(getApplicationContext(), "Please fill the form", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //function to Validate password and check whether password and confirmPassword is same.
    private boolean validatePassword(String password,String confirmPassword){
        // if password does not matches to the pattern
        // it will display an toast message"
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passwordLayout.setError("Enter strong password");
            //Toast.makeText(getApplicationContext(), "Enter strong password ", Toast.LENGTH_LONG).show();
            return false;
        } else {
            //Now checking password and confirmPassword are same
            if(password.equals(confirmPassword)){
                return true;
            } else {
                confirmLayout.setError("Password doesn't match");
                //Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
    private boolean validateEmail(String email){
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }
    private boolean validateMobile(String mobile){
        if(EMAIL_PATTERN.matcher(mobile).matches()){
            return true;
        } else {
            return false;
        }
    }


    @IgnoreExtraProperties
    public class Post {

        public String cid;
        public String name;
        public String email;
        public String phone;
        public String status="A";
        public String password;
        public String login_type;

//        public Map<String, Boolean> stars = new HashMap<>();

        public Post() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public Post(String cid, String name, String phone, String password,String email,String login_type) {
            this.cid = cid;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.password =password;
            this.login_type =login_type;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getLogin_type() {
            return login_type;
        }

        public void setLogin_type(String login_type) {
            this.login_type = login_type;
        }

//        @Exclude
//        public Map<String, Object> toMap() {
//            HashMap<String, Object> result = new HashMap<>();
//            result.put("cid", cid);
//            result.put("name", name);
//            result.put("email", email);
//            result.put("phone", phone);
//            result.put("password", password);
//            result.put("status", status);
//            result.put("login_type", login_type);
//
//
//            return result;
//        }
    }
}