package com.example.rentsell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class C02 extends AppCompatActivity {

    EditText EtvUsername;   //consider here username can be email or phone number we need to check and then insert accordingly
    EditText EtvName;
    EditText EtvPassword;
    EditText EtvConfirmPassword;
    Button BtnRegister;

    DatabaseReference dbreference;

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
                            Query query = dbreference;
//                                Post post = new Post(name, mobile, password,"P");
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

        public int starCount = 0;
        public Map<String, Boolean> stars = new HashMap<>();

        public Post() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }

        public Post(String cid, String name, String phone, String password,String email) {
            this.cid = cid;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.password =password;

        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("cid", cid);
            result.put("name", name);
            result.put("email", email);
            result.put("phone", phone);
            result.put("password", password);
            result.put("status", status);
            result.put("login_type", login_type);


            return result;
        }
    }
}