package com.example.rentsell;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class C32 extends C11_C12_C13_AppBar_Navgation implements OnMapReadyCallback {

    String pid = "", tid = "";
    String ConnectionResult = "";
    EditText EtvType, EtvPname, EtvName, EtvNumber, EtvAddress, EtvDate, EtvDescription, EtvAmount, EtvTypeRes, EtvLocation;
    EditText[] detailsEditTextArray;
    TextView FeaturedTxt;
    Button BtnSold, BtnUpdate, BtnCancel, BtnLocation, BtnSellFast;
    Connection connection;
    FloatingActionButton backFloatingBtn, editFloatingBtn, deleteFloatingBtn;
    String userid = "", connectionResult;
    String[] splitdetails;

    SharedPreferences sharedPreferences;
    private GoogleMap mMap;

    String resType = "", prop_name = "", name = "", date = "", number = "", address = "", location = "", description = "", amount = "";
    Double propertyLatitude, propertyLongitude;
    CarouselView carouselView;
    String detail = null;
    GridLayout myGridLayout;
    TextView[] myTextViews;
    ArrayList<URL> imgList;

    int[] sampleImages = {R.drawable.home1, R.drawable.home1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LayoutInflater layoutInflater=LayoutInflater.from(this);
//        View view=layoutInflater.inflate(R.layout.activity_c32,null,false);
//        drawerLayout.addView(view,0);
        setContentView(R.layout.activity_c32);
        carouselView = (CarouselView) findViewById(R.id.c32_carouselView);

        FeaturedTxt = (TextView) findViewById(R.id.c32_textFeature);
        EtvType = (EditText) findViewById(R.id.c32_editTextType);
        EtvAmount = (EditText) findViewById(R.id.c32_editTextAmount);
        EtvTypeRes = (EditText) findViewById(R.id.c32_editTextResType);
        EtvPname = (EditText) findViewById(R.id.c32_editTextPropertyName);
        EtvName = (EditText) findViewById(R.id.c32_editTexTNAME);
        EtvNumber = (EditText) findViewById(R.id.c32_editTextNumber);
        EtvAddress = (EditText) findViewById(R.id.c32_editTextAddress);
        EtvLocation = (EditText) findViewById(R.id.c32_editTextLocation);
        EtvDate = (EditText) findViewById(R.id.c32_editTextDate);
        EtvDescription = (EditText) findViewById(R.id.c32_editTextDescription);
        BtnUpdate = (Button) findViewById(R.id.c32_buttonUpdate);
        BtnSold = (Button) findViewById(R.id.c32_buttonSold);
        BtnCancel = (Button) findViewById(R.id.c32_buttonCancel);
        BtnLocation = (Button) findViewById(R.id.c32_selectLocation);
        BtnSellFast = (Button) findViewById(R.id.c32_buttonSellFastNow);
        backFloatingBtn = (FloatingActionButton) findViewById(R.id.c32_backBtn);
        editFloatingBtn = (FloatingActionButton) findViewById(R.id.c32_editBtn);
        deleteFloatingBtn = (FloatingActionButton) findViewById(R.id.c32_deleteFloatingBtn);
        myGridLayout = (GridLayout) findViewById(R.id.c32_myGridLayout);
//        LinearLayout myLinearLayout2=(LinearLayout) findViewById(R.id.myLinearLayout2);


        pid = getIntent().getStringExtra("pid");
        tid = getIntent().getStringExtra("tid");
        Toast.makeText(this, pid + ", " + tid, Toast.LENGTH_SHORT).show();

        FeaturedTxt.setVisibility(View.GONE);
        BtnUpdate.setVisibility(View.GONE);
        BtnCancel.setVisibility(View.GONE);
        BtnLocation.setVisibility(View.GONE);

        boolean feature=getIntent().getBooleanExtra("feature",false);
        if(feature){
            FeaturedTxt.setVisibility(View.VISIBLE);
        }else {
            FeaturedTxt.setVisibility(View.GONE);
        }
        fetchPropertyData();
        fetchImageFromAmplify();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.c32_map);
        mapFragment.getMapAsync(this);

        BtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(C32.this, "selecting location", Toast.LENGTH_SHORT).show();
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                try {
//                    // for activty
//                    LatLngBounds BOUNDS_INDIA = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));                    LatLng coordinate = new LatLng(22.22, 70.81);
//
//                    builder.setLatLngBounds(BOUNDS_INDIA);
//                    startActivityForResult(builder.build(C32.this), 2);
//                    // for fragment
//                    //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
            }
        });

        BtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(C32.this);
                builder.setMessage("Are you sure you want to update this Property ? ");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                prop_name = EtvPname.getText().toString();
                                name = EtvName.getText().toString();
                                number = EtvNumber.getText().toString();
                                description = EtvDescription.getText().toString();
                                amount = EtvAmount.getText().toString();
                                location = EtvLocation.getText().toString();
                                address = EtvAddress.getText().toString();
                                date = EtvDate.getText().toString();
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(rs.getString("latitude")), Double.parseDouble(rs.getString("longitude")), 1);
                                List<Address> addressesList = null;
                                try {
                                    addressesList = geocoder.getFromLocationName(location, 1);
                                    propertyLatitude = addressesList.get(0).getLatitude();
                                    propertyLongitude = addressesList.get(0).getLongitude();

                                    Log.e("adresss", addressesList.toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (EtvTypeRes.getText().toString().toLowerCase().equals("rent")) {
                                    resType = "R";
                                } else {
                                    resType = "S";
                                }
                                detail = "";
                                for (int i = 0; i < detailsEditTextArray.length; i++) {
                                    detail = detail + splitdetails[2 * i] + ":" + detailsEditTextArray[i].getText().toString() + "|";
                                }

//                                try {
//
//                                    C00_connectionHelper connectionHelper = new C00_connectionHelper();
//                                    connection = connectionHelper.connectionClass();
//                                    if (connection != null) {
//                                        String query;
//
//                                        query = "UPDATE `c_property` SET `amount`='" + amount + "',`name`='" + prop_name + "',`owner_name`='" + name + "',`owner_number`='" + number + "',`latitude`='" + propertyLatitude + "',`longitude`='" + propertyLongitude + "',`address`='" + address + "',`detail`='" + detail + "',`description`='" + description + "',`rent_sell`='" + resType + "' where pid='" + pid + "'";
//                                        Statement st = connection.createStatement();
//                                        st.executeUpdate(query);
//
//                                        ConnectionResult = "Successful updated";
//                                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
//                                        //make edittext unchangeable
//                                        setVisibiltyGoneofViews();
//                                    } else {
//                                        ConnectionResult = "Check connection";
//                                    }
//                                    connection.close();
//                                } catch (Exception ex) {
//                                    Log.e("Error in", ex.getMessage());
//                                }
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        BtnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(C32.this);
                builder.setMessage("Are you sure you want to sold this Property ? ");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                soldProperty();
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), C32.class));
                                finish();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibiltyGoneofViews();
                fetchPropertyData();
            }
        });
        editFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtvAddress.setFocusableInTouchMode(true);
                EtvAmount.setFocusableInTouchMode(true);
                EtvDescription.setFocusableInTouchMode(true);
//                EtvDetail.setFocusableInTouchMode(true);
                EtvName.setFocusableInTouchMode(true);
                EtvNumber.setFocusableInTouchMode(true);
                EtvPname.setFocusableInTouchMode(true);
                EtvType.setFocusableInTouchMode(true);
                EtvLocation.setFocusableInTouchMode(true);
                EtvAddress.setFocusableInTouchMode(true);
                EtvDate.setFocusableInTouchMode(true);
                for (int i = 0; i < detailsEditTextArray.length; i++) {
                    detailsEditTextArray[i].setFocusableInTouchMode(true);
                }
//                EtvTypeRes.setFocusableInTouchMode(true);
                BtnUpdate.setVisibility(View.VISIBLE);
                BtnCancel.setVisibility(View.VISIBLE);
                BtnLocation.setVisibility(View.VISIBLE);
            }
        });
        deleteFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(C32.this);
                builder.setMessage("Are you sure you want to delete this Property ? ");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteProperty();
                                dialog.cancel();
                                startActivity(new Intent(getApplicationContext(), C32.class));
                                finish();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        backFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), C31.class));
                finish();
            }
        });
        BtnSellFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), C31_C33_Payment.class);
                intent.putExtra("price", amount);
                intent.putExtra("pid", pid);
                intent.putExtra("tid", tid);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setVisibiltyGoneofViews() {
        EtvAddress.setFocusableInTouchMode(false);
        EtvAmount.setFocusableInTouchMode(false);
        EtvDescription.setFocusableInTouchMode(false);
//                EtvDetail.setFocusableInTouchMode(true);
        EtvName.setFocusableInTouchMode(false);
        EtvNumber.setFocusableInTouchMode(false);
        EtvPname.setFocusableInTouchMode(false);
        EtvType.setFocusableInTouchMode(false);
        EtvTypeRes.setFocusableInTouchMode(false);
        EtvLocation.setFocusableInTouchMode(false);
        EtvAddress.setFocusableInTouchMode(false);
        EtvDate.setFocusableInTouchMode(false);
        for (int i = 0; i < detailsEditTextArray.length; i++) {
            detailsEditTextArray[i].setFocusableInTouchMode(false);
        }
        BtnUpdate.setVisibility(View.GONE);
        BtnCancel.setVisibility(View.GONE);
        BtnLocation.setVisibility(View.GONE);
    }

    private void fetchPropertyData() {
//        try {
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connection = connectionHelper.connectionClass();
//            if (connection != null) {
//
//                String query = "SELECT * FROM `c_property` where pid= '" + pid + "'";
//                Statement st = connection.createStatement();
//                ResultSet rs = st.executeQuery(query);
//
//                while (rs.next()) {
//                    detail = rs.getString("detail");
//                    Log.e("detail", detail);
//                    EtvAmount.setText(rs.getString("amount"));
//                    EtvPname.setText(rs.getString("name"));
//                    EtvName.setText(rs.getString("owner_name"));
//                    EtvAddress.setText(rs.getString("address"));
//                    EtvNumber.setText(rs.getString("owner_number"));
//                    EtvDate.setText(rs.getString("date"));
//                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//                    List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(rs.getString("latitude")), Double.parseDouble(rs.getString("longitude")), 1);
//                    propertyLatitude = Double.valueOf(rs.getString("latitude"));
//                    propertyLongitude = Double.valueOf(rs.getString("longitude"));
//                    EtvLocation.setText(addresses.get(0).getLocality());
//
//                    if (rs.getString("rent_sell").contains("R")) {
//                        EtvType.setText("Rent");
//                    } else {
//                        EtvType.setText("Sell");
//                    }
//                    EtvDescription.setText(rs.getString("description"));
//
//
//                }
//
//
//                String query1 = "SELECT * FROM `o_property_type` where tid= '" + tid + "'";
//                Statement st1 = connection.createStatement();
//                ResultSet rs1 = st1.executeQuery(query1);
//                while (rs1.next()) {
//                    EtvTypeRes.setText(rs1.getString("tname"));
//                }
//
//                splitdetails = detail.split("[//s//d|:]");
////
//
//                detailsEditTextArray = new EditText[splitdetails.length / 2];
//                myTextViews = new TextView[splitdetails.length / 2];
//
//                for (int i = 0; i < splitdetails.length / 2; i++) {
//                    String query2 = "SELECT * FROM `o_property_details` where did= '" + splitdetails[2 * i] + "' and tid='" + tid + "'";
//                    Statement st2 = connection.createStatement();
//                    ResultSet rs2 = st2.executeQuery(query2);
//
//
//                    while (rs2.next()) {
////                        addEditTextDynamically(myLinearLayout,split);
//                        EditText myEditText = new EditText(this); //Context
//                        final TextView rowTextView = new TextView(this);
//
//                        // set some properties of rowTextView or something
//                        SpannableString spanString = new SpannableString(splitdetails[2 * i + 1]);
//                        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
//                        if (!rs2.getString("unit").isEmpty()) {
//                            rowTextView.setText(rs2.getString("dname") + "(" + rs2.getString("unit") + ")");
//                        } else {
//                            rowTextView.setText(rs2.getString("dname"));
//                        }
//                        myEditText.setText(spanString);
//                        myEditText.setFocusableInTouchMode(false);
//
//                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
//                        layoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
//                        layoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
//                        layoutParams.setGravity(Gravity.CENTER);
//                        layoutParams.rowSpec = GridLayout.spec(0);
//                        myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//                        myEditText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue_500));
//
//                        myEditText.setLayoutParams(layoutParams);
//
//
//                        myEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
//                        GridLayout.LayoutParams layoutParams1 = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f));
//                        layoutParams1.width = GridLayout.LayoutParams.WRAP_CONTENT;
//                        layoutParams1.height = GridLayout.LayoutParams.MATCH_PARENT;
//                        layoutParams1.setGravity(Gravity.CENTER);
//                        layoutParams1.rowSpec = GridLayout.spec(1);
//
//                        rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);
//
//                        rowTextView.setLayoutParams(layoutParams1);
//                        Log.e("values", splitdetails.toString());
//
//                        // add the textview to the gridlayout
//                        myGridLayout.addView(myEditText);
//                        myGridLayout.addView(rowTextView);
//
//
//                        // save a reference to the textview for later
//                        detailsEditTextArray[i] = myEditText;
//                        myTextViews[i] = rowTextView;
//                        Log.e("type", rs2.getString(3));
//
//                    }
//                }
//
//            } else {
//                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
//            }
////
////            String query3 = "SELECT * FROM `c_advertise` where pid='" + pid + "'";
////            Statement st3 = connect.createStatement();
////            ResultSet rs3 = st3.executeQuery(query3);
//////                boolean feature=false;
////            try {
////                java.sql.Date dateToBeChecked = null;
////                java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
////
////                while (rs3.next()) {
////                    dateToBeChecked = Date.valueOf(rs3.getString("due_time"));
////                }
////                if (dateToBeChecked.getDate() > currentDate.getDate()) {
////                    FeaturedTxt.setVisibility(View.VISIBLE);
////                } else {
////                    FeaturedTxt.setVisibility(View.GONE);
////                }
////            } catch (Exception e) {
////                //if user's property will not be c_advertise then it will genereate below exception
////                //E/excep: Attempt to invoke virtual method 'int java.sql.Date.getDate()' on a null object reference
////                Log.e("excep", e.getMessage());
////                FeaturedTxt.setVisibility(View.GONE);
////            }
//
//        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
//        }
    }

    private void deleteProperty() {
//        try {
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connection = connectionHelper.connectionClass();
//            Intent intent;
//            if (connection != null) {
//                String query;
//
//                query = "UPDATE `c_property` SET `property_status`='delete' where pid='" + pid + "'";
//                Statement st = connection.createStatement();
//                st.executeUpdate(query);
//
//                ConnectionResult = "Successful Delete";
//                Toast.makeText(getApplicationContext(), "Property Deleted", Toast.LENGTH_LONG).show();
//            } else {
//                ConnectionResult = "Check connection";
//            }
//            connection.close();
//
//        } catch (Exception ex) {
//            Log.e("Error", ex.getMessage());
//        }
    }

    private void soldProperty() {

//        try {
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connection = connectionHelper.connectionClass();
//            Intent intent;
//            if (connection != null) {
//                String query;
//
//                query = "UPDATE `c_property` SET `property_status`='sold' where pid='" + pid + "'";
//                Statement st = connection.createStatement();
//                st.executeUpdate(query);
//
//
//                ConnectionResult = "Update Successful";
//                Toast.makeText(getApplicationContext(), "Property Sold", Toast.LENGTH_LONG).show();
//            } else {
//                ConnectionResult = "Check connection";
//            }
//            connection.close();
//
//        } catch (Exception ex) {
//            Log.e("Error", ex.getMessage());
//        }

    }

    private void fetchImageFromAmplify() {
//        String propertyName = EtvPname.getText().toString();
//        imgList = new ArrayList<>();
//        //get imageid
//        Amplify.Storage.list("",
//                result -> {
//                    for (StorageItem item : result.getItems()) {
//                        Log.i("MyAmplifyApp", "Item: " + item.getKey());
//                        Log.i("MyAmplifyApp url", "Item: " + item.getKey());
//                        //printing image url in log
//                        if (item.getKey().contains(propertyName)) {
//                            Amplify.Storage.getUrl(
//                                    item.getKey(),
//                                    result1 -> {
//                                        imgList.add(result1.getUrl());
//                                        Log.e("url", result1.getUrl().toString());
//                                    },
//                                    error -> Log.e("MyAmplifyApp", "URL generation failure", error)
//                            );
//
//                        }
////                        if(item.getKey().contains("Properties/3/")){
////                            Amplify.Storage.remove(
////                                    item.getKey(),
////                                    result2 -> Log.i("MyAmplifyApp", "Successfully removed: " + result2.getKey()),
////                                    error -> Log.e("MyAmplifyApp", "Remove failure", error)
////                            );
////                        }
//                    }
//                },
//                error -> Log.e("MyAmplifyApp", "List failure", error)
//        );
//        carouselView.setPageCount(5);
//
//        carouselView.setImageListener(imageListener);
//        Log.e("imgurll", imgList.toString());
    }

    //    private void addEditTextDynamically(LinearLayout mParentLayout, String[] myList)
//    {
//        for (int i = 0; i < myList.length; i++)
//        {
//            EditText myEditText = new EditText (mParentLayout.getContext()); //Context
//            myEditText.setLayoutParams(new      LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            myEditText.setId(i);
//            myEditText.setTag(myList[i]);
//            mParentLayout.addView(myEditText);
//        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.c12_c32_edit,menu);
//
////        // Associate searchable configuration with the SearchView
//        MenuItem editBtn = menu.findItem(R.id.edit);
//        editBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                EtvAddress.setFocusableInTouchMode(true);
//                EtvAmount.setFocusableInTouchMode(true);
//                EtvDescription.setFocusableInTouchMode(true);
////                EtvDetail.setFocusableInTouchMode(true);
//                EtvName.setFocusableInTouchMode(true);
//                EtvNumber.setFocusableInTouchMode(true);
//                EtvPname.setFocusableInTouchMode(true);
//                EtvType.setFocusableInTouchMode(true);
//                EtvTypeRes.setFocusableInTouchMode(true);
//                BtnUpdate.setVisibility(View.VISIBLE);
//                BtnDelete.setVisibility(View.VISIBLE);
//                BtnSold.setVisibility(View.VISIBLE);
//
//                return true;
//            }
//        });
//        MenuItem deleteBtn = menu.findItem(R.id.delete);
//        deleteBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                EtvAddress.setFocusableInTouchMode(true);
//                EtvAmount.setFocusableInTouchMode(true);
//                EtvDescription.setFocusableInTouchMode(true);
////                EtvDetail.setFocusableInTouchMode(true);
//                EtvName.setFocusableInTouchMode(true);
//                EtvNumber.setFocusableInTouchMode(true);
//                EtvPname.setFocusableInTouchMode(true);
//                EtvType.setFocusableInTouchMode(true);
//                EtvTypeRes.setFocusableInTouchMode(true);
//                BtnUpdate.setVisibility(View.VISIBLE);
//                BtnDelete.setVisibility(View.VISIBLE);
//                BtnSold.setVisibility(View.VISIBLE);
//
//                return true;
//            }
//        });
//
//
//        return super.onCreateOptionsMenu(menu);
//    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), C31.class));
        finish();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            Bitmap bimage = null;
            String urll = "https://mdrentsell111207-mdrentsell.s3.ap-south-1.amazonaws.com/public/Gautaum_Residency/images2.jpeg?X-Amz-Security-Token=IQoJb3JpZ2luX2VjELP%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FwEaCmFwLXNvdXRoLTEiRzBFAiBUzdh1rlVVLdNrNfm%2Bov0G7JYi7vd85Nm84fE7JWnQ5QIhAJGHN1rLnUOf%2BYTCP%2BoSWtaAj3qwmpYA2u4v2jWgz4VeKpMGCFwQABoMOTA4OTczODIzODM2IgzO0ZwBMt7rgAFG8E4q8AW2JzeIZXnabntCJnrrHmVc3d%2FxX9DghNkrBu3r4mM0q7s7zxtor1F3yKAYXVbfIFgTnasP5OnsEfSw01sQIf5pygp5etfFP3gdHmIoXrW1FA%2Fmuto7X6NzR31ShYuI7ZMGqmrQKdlqUBtkVkls0TwDmCsQh6%2Bj%2BRSSprGFkdDMd2kiuU4S6AWH6flFTZ5Yu%2BckQb0pwbzNRRF3sjhYzfz9AEHtfaCI4sLhnjYxrGAbqdTRI5zW3S3algVQBc1kXwOsvaSTGKo0YmbHW5Fi2IrVPgVquHCH0qSM3fQdIdrkCfEBdDbnKA%2B1ys7v8GhsdYtEpH3Lenj4%2FLkYfieUAU9e%2BQVBhUPv%2BzpEVyuFh7od45%2FdqBMTXEH%2FQRHH2HbdYweqTMq6VVGaKEDpnIiMFIwoiAHW1GRaxlOHn7kXOzO0FhdoGrH3MM%2Fc5w1k1rYXHfa62tgCEeHngy626CipzuBn%2FtBYlUj4V7cc4lj9e3kScP19OirwFzuJK4n9hpQLvsQJIrLvN0mqaW65AvQWNoNuyhwJJ5crTnhXUcTmw0djqoDvHlcWWsrEfFCzTmhwsWv0G8dd%2BhwqE3ICBqkgEhhB5BqR9mcFDtwQQ6JlJ7Q1qnaX0EMxAENsdjXFj87%2BfN5KKHpMBUkUjdv1COFhcPYL73RA68YYvyiV6MUz0fZvBEtyiPMeVBTui%2F%2FS%2F4RI%2FQCTbj2MPYPu6JST9qlX2fg%2FAzHSoBkEKWeOIz9FTlZCvJfLUj42%2F1b4U7qO1vdi53URIKTSHdT5ES6gGEQMzoLyYPydEpcewIixRVI7aREoFdxwOuwXl5i3ndk%2F72ioHP0C9MjgPM3wa%2BRCOGnJnr6E%2BxDgoeH2RDwGOtm%2FBBrkh3ODidO72Ab9fD1s5v2yniM6FL6xgtEy8MYo9kA7kVkYt%2FYjvngb9wDaj3G1I6aUfLZXadnwofVZlr6yHzkIbfJ6cBrDV5Jsmqr0WS1im1wbgT%2BanzCrTRbjnqzsjLLYYDD4%2BY6MBjqHAkvsPh1pGei0zGdgBQhTSJfqqchtcBy5Fp3RcjTqSFODk%2B2gLSxsc7YZG9CAAvr3w194TgRx5cUdgzgFKcHrrhEtkT2aI7ONdVXupJDP9YfPFhVyJt5ViIBhfxQNwDlnGWskPn8SuBI9ppZ2Vey2UvW1DGl6qNI90NxYH5aF2IvmrN8PWz6ow%2BheVNPu9Tto5%2FZiqcQHzL3BbPVDC0XWFGh9hEhEZ%2B6zpHNx84OaZ0TGm79haGAlg9kQZSsJmI%2ByPfbPidm9fvrCa7L4yStF1lTVs42wWGxfbun%2B2KT%2FNgDOkHD8DXX4SK4lUWDXxLVDAx9H2qymk%2F46AmW1DNuFWe92kFmFhVzL&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20211104T111628Z&X-Amz-SignedHeaders=host&X-Amz-Expires=604799&X-Amz-Credential=ASIA5HIYP25OMLBZJC3I%2F20211104%2Fap-south-1%2Fs3%2Faws4_request&X-Amz-Signature=a70bfed867ebc8a021047e6922f6d1897b77afa58dd1032f75fce1ead4aa747a";
            try {
                InputStream in = new java.net.URL(urll).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            imageView.setImageBitmap(bimage);
        }
    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(propertyLatitude, propertyLongitude);
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                .title(EtvPname.getText().toString()));
//        mMap.setMinZoomPreference(16);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK && requestCode==2) {
//            Place place = PlacePicker.getPlace(this, data);
//            double latitude = place.getLatLng().longitude;
//            double longitude = place.getLatLng().longitude;
//            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//            List<Address> addresses = null;
//            try {
//                addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Log.i("address", addresses.toString());
//            propertyLatitude=addresses.get(0).getLatitude();
//            propertyLongitude=addresses.get(0).getLongitude();
//            EtvAddress.setText(addresses.get(0).toString());
//            EtvLocation.setText(addresses.get(0).getLocality());
//            //                    MarkerOptions markerOptions = new MarkerOptions();
////                    markerOptions.position(coordinate);
////                    markerOptions.title(placeName); //Here Total Address is address which you want to show on marker
////                    mGoogleMap.clear();
////                    markerOptions.icon(
////                            BitmapDescriptorFactory
////                                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
////
////                    markerOptions.getPosition();
////                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
////                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
////                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}