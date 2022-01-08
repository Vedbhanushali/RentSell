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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
    Connection connect;

    //use sharedreference for login
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String USERID = "userid";
    public static final String EMAIL = "email";
    public static final String NAME = "name";

    SharedPreferences sharedpreferences;

    int RC_SIGN_IN = 0;
    SignInButton SignIn;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    //For Error Showing
    TextInputLayout usernameLayout,passwordLayout;

    @Override
    protected void onStart() {

        //if user already login then open C11 class
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(NAME)) {
            startActivity(new Intent(getApplicationContext(),C11.class));
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

        SignIn=findViewById(R.id.sign_in_button);

        //Error part
        usernameLayout = findViewById(R.id.c01_editTextEmailLayout);
        passwordLayout = findViewById(R.id.c01_editTextPasswordLayout);

        //onclick google signin button events
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
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
//                            GetData getData = new GetData();   //This function to start the process of connection
//                            try {
//                                ConnectionResult = getData.execute("").get();         //This function to start the process of connection
//                            } catch (ExecutionException e) {
//                                e.printStackTrace();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            Log.e("connection result",ConnectionResult);
                            if(ConnectionResult.equals("Check connection")){
                                //usernameLayout.setError("Check Connection");
                                //passwordLayout.setError("Check Connection");
                                Toast.makeText(C01.this, "Check Connection", Toast.LENGTH_SHORT).show();
                            } else if (ConnectionResult.equals("Wrong")){
                                usernameLayout.setError("Wrong Credentials");
                                passwordLayout.setError("Wrong Credentials");
                            } else {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(USERID, userid);
                                editor.putString(NAME, name);
                                editor.putString(EMAIL,mobile);
                                editor.commit();
                                Log.i("username",userid+" , "+name+" , "+mobile);
                                //Toast.makeText(C01.this, userid, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),C11.class);
                                intent.putExtra("userid",userid);
                                startActivity(intent);
                            }
                        } else {
                            usernameLayout.setError("Check mobile number");
                            //Toast.makeText(getApplicationContext(), "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(validateEmail(email)){
//                            GetData getData = new GetData();   //This function to start the process of connection
//                            try {
//                                ConnectionResult = getData.execute("").get();         //This function to start the process of connection
//                            } catch (ExecutionException e) {
//                                e.printStackTrace();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            Log.e("connection result",ConnectionResult);
                            if(ConnectionResult.equals("Check connection")){
                                //usernameLayout.setError("Check Connection");
                                //passwordLayout.setError("Check Connection");
                                Toast.makeText(C01.this, "Check Connection", Toast.LENGTH_SHORT).show();
                            } else if (ConnectionResult.equals("Wrong")){
                                usernameLayout.setError("Wrong Credentials");
                                passwordLayout.setError("Wrong Credentials");
                            } else {
                                //Saving data to sharedpreference
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString(USERID, userid);
                                editor.putString(NAME, name);
                                editor.putString(EMAIL,email);
                                editor.commit();
                                Log.i("username",userid+" , "+name+" , "+email);
                                //Toast.makeText(C01.this, userid, Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(),C11.class);
                                intent.putExtra("userid",userid);
                                startActivity(intent);
                            }
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

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
//                handleSignInResult(task);
                //handle signIn Result
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken(),account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("Sign in status", "Google sign in failed", e);
                Toast.makeText(getApplicationContext(),"Google sign in failed",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken,GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            insertData(account);
                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                            Toast.makeText(getApplicationContext(),"Problem found in Firebase Login",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    private  void insertData(GoogleSignInAccount acc){
//        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        GoogleSignInAccount account=acc;
        // Signed in successfully, show authenticated UI.
        //insert data in databse
        if (account != null) {
            name = account.getDisplayName();
            email= account.getEmail();
//            try {
//                C00_connectionHelper connectionHelper = new C00_connectionHelper();
//                connect = connectionHelper.connectionClass();
//                if (connect != null) {
//                    String query;
//
//                    query = "INSERT INTO `c_profile`(`name`, `email`, `password`,`login_type` , `status`) SELECT * FROM (SELECT '"+name+"','"+email+"','Google','G','A') AS tmp WHERE NOT EXISTS (SELECT `email` FROM `c_profile` WHERE `email` = '"+email+"' )";
//                    Statement st = connect.createStatement();
//                    st.executeUpdate(query);
//
//                    ConnectionResult = "Successful inserted";
//
//                    //getting userid from database
//                    String query1 = "SELECT * FROM `c_profile` WHERE email='"+email+"' AND login_type='G' AND status='A'";
//                    Statement statement = connect.createStatement();
//                    ResultSet rs = statement.executeQuery(query1);
//                    String id = null;
//                    while (rs.next()){
//                        id=rs.getString("cid");
//                    }
////                        add data to sharedpreference
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString(NAME, name);
//                    editor.putString(EMAIL,email);
//                    editor.putString(USERID,id);
//                    editor.commit();
//                    Intent intent = new Intent(getApplicationContext(), C11.class);
//                    startActivity(intent);
//                } else {
//                    ConnectionResult = "Check connection";
//                }
//                connect.close();
//            } catch (Exception ex) {
//                Log.e("Error in1", ex.getMessage());
//            }

        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

}