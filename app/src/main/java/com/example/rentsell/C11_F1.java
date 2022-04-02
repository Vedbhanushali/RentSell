package com.example.rentsell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.rentsell.C11_Filter.rentFilterPref;
import static com.example.rentsell.C11_Sort.sortId;
import static com.example.rentsell.C11_Sort.sortPrefernce;

public class C11_F1 extends Fragment {
    Connection connect;
    TextView filterBtn, sortBtn, searchView;
    C11F1_C11F2_A f1_adapterVertical, f1_adapterHorizontal;
    ArrayList<C11F1_C11F2_M> c11_f1_horizontalPropertyList = new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f1_verticalPropertyList = new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f1_horizontalPropertyList2 = new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f1_verticalPropertyList2 = new ArrayList<>();

    DatabaseReference dbreference;
    SharedPreferences sortSharedPreferences;
    TextView recommendForU, popularForU;

    RecyclerView f1_recyclerViewHorizontal, f1_recyclerViewVertical;
    LinearLayoutManager f1_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences rentSharedPreference;
    public static C11_Filter bottomSheet_f1;

    public C11_F1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_c11_f1, container, false);
        setHasOptionsMenu(true);
        constraintLayout = rootView.findViewById(R.id.progressAnimation);
        coordinatorLayout = rootView.findViewById(R.id.c11_f1_coordinator);
        linearLayout = rootView.findViewById(R.id.c11_linearLayout);
        coordinatorLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
//        constraintLayout.setVisibility(View.VISIBLE);
//        SwipeRefreshLayout swiperef = rootView.findViewById(R.id.c11_f1_swipeRefresh);
        sortSharedPreferences = getActivity().getSharedPreferences(sortPrefernce, Context.MODE_PRIVATE);
        rentSharedPreference = getActivity().getSharedPreferences(rentFilterPref, Context.MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                coordinatorLayout.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.GONE);
            }

        }, 5000);
        f1_recyclerViewHorizontal = (RecyclerView) rootView.findViewById(R.id.c11_f1_recyclerViewHorizontal);
        f1_recyclerViewHorizontal.setLayoutManager(f1_layoutManager);

        f1_recyclerViewVertical = (RecyclerView) rootView.findViewById(R.id.c11_f1_recyclerViewVertical);
        f1_recyclerViewVertical.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        filterBtn = (TextView) rootView.findViewById(R.id.c11_f1_filterBtn);
        sortBtn = (TextView) rootView.findViewById(R.id.c11_f1_sortBtn);
        searchView = (TextView) rootView.findViewById(R.id.c11_f1_search);
        recommendForU = (TextView) rootView.findViewById(R.id.c11_f1_recommendForYou);
        popularForU = (TextView) rootView.findViewById(R.id.c11_f1_popularForYou);

        getData();


        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheet_f1 = new C11_Filter(new C11_Filter.filterCallback() {
                    @Override
                    public void onCallback(ArrayList<String> types, String category, Float valueFrom, Float valueTo) {
                        ArrayList<C11F1_C11F2_M> fHorizontalList = new ArrayList<>();
                        ArrayList<C11F1_C11F2_M> fVerticalList = new ArrayList<>();
                        Log.e("filterProperties are", types.toString() + ", " + category + ", " + valueFrom + " , " + valueTo);
                        if (!category.contains("Rent")) {
                            ViewPager viewPager = (ViewPager) getActivity().findViewById(
                                    R.id.c11_viewPager);
                            viewPager.setCurrentItem(1);
                        } else {
                            getData();
                            for (int i = 0; i < c11_f1_verticalPropertyList.size(); i++) {
                                if (Float.parseFloat(c11_f1_verticalPropertyList.get(i).getPrice()) > valueFrom &&
                                        Float.parseFloat(c11_f1_verticalPropertyList.get(i).getPrice()) < valueTo) {
                                    for (int j = 0; j < types.size(); j++) {
                                        if (c11_f1_verticalPropertyList.get(i).getType().equals(types.get(j))) {
                                            fVerticalList.add(c11_f1_verticalPropertyList.get(i));
                                        }
                                    }
                                }
                            }
                            for (int i = 0; i < c11_f1_horizontalPropertyList.size(); i++) {
                                if (Float.parseFloat(c11_f1_horizontalPropertyList.get(i).getPrice()) > valueFrom &&
                                        Float.parseFloat(c11_f1_horizontalPropertyList.get(i).getPrice()) < valueTo) {
                                    for (int j = 0; j < types.size(); j++) {
                                        if (c11_f1_horizontalPropertyList.get(i).getType().equals(types.get(j))) {
                                            fHorizontalList.add(c11_f1_horizontalPropertyList.get(i));
                                        }
                                    }
                                }
                            }
//                            C11F1_C11F2_A f2_a=new C11F1_C11F2_A(,getContext().getApplicationContext(),"Rent");
//                            f1_recyclerViewVertical.setAdapter(f2_a);
                            f1_adapterVertical.setFilter(fVerticalList);
                            f1_adapterHorizontal.setFilter(fHorizontalList);
                            if (fHorizontalList.size() != 0) {
                                popularForU.setVisibility(View.VISIBLE);
                            } else {
                                popularForU.setVisibility(View.GONE);
                                Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
                            }
                            if (fVerticalList.size() != 0) {
                                recommendForU.setVisibility(View.VISIBLE);
                            } else {
                                recommendForU.setVisibility(View.GONE);
                                Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
                            }
                        }
//                        fList.clear();
//                        f1_adapterVertical.list=c11_f1_verticalPropertyList;
                    }
                }, "Rent");
                bottomSheet_f1.show(getFragmentManager(),
                        "FilterBottomSheet");

            }
        });
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {


                int checkID = 0;
                if (sortSharedPreferences.contains(sortId)) {
                    checkID = sortSharedPreferences.getInt(sortId, 0);
                }
                C11_Sort c11_sort = new C11_Sort(new C11_Sort.sortCallback() {
                    @Override
                    public void onCallback(int checkedId) {
                        sortData(checkedId);
                    }
                }, checkID);
                c11_sort.show(getFragmentManager(), "SortBottomSheet");


//                Toast.makeText(getContext().getApplicationContext(), item, Toast.LENGTH_SHORT).show();

            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                c11_f1_horizontalPropertyList2 = c11_f1_horizontalPropertyList;
                c11_f1_verticalPropertyList2 = c11_f1_verticalPropertyList;

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    searchFilter(s.toString());
                } else {
                    getData();
                }
            }
        });

        return rootView;

    }

    private void getData() {

        dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("c_property");
        Query query = dbreference.child("cid");
        dbreference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("Task", "Task incomplete");
                } else {
                    ArrayList<C11F1_C11F2_M> propertyList = new ArrayList<>();

                    String address= null, amount = null, cid= null, date= null, description= null, detail= null, latitude= null, longitude = null, name= null, owner_name= null, owner_number= null, pid = null,
                            property_status= null, tid= null, timestamp= null, rent_sell= null;
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        Log.i("value", ds.toString());
                        for (DataSnapshot dsp : ds.getChildren()) {
                            switch (dsp.getKey()) {
                                case "address":
                                    address = (String) dsp.getValue();
                                case "amount":
                                    amount = (String) dsp.getValue();
                                case "cid":
                                    cid = (String) dsp.getValue();
                                case "date":
                                    date = (String) dsp.getValue();
                                case "description":
                                    description = (String) dsp.getValue();
                                case "detail":
                                    detail = (String) dsp.getValue();
                                case "latitude":
                                    latitude = (String) dsp.getValue();
                                case "longitude":
                                    longitude = (String) dsp.getValue();
                                case "name":
                                    name = (String) dsp.getValue();
                                case "owner_name":
                                    owner_name = (String) dsp.getValue();
                                case "owner_number":
                                    owner_number = (String) dsp.getValue();
                                case "pid":
                                    pid = (String) dsp.getValue();
                                case "property_status":
                                    property_status = (String) dsp.getValue();
                                case "rent_sell":
                                    if(dsp.getValue().toString().contains("S")){
                                        rent_sell = "Sell";
                                    }else {
                                        rent_sell = "Rent";
                                    }

                                case "tid":
                                    tid = (String) dsp.getValue();
                                case "timestamp":
                                    timestamp = (String) dsp.getValue();
                                default:
                                    Log.e("switch","default");
                            }

                        }
                        //get locality from latitude and longitude
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String location="";
//                        location= addresses.get(0).getLocality();

                        final String[] property_type = {null};
                        DB_Reference db_reference=new DB_Reference();
                        String finalName = name;
                        String finalRent_sell = rent_sell;
                        String finalTid = tid;
                        String finalPid = pid;
                        String finalTimestamp = timestamp;
                        String finalAmount = amount;
                        String finalLocation = location;
                        db_reference.getType(tid, new DB_Reference.typeCallback() {
                            @Override
                            public void onCallback(String type) {
                                String imgURL="https://firebasestorage.googleapis.com/v0/b/rentsell-65f43.appspot.com/o/Property%2F1_propertyName%2Fhome1.jfif?alt=media&token=02753750-cf9d-4387-b0ed-279019a35d30";
                                if(finalRent_sell.contains("Rent")){
                                    propertyList.add(new C11F1_C11F2_M(finalName, finalAmount, finalLocation, type, finalRent_sell, finalTid, finalPid, finalTimestamp, imgURL));
                                }
                            }
                        });
                    }
                    c11_f1_horizontalPropertyList=propertyList;
                    c11_f1_verticalPropertyList=propertyList;
                    showData2();


                }
            }
        });
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }


    private void sortData(int checkedId) {
//        ArrayList<C11F1_C11F2_M> newList=c11_f1_verticalPropertyList;
        switch (checkedId) {
            case R.id.c11_sort_newest:
                Collections.sort(c11_f1_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp())) > 0) {
                            return -1;
                            //lhs.timestamp value is greater"
                        } else {
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f1_adapterVertical.notifyDataSetChanged();

                Collections.sort(c11_f1_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp())) > 0) {
                            return -1;
                            //lhs.timestamp value is greater"
                        } else {
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f1_adapterHorizontal.notifyDataSetChanged();

                break;
            case R.id.c11_sort_oldest:
                Collections.sort(c11_f1_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp())) < 0) {
                            return -1;
                            //lhs.timestamp value is greater"
                        } else {
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f1_adapterVertical.notifyDataSetChanged();

                Collections.sort(c11_f1_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp())) < 0) {
                            return -1;
                            //lhs.timestamp value is greater"
                        } else {
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f1_adapterHorizontal.notifyDataSetChanged();

                break;
            case R.id.c11_sort_low_high:
                Collections.sort(c11_f1_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Double.parseDouble(lhs.getPrice()) < Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f1_adapterVertical.notifyDataSetChanged();
                Collections.sort(c11_f1_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Double.parseDouble(lhs.getPrice()) < Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f1_adapterHorizontal.notifyDataSetChanged();

                break;
            case R.id.c11_sort_high_low:
                Collections.sort(c11_f1_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Double.parseDouble(lhs.getPrice()) > Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f1_adapterVertical.notifyDataSetChanged();
                Collections.sort(c11_f1_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if (Double.parseDouble(lhs.getPrice()) > Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f1_adapterHorizontal.notifyDataSetChanged();

                break;
            default:
                Toast.makeText(getContext().getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
                break;
        }
        Log.e("checkitem", String.valueOf(checkedId));
    }

    //display data in recyclerView which was fetched from database
    public ArrayList<C11F1_C11F2_M> showData(ResultSet result) {
        ArrayList<C11F1_C11F2_M> propertyList = new ArrayList<>();
//        ResultSet rs = result;
//        try {
//
//            while (rs.next()) {
//                //get locality from latitude and longitude
//                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(rs.getString("latitude")), Double.parseDouble(rs.getString("longitude")), 1);
//                Log.i("address", addresses.toString());
//                String cityName = addresses.get(0).getLocality();
//
//                String pid = null;
//                pid = rs.getString("pid");
//                String tid = null;
//                tid = rs.getString("tid");
//                String timeStamp = rs.getString("timestamp");
//                Log.e("times", timeStamp);
//
//                String query2 = "SELECT * FROM `o_property_type` where tid='" + tid + "'";
//                Statement st2 = connect.createStatement();
//                ResultSet rs2 = st2.executeQuery(query2);
//
//
//                String propertyType = null, propertyFor = null;
//                //getting property for rent/sell
//                propertyFor = rs.getString("rent_sell");
//                Log.e("for", propertyFor);
//
//                //getting property type
//                while (rs2.next()) {
//                    propertyType = rs2.getString("tname");
//                }
//
//                //getting img from
//                final String[] imgUrl = {""};
//                String finalPid = pid;
//
//
//                propertyList.add(new C11F1_C11F2_M(rs.getString("name"), rs.getString("amount"), cityName, propertyType, propertyFor, tid, pid, timeStamp, imgUrl[0]));
//
//            }
//
//        } catch (Exception E) {
//            Toast.makeText(getContext(), E.getMessage(), Toast.LENGTH_SHORT).show();
//        }


        Log.e("propertyy", propertyList.toString());

        return propertyList;
    }

    public void showData2() {

        f1_adapterHorizontal = new C11F1_C11F2_A(c11_f1_horizontalPropertyList, getContext(), "horizontal");
        f1_recyclerViewHorizontal.setAdapter(f1_adapterHorizontal);

        f1_adapterVertical = new C11F1_C11F2_A(c11_f1_verticalPropertyList, getContext(), "vertical");
        f1_recyclerViewVertical.setAdapter(f1_adapterVertical);

        //add autoscroll to horizontal recyclerview
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(f1_recyclerViewHorizontal);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (f1_layoutManager.findLastCompletelyVisibleItemPosition() < (f1_adapterHorizontal.getItemCount() - 1)) {
                    f1_layoutManager.smoothScrollToPosition(f1_recyclerViewHorizontal, new RecyclerView.State(), f1_layoutManager.findLastVisibleItemPosition() + 1);
                } else if (f1_layoutManager.findLastCompletelyVisibleItemPosition() == (f1_adapterHorizontal.getItemCount() - 1)) {
                    f1_layoutManager.smoothScrollToPosition(f1_recyclerViewHorizontal, new RecyclerView.State(), 0);
                }
            }
        }, 1000, 3000);

        if (sortSharedPreferences.contains(sortId)) {
            sortData(sortSharedPreferences.getInt(sortId, 0));
        } else {
            sortData(R.id.c11_sort_newest);

        }

//        coordinatorLayout.setVisibility(View.VISIBLE);
//        linearLayout.setVisibility(View.VISIBLE);
//        constraintLayout.setVisibility(View.GONE);
    }


    //search view code==============================================================================

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchFilter(String text) {
        //new array list that will hold the filtered data
        ArrayList<C11F1_C11F2_M> horizontalFilterdNames = new ArrayList<>();
        ArrayList<C11F1_C11F2_M> verticalFilterdNames = new ArrayList<>();

        //looping through existing elements
        for (C11F1_C11F2_M s : c11_f1_horizontalPropertyList) {
            //if the existing elements contains the search input
            if (s.getLocation().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                horizontalFilterdNames.add(s);
            }
        }

        for (C11F1_C11F2_M s : c11_f1_verticalPropertyList) {
            //if the existing elements contains the search input
            if (s.getLocation().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                verticalFilterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list

        f1_adapterHorizontal.setFilter(horizontalFilterdNames);
        f1_adapterVertical.setFilter(verticalFilterdNames);
        if (horizontalFilterdNames.size() != 0) {
            popularForU.setVisibility(View.VISIBLE);
        } else {
            popularForU.setVisibility(View.GONE);
            Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
        }
        if (verticalFilterdNames.size() != 0) {
            recommendForU.setVisibility(View.VISIBLE);
        } else {
            recommendForU.setVisibility(View.GONE);
            Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        SharedPreferences.Editor editor = rentSharedPreference.edit();
        editor.clear();
        editor.apply();
        editor.commit();

        SharedPreferences.Editor editor1 = sortSharedPreferences.edit();
        editor1.clear();
        editor1.apply();
        editor1.commit();
        Log.e("onstop", "deleteshared");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}