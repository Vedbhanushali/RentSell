package com.example.rentsell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.rentsell.C11_Filter.sellFilterPref;
import static com.example.rentsell.C11_Sort.sortId;
import static com.example.rentsell.C11_Sort.sortPrefernce;
public class C11_F2 extends Fragment {

    Connection connect;
    TextView filterBtn,sortBtn,searchView;
    C11F1_C11F2_A f2_adapterVertical,f2_adapterHorizontal;
    ArrayList<C11F1_C11F2_M> c11_f2_horizontalPropertyList=new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f2_verticalPropertyList=new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f2_horizontalPropertyList2=new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f2_verticalPropertyList2=new ArrayList<>();

    SharedPreferences sortSharedPreferences, sellSharedPreference;;
    TextView recommendForU,popularForU;

    RecyclerView f2_recyclerViewHorizontal,f2_recyclerViewVertical;
    LinearLayoutManager f2_layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

    public static C11_Filter bottomSheet_f2;
    public C11_F2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_c11_f2, container, false);
        setHasOptionsMenu(true);

//        SwipeRefreshLayout swiperef = rootView.findViewById(R.id.c11_f2_swipeRefresh);

        f2_recyclerViewHorizontal=(RecyclerView)rootView.findViewById(R.id.c11_f2_recyclerViewHorizontal);
        f2_recyclerViewHorizontal.setLayoutManager(f2_layoutManager);

        f2_recyclerViewVertical=(RecyclerView)rootView.findViewById(R.id.c11_f2_recyclerViewVertical);
        f2_recyclerViewVertical.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        filterBtn=(TextView)rootView.findViewById(R.id.c11_f2_filterBtn);
        sortBtn=(TextView)rootView.findViewById(R.id.c11_f2_sortBtn);
        searchView=(TextView) rootView.findViewById(R.id.c11_f2_search);
        recommendForU=(TextView)rootView.findViewById(R.id.c11_f2_recommendForYou);
        popularForU=(TextView)rootView.findViewById(R.id.c11_f2_popularForYou);

        sellSharedPreference= getActivity().getSharedPreferences(sellFilterPref, Context.MODE_PRIVATE);

        getData();


        if(sellSharedPreference.contains("category")){
            Set<String> stringSet=sellSharedPreference.getStringSet("types", Collections.singleton(""));
            ArrayList<String> typeList= new ArrayList<>();
            typeList.addAll(stringSet);

            //            ArrayList<String> typeList= (ArrayList<String>) rentSharedPreference.getStringSet("types", Collections.singleton(""));
            Float valueFrom = Float.valueOf(sellSharedPreference.getString("valueFrom"," "));
            Float valueTo=Float.valueOf(sellSharedPreference.getString("valueTo"," "));

            ArrayList<C11F1_C11F2_M> fHorizontalList=new ArrayList<>();
            ArrayList<C11F1_C11F2_M> fVerticalList=new ArrayList<>();
            getData();
            for(int i=0;i<c11_f2_verticalPropertyList.size();i++){
                if(Float.parseFloat(c11_f2_verticalPropertyList.get(i).getPrice()) > valueFrom &&
                        Float.parseFloat(c11_f2_verticalPropertyList.get(i).getPrice()) < valueTo){
                    for (int j=0;j<typeList.size();j++){
                        if(c11_f2_verticalPropertyList.get(i).getType().equals(typeList.get(j))){
                            fVerticalList.add(c11_f2_verticalPropertyList.get(i));
                        }
                    }
                }
            }
            for(int i=0;i<c11_f2_horizontalPropertyList.size();i++){
                if(Float.parseFloat(c11_f2_horizontalPropertyList.get(i).getPrice()) > valueFrom &&
                        Float.parseFloat(c11_f2_horizontalPropertyList.get(i).getPrice()) < valueTo){
                    for (int j=0;j<typeList.size();j++){
                        if(c11_f2_horizontalPropertyList.get(i).getType().equals(typeList.get(j))){
                            fHorizontalList.add(c11_f2_horizontalPropertyList.get(i));
                        }
                    }
                }
            }
//                            C11F1_C11F2_A f2_a=new C11F1_C11F2_A(,getContext().getApplicationContext(),"Rent");
//                            f1_recyclerViewVertical.setAdapter(f2_a);
            f2_adapterVertical.setFilter(fVerticalList);
            f2_adapterHorizontal.setFilter(fHorizontalList);


        }
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet_f2 = new C11_Filter(new C11_Filter.filterCallback() {
                    @Override
                    public void onCallback(ArrayList<String> types, String category, Float valueFrom, Float valueTo) {

                        ArrayList<C11F1_C11F2_M> fHorizontalList=new ArrayList<>();
                        ArrayList<C11F1_C11F2_M> fVerticalList=new ArrayList<>();

                        Log.e("filterProperties are",types.toString()+", "+category+", "+valueFrom+" , "+valueTo);
                        if(!category.contains("Sell")){
                            ViewPager viewPager = (ViewPager) getActivity().findViewById(
                                    R.id.c11_viewPager);
                            viewPager.setCurrentItem(0);
                        }else {
                            getData();
                            for(int i=0;i<c11_f2_verticalPropertyList.size();i++){
                                if(Float.parseFloat(c11_f2_verticalPropertyList.get(i).getPrice()) > valueFrom &&
                                        Float.parseFloat(c11_f2_verticalPropertyList.get(i).getPrice()) < valueTo){
                                    for (int j=0;j<types.size();j++){
                                        if(c11_f2_verticalPropertyList.get(i).getType().equals(types.get(j))){
                                            fVerticalList.add(c11_f2_verticalPropertyList.get(i));
                                        }
                                    }
                                }
                            }
                            for(int i=0;i<c11_f2_horizontalPropertyList.size();i++){
                                if(Float.parseFloat(c11_f2_horizontalPropertyList.get(i).getPrice()) > valueFrom &&
                                        Float.parseFloat(c11_f2_horizontalPropertyList.get(i).getPrice()) < valueTo){
                                    for (int j=0;j<types.size();j++){
                                        if(c11_f2_horizontalPropertyList.get(i).getType().equals(types.get(j))){
                                            fHorizontalList.add(c11_f2_horizontalPropertyList.get(i));
                                        }
                                    }
                                }
                            }
//                            C11F1_C11F2_A f2_a=new C11F1_C11F2_A(,getContext().getApplicationContext(),"Rent");
//                            f1_recyclerViewVertical.setAdapter(f2_a);
                            f2_adapterVertical.setFilter(fVerticalList);
                            f2_adapterHorizontal.setFilter(fHorizontalList);
                            if(fHorizontalList.size()!=0){
                                popularForU.setVisibility(View.VISIBLE);
                            }else {
                                popularForU.setVisibility(View.GONE);
                                Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
                            }
                            if(fVerticalList.size()!=0){
                                recommendForU.setVisibility(View.VISIBLE);
                            }else {
                                recommendForU.setVisibility(View.GONE);
                                Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        Log.e("shared",sellSharedPreference.getAll().toString());
                        Log.e("filterProperties are",types.toString()+", "+category+", "+valueFrom+" , "+valueTo);

                    }
                },"Sell");
                bottomSheet_f2.show(getFragmentManager(),
                        "FilterBottomSheet");

            }
        });
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {


                int checkID = 0;
                sortSharedPreferences= getActivity().getSharedPreferences(sortPrefernce, Context.MODE_PRIVATE);
                if(sortSharedPreferences.contains(sortId)){
                    checkID=sortSharedPreferences.getInt(sortId,0);
                }
                C11_Sort c11_sort=new C11_Sort(new C11_Sort.sortCallback() {
                    @Override
                    public void onCallback(int checkedId) {
                        sortData(checkedId);
                    }
                },checkID);
                c11_sort.show(getFragmentManager(),"SortBottomSheet");


//                Toast.makeText(getContext().getApplicationContext(), item, Toast.LENGTH_SHORT).show();

            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    filter(s.toString());
                }else {
//                    f2_adapterVertical.setFilter(c11_f2_horizontalPropertyList2);
//                    f2_adapterHorizontal.setFilter(c11_f2_verticalPropertyList2);
                    getData();
                }
            }
        });

        return rootView;
//        swiperef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                propertyList.clear();
//                getData();
//                swiperef.setRefreshing(false);
//            }
//        });

        // Inflate the layout for this fragment

    }

    private void sortData(int checkedId) {
//        ArrayList<C11F1_C11F2_M> newList=c11_f2_verticalPropertyList;
        switch(checkedId){
            case R.id.c11_sort_newest:
                Collections.sort(c11_f2_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp()))>0){
                            return -1;
                            //lhs.timestamp value is greater"
                        }
                        else{
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f2_adapterVertical.notifyDataSetChanged();

                Collections.sort(c11_f2_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp()))>0){
                            return -1;
                            //lhs.timestamp value is greater"
                        }
                        else{
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f2_adapterHorizontal.notifyDataSetChanged();

                break;
            case R.id.c11_sort_oldest:
                Collections.sort(c11_f2_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp()))<0){
                            return -1;
                            //lhs.timestamp value is greater"
                        }
                        else{
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f2_adapterVertical.notifyDataSetChanged();

                Collections.sort(c11_f2_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Timestamp.valueOf(lhs.getTimeStamp()).compareTo(Timestamp.valueOf(rhs.getTimeStamp()))<0){
                            return -1;
                            //lhs.timestamp value is greater"
                        }
                        else{
                            return 1;
                            //rhs.timestamp value is greater"
                        }
                    }
                });
                f2_adapterHorizontal.notifyDataSetChanged();

                break;
            case R.id.c11_sort_low_high:
                Collections.sort(c11_f2_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Double.parseDouble(lhs.getPrice()) < Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f2_adapterVertical.notifyDataSetChanged();
                Collections.sort(c11_f2_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Double.parseDouble(lhs.getPrice()) < Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f2_adapterHorizontal.notifyDataSetChanged();

                break;
            case R.id.c11_sort_high_low:
                Collections.sort(c11_f2_verticalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Double.parseDouble(lhs.getPrice()) > Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f2_adapterVertical.notifyDataSetChanged();
                Collections.sort(c11_f2_horizontalPropertyList, new Comparator<C11F1_C11F2_M>() {
                    @Override
                    public int compare(C11F1_C11F2_M lhs, C11F1_C11F2_M rhs) {
                        if(Double.parseDouble(lhs.getPrice()) > Double.parseDouble(rhs.getPrice())) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                f2_adapterHorizontal.notifyDataSetChanged();

                break;
            default:
                Toast.makeText(getContext().getApplicationContext(), "Default", Toast.LENGTH_SHORT).show();
                break;
        }
        Log.e("checkitem", String.valueOf(checkedId));
    }

    //display data in recyclerView which was fetched from database
    public ArrayList<C11F1_C11F2_M> showData(ResultSet result){
        ArrayList<C11F1_C11F2_M> propertyList=new ArrayList<>();
        ResultSet rs=result;
        try {
//            if(rs.next()==false){
//                Toast.makeText(getContext().getApplicationContext(), "Data is null ", Toast.LENGTH_SHORT).show();
//            }else {
            while (rs.next()) {
                //get locality from latitude and longitude
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(rs.getString("latitude")), Double.parseDouble(rs.getString("longitude")), 1);
                Log.i("address", addresses.toString());
                String stateName = addresses.get(0).getLocality();



                String pid=null;
                pid=rs.getString("pid");
                String tid=null;
                tid = rs.getString("tid");
                String timeStamp=rs.getString("timestamp");
                Log.e("times",timeStamp);

                String query2 = "SELECT * FROM `o_property_type` where tid='"+tid+"'";
                Statement st2 = connect.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);


                String propertyType = null,propertyFor=null;
                //getting property for rent/sell
                propertyFor=rs.getString("rent_sell");
                Log.e("for",propertyFor);

                //getting property type
                while (rs2.next()) {
                    propertyType = rs2.getString("tname");
                }

                //get img url from aws
                String imgUrl=null;
                propertyList.add(new C11F1_C11F2_M(rs.getString("name"), rs.getString("amount"), stateName,propertyType,propertyFor,tid,pid,timeStamp,imgUrl));
//                    c11_f2_horizontalPropertyList2.add(new C11F1_C11F2_M(rs.getString("name"), rs.getString("amount"), stateName,propertyType,propertyFor,tid,pid));

            }

        }catch (Exception E){
            Toast.makeText(getContext(), E.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Log.e("propertyy",propertyList.toString());
        return propertyList;
    }
    public  void showData2(){


        f2_adapterHorizontal = new C11F1_C11F2_A(c11_f2_horizontalPropertyList, getContext(), "horizontal");
        f2_recyclerViewHorizontal.setAdapter(f2_adapterHorizontal);

        f2_adapterVertical = new C11F1_C11F2_A(c11_f2_verticalPropertyList2, getContext(), "vertical");
        f2_recyclerViewVertical.setAdapter(f2_adapterVertical);
        //add autoscroll to horizontal recyclerview
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(f2_recyclerViewHorizontal);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (f2_layoutManager.findLastCompletelyVisibleItemPosition() < (f2_adapterHorizontal.getItemCount() - 1)) {
                    f2_layoutManager.smoothScrollToPosition(f2_recyclerViewHorizontal, new RecyclerView.State(), f2_layoutManager.findLastVisibleItemPosition() + 1);
                } else if (f2_layoutManager.findLastCompletelyVisibleItemPosition() == (f2_adapterHorizontal.getItemCount() - 1)) {
                    f2_layoutManager.smoothScrollToPosition(f2_recyclerViewHorizontal, new RecyclerView.State(), 0);
                }
            }
        }, 1000, 3000);
        if(sortSharedPreferences.contains(sortId)){
            sortData(sortSharedPreferences.getInt(sortId,R.id.c11_sort_newest));
        }
    }
    private void getData() {
//        propertyList.add(new C11F1_C11F2_M("Apartment","10000","Ahmedabad"))
//getting data of properties from database
//        try {
//            String connectionResult;
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query;
//
//                query = "SELECT * FROM `c_property` where rent_sell='S' AND property_status='pending'";
//                Statement st = connect.createStatement();
//                ResultSet rs = st.executeQuery(query);
//
//                //call showData function to show data in horizontal recyclerView
//                c11_f2_verticalPropertyList=showData(rs);
//                c11_f2_verticalPropertyList2=c11_f2_verticalPropertyList;
//
//                String query2 = "SELECT * FROM `c_property` WHERE exists (SELECT 1 FROM `c_advertise` WHERE c_property.pid=pid) AND rent_sell='S' AND property_status='pending'";
//                Statement st2 = connect.createStatement();
//                ResultSet rs2 = st2.executeQuery(query2);
//
//                c11_f2_horizontalPropertyList=showData(rs2);
//                c11_f2_horizontalPropertyList2=c11_f2_horizontalPropertyList;
//
//                showData2();
//                connectionResult = "Successful fetched";
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//        }

    }

    //search view code==============================================================================

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.c11_search, menu);
//        // Associate searchable configuration with the SearchView
//        final MenuItem searchItem = menu.findItem(R.id.c11_search_menu);
//        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }
//it was for search view
//    @Override
//    public boolean onMenuItemActionExpand(MenuItem item) {
//        return true;
//    }
//
//    @Override
//    public boolean onMenuItemActionCollapse(MenuItem item) {
//        f2_adapterHorizontal.setFilter(c11_f2_propertyList);
//        f2_adapterVertical.setFilter(c11_f2_propertyList);
//        return true;
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        final ArrayList<C11F1_C11F2_M> filteredNewsList = new ArrayList<>();
//        for (C11F1_C11F2_M model : c11_f2_propertyList) {
//            final String modelLocation = model.getLocation().toLowerCase();
//            if (modelLocation.contains(query)) {
//                filteredNewsList.add(model);
//            }
//        }
//        f2_adapterHorizontal.setFilter(filteredNewsList);
//        f2_adapterVertical.setFilter(filteredNewsList);
//        return true;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        newText = newText.toLowerCase();
//        final ArrayList<C11F1_C11F2_M> filteredNewsList = new ArrayList<>();
//        if (newText == null || newText.trim().isEmpty()) {
//            filteredNewsList.addAll(c11_f2_propertyList);
//            f2_adapterHorizontal.setFilter(filteredNewsList);
//            f2_adapterVertical.setFilter(filteredNewsList);
//            return false;
//        }
//        else {
//            for (C11F1_C11F2_M model : c11_f2_propertyList) {
//                final String modelLocation = model.getLocation().toLowerCase();
//                if (modelLocation.contains(newText)) {
//                    filteredNewsList.add(model);
//                }
//            }
//            f2_adapterHorizontal.setFilter(filteredNewsList);
//            f2_adapterVertical.setFilter(filteredNewsList);
//            return true;
//        }
//   }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<C11F1_C11F2_M> horizontalFilterdNames = new ArrayList<>();
        ArrayList<C11F1_C11F2_M> verticalFilterdNames = new ArrayList<>();

        //looping through existing elements
        for (C11F1_C11F2_M s : c11_f2_horizontalPropertyList) {
            //if the existing elements contains the search input
            if (s.getLocation().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                horizontalFilterdNames.add(s);
            }
        }

        for (C11F1_C11F2_M s : c11_f2_verticalPropertyList) {
            //if the existing elements contains the search input
            if (s.getLocation().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                verticalFilterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list

        f2_adapterHorizontal.setFilter(horizontalFilterdNames);
        f2_adapterVertical.setFilter(verticalFilterdNames);
        if(horizontalFilterdNames.size()!=0){
            popularForU.setVisibility(View.VISIBLE);
        }else {
            popularForU.setVisibility(View.GONE);
            Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
        }
        if(verticalFilterdNames.size()!=0){
            recommendForU.setVisibility(View.VISIBLE);
        }else {
            recommendForU.setVisibility(View.GONE);
            Toast.makeText(getContext().getApplicationContext(), "Sorry No result Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        SharedPreferences.Editor editor=sellSharedPreference.edit();
        editor.clear();
        editor.apply();
        editor.commit();

        sortSharedPreferences= getActivity().getSharedPreferences(sortPrefernce, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor1=sortSharedPreferences.edit();
        editor1.clear();
        editor1.apply();
        editor1.commit();
        Log.e("ondestroy","deleteshared");

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}