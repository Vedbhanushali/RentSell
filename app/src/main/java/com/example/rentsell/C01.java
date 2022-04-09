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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class C01 extends AppCompatActivity {
    EditText EtvUsername; //consider here username can be email or phone number
    EditText EtvPassword;
    TextView BtnForget, BtnRegister;
    Button BtnLogin;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");
    private static final Pattern DIFF_EMAIL_MOBILE = Pattern.compile("\\d+");
    String username,password,email,mobile,ConnectionResult,getpassword,userid="",name="";

    DatabaseReference dbreference;

    //use sharedreference for login
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String USERID = "userid";
    public static final String EMAIL = "email";
    public static final String NAME = "name";

    //for google signin
    SignInButton googleSignInBtn;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 100;


    SharedPreferences sharedpreferences;

    //For Error Showing
    TextInputLayout usernameLayout,passwordLayout;

    @Override
    protected void onStart() {

        //if user already login then open C11 class
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (sharedpreferences.contains(NAME)) {
            startActivity(new Intent(getApplicationContext(),C11.class));
            Toast.makeText(this, currentUser.getDisplayName()   , Toast.LENGTH_SHORT).show();
        }

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c01);

        EtvUsername = (EditText) findViewById(R.id.c01_editTextEmail);
        EtvPassword = (EditText) findViewById(R.id.c01_editTextPassword);
        BtnForget = (TextView)findViewById(R.id.c01_buttonForget);
        BtnLogin = (Button)findViewById(R.id.c01_buttonLogin);
        BtnRegister = (TextView) findViewById(R.id.c01_buttonRegister);

        googleSignInBtn=findViewById(R.id.sign_in_button);

        //Error part
        usernameLayout = findViewById(R.id.c01_editTextEmailLayout);
        passwordLayout = findViewById(R.id.c01_editTextPasswordLayout);

        //onclick google signin button events
//        SignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.sign_in_button:
////                       if ( !=  null) {
////                            // Got an ID token from Google. Use it to authenticate
////                            // with Firebase.
////                            Log.d("Token Id ", "Got ID token.");
////                        }
//
//                        signIn();
//                        break;
//                    // ...
//                }
//            }
//        });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        processRequest();

        googleSignInBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                signIn();
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = EtvUsername.getText().toString();
                password = EtvPassword.getText().toString();

                //Removing Error on button click
                usernameLayout.setError("");
                passwordLayout.setError("");

                if(!username.isEmpty()){
                    if(DIFF_EMAIL_MOBILE.matcher(username).matches()){      //if username is number format
                        mobile=username;
                        username="M";       //M for mobile
                    } else {                                                //else username is email
                        email=username;
                        username="E";       //E for Email
                    }
                }
                if(!username.isEmpty() && !password.isEmpty()) {

                    if(username.equals("M")){
                        if(validateMobile(mobile)){

                            getLoginData(new LoginCallback() {
                                @Override
                                public void onCallback(String username, String name, String uid) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(USERID, username);
                                    editor.putString(NAME, name);
                                    editor.putString(EMAIL,uid);
                                    editor.commit();
                                    Log.i("username",uid+" , "+name+" , "+username);
                                    //Toast.makeText(C01.this, userid, Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(),C11.class);
                                    intent.putExtra("userid",uid);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            usernameLayout.setError("Check mobile number");
                            //Toast.makeText(getApplicationContext(), "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(validateEmail(email)){

                            getLoginData(new LoginCallback() {
                                @Override
                                public void onCallback(String username, String name, String uid) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(USERID, uid);
                                    editor.putString(NAME, name);
                                    editor.putString(EMAIL,username);
                                    editor.commit();
                                    Log.i("username",uid+" , "+name+" , "+username);
                                    //Toast.makeText(C01.this, userid, Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(getApplicationContext(),C11.class);
                                    intent.putExtra("userid",uid);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            usernameLayout.setError("check Email address");
                            //Toast.makeText(getApplicationContext(), "Enter valid email address", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    usernameLayout.setError("Fill details");
                    passwordLayout.setError("Fill details");
                    //Toast.makeText(getApplicationContext(), "Please fill the form", Toast.LENGTH_LONG).show();
                }
            }
        });

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), C02.class);
                startActivity(intent);
            }
        });

        BtnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), C04.class);
                startActivity(intent);
            }
        });
    }

    private void processRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
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
    //==============================Google Signin Code======================================

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("SignIn status", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Sign in status9", "Google sign in failed", e);
                Toast.makeText(getApplicationContext(),"Google sign in failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String uid="1";
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            username=user.getEmail();
                            name=user.getDisplayName();

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(USERID, username);
                            editor.putString(NAME, name);
                            editor.putString(EMAIL,"1");
                            editor.commit();
                            Log.i("username",uid+" , "+name+" , "+username);
                            //Toast.makeText(C01.this, userid, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),C11.class);
//                            intent.putExtra("userid",uid);
                            startActivity(intent);


                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                            Toast.makeText(getApplicationContext(),"Problem found in Firebase Login",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

   

    private  void insertData(GoogleSignInAccount acc){
}


    private interface LoginCallback {
        void onCallback(String username,String name,String uid);
    }
    private void getLoginData(LoginCallback loginCallback){
        username = String.valueOf(EtvUsername.getText());
        password = String.valueOf(EtvPassword.getText());


        dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_profile");
        Query query = dbreference.orderByChild("email").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
//                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_profile").child(snapshot.getKey());
//                    Query query = reference.child("status").equalTo("A");
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
                    for(DataSnapshot ds : snapshot.getChildren()){
                        for(DataSnapshot d : ds.getChildren()){
                            String uName="",uCid="";
                            if(d.getKey().contains("name")){
                                uName=d.getValue().toString();
                            }
                            if(d.getKey().contains("cid")){
                                uCid=d.getValue().toString();
                            }
                            if(d.getKey().contains("password")){
                                String DatabasePassword= d.getValue().toString();
                                if(password.equals(DatabasePassword)){
                                    Toast.makeText(C01.this, "Successful login", Toast.LENGTH_SHORT).show();
                                    getProfileData(new ProfileCallback() {
                                        @Override
                                        public void onCallback(String name, String uid) {
                                            loginCallback.onCallback(username,name,uid);

                                        }
                                    });



                                }else {
                                    passwordLayout.setError("Wrong Credentials");
                                    Toast.makeText(C01.this, "Failed try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                            Log.e("login data", d.toString());
                            Log.e("login data2", d.getValue().toString());

                        }
                    }
                }else{
                    usernameLayout.setError("Wrong Credentials");
                    passwordLayout.setError("Wrong Credentials");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(C01.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private interface ProfileCallback {
        void onCallback(String name,String uid);
    }
    private void getProfileData(ProfileCallback profileCallback){
        username = String.valueOf(EtvUsername.getText());
        String uName="",uCid="";

        dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_profile");
        Query query = dbreference.orderByChild("email").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        for(DataSnapshot d : ds.getChildren()){
                            String uName="",uCid="";
                            if(d.getKey().contains("name")){
                                uName=d.getValue().toString();
                            }
                            if(d.getKey().contains("cid")){
                                uCid=d.getValue().toString();
                            }
                        }
                        profileCallback.onCallback(uName,uCid);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError databaseError) {

            }
        });
    }

}