package com.example.rentsell;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.rentsell.C11_F1.bottomSheet_f1;
import static com.example.rentsell.C11_F2.bottomSheet_f2;

public class C11_Filter extends BottomSheetDialogFragment {

    public ChipGroup typeChipGroup,categoryChipGroup;
    public ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    ArrayList<String> typeList=new ArrayList<>();
    ArrayList<String> tidList=new ArrayList<>();
    List<Float> filterRangeSlider=new ArrayList<>();
    RangeSlider rangeSlider;
    Connection connect;
    String valueTo,valueFrom,stepSize;
    String category,filteredCategory;
    Chip[] categoryChipList=new Chip[2],typeChipList;
    private filterCallback filterListener;
    public static final String sellFilterPref="sellFilterPref",rentFilterPref="rentFilterPref";
    SharedPreferences sellFilterPreference,rentFilterPreference;

    DatabaseReference dbreference;


    public C11_Filter(filterCallback callback,String category){
        filterListener=callback;
        this.category=category;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.c11_filter_layout,
                container, false);
        Button showResultButton = v.findViewById(R.id.c11_filter_showResultBtn);
        ImageButton back=v.findViewById(R.id.c11_filter_backBtn);
        TextView reset=v.findViewById(R.id.c11_filter_resetBtn);
        rangeSlider=v.findViewById(R.id.c11_filter_rangeSlider);

        typeChipGroup=v.findViewById(R.id.chips_group);

        categoryChipGroup=v.findViewById(R.id.c11_filter_categoryChipsGroup);
        sellFilterPreference= getActivity().getSharedPreferences(sellFilterPref, Context.MODE_PRIVATE);
        rentFilterPreference= getActivity().getSharedPreferences(rentFilterPref, Context.MODE_PRIVATE);

        //        typeChipGroup.getCheckedChipIds();

        getRangeFromDatabase();
        getTypesFromDatabase();


        //setting type and getting selected types
        for (int i=0;i<2;i++){
            categoryChipList[i]= new Chip(v.getContext());
            categoryChipList[i].setId(i);
            categoryChipList[i].setTag(i);
            Log.e("tag"+i,categoryChipList[i].getTag().toString());
            if(i==0){
                categoryChipList[i].setText(R.string.c11_filter_rent);
            }else {
                categoryChipList[i].setText(R.string.c11_filter_buy_sell);
            }
            categoryChipList[i].setCheckable(true);
            categoryChipGroup.addView(categoryChipList[i]);
        }
        if(category.contains("Rent")){
            categoryChipList[0].setChecked(true);
        }
        else {
            categoryChipList[1].setChecked(true);
        }

        typeChipList=new Chip[typeList.size()];

        for(int i = 0; i < typeList.size(); i++) {

            Chip chip = new Chip(v.getContext());
            chip.setId(i);
            chip.setTag(i);
            Log.e("tag"+i,chip.getTag().toString());

            booleanArrayList.add(true);
            chip.setText(typeList.get(i));
            chip.setCheckable(true);
            chip.setChecked(true);


            rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
                @Override
                public void onStartTrackingTouch(@NonNull  RangeSlider slider) {
                    Log.e("range",slider.getValues().toString());

                }

                @Override
                public void onStopTrackingTouch(@NonNull  RangeSlider slider) {
                    Log.e("range2",slider.getValues().toString());
                    filterRangeSlider.clear();
                    filterRangeSlider=slider.getValues();
                }
            });


            categoryChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ChipGroup group, int checkedId) {
                    if(group.getCheckedChipId()==0){
                        filteredCategory = "Rent";
                        ViewPager viewPager = (ViewPager) getActivity().findViewById(
                                R.id.c11_viewPager);
                        viewPager.setCurrentItem(0);
                        bottomSheet_f2.dismiss();
//                        bottomSheet_f1.show(getActivity().getSupportFragmentManager(),"rent filter fragment");
                    }else {
                        filteredCategory="Sell";
                        ViewPager viewPager = (ViewPager) getActivity().findViewById(
                                R.id.c11_viewPager);
                        viewPager.setCurrentItem(1);
                        bottomSheet_f1.dismiss();
//                        bottomSheet_f2.show(getActivity().getSupportFragmentManager(),"sell filter fragment");
//                        bottomSheet_f1.dismiss();
                    }
                }
            });
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    int tag = (int) compoundButton.getTag();
                    booleanArrayList.set(tag, b);
                    Log.e("tag",booleanArrayList.toString());

                }
            });
            typeChipList[i]=chip;
            typeChipGroup.addView(chip);

        }

        typeChipGroup.invalidate();

        if(category.contains("Rent")){
            if(rentFilterPreference.contains("category")){
                Set<String> stringSet=rentFilterPreference  .getStringSet("types", Collections.singleton(""));
                ArrayList<String> FilterTpyes= new ArrayList<>();
                FilterTpyes.addAll(stringSet);
//                ArrayList<String> FilterTpyes= (ArrayList<String>) rentFilterPreference.getStringSet("types", Collections.singleton(""));
//                for(int i=0;i<typeList.size();i++){
//                    if(rentFilterPreference.getBoolean(typeList.get(i),false)){
//                        typeChipList[i].setChecked(true);
//                    }
//                }
                for(int i=0;i<typeList.size();i++){
                    boolean checkStatus=false;
                    for(int j=0;j<FilterTpyes.size();j++){
                        if (typeList.get(i).contains(FilterTpyes.get(j))) {
//                                typeChipList[j].setChecked(true);
                            checkStatus=true;
                        }
                    }
                    if(checkStatus){
                        typeChipList[i].setChecked(true);
                    }else{
                        typeChipList[i].setChecked(false);
                    }
                }
                List<Float> rangeList=new ArrayList<>();
                rangeList.add(Float.valueOf(rentFilterPreference.getString("valueFrom"," ")));
                rangeList.add(Float.valueOf(rentFilterPreference.getString("valueTo"," ")));
                rangeSlider.setValues(rangeList);
            }
        }else {
            if(sellFilterPreference.contains("category")){
                Set<String> stringSet=sellFilterPreference  .getStringSet("types", Collections.singleton(""));
                ArrayList<String> FilterTpyes= new ArrayList<>();
                FilterTpyes.addAll(stringSet);
//                ArrayList<String> FilterTpyes= (ArrayList<String>) rentFilterPreference.getStringSet("types", Collections.singleton(""));
//                for(int i=0;i<typeList.size();i++){
//                    if(sellFilterPreference.getBoolean(typeList.get(i),false)){
//                        typeChipList[i].setChecked(true);
//                    }
//                }
                for(int i=0;i<typeList.size();i++){
                    boolean checkStatus=false;
                    for(int j=0;j<FilterTpyes.size();j++){
                        if (typeList.get(i).contains(FilterTpyes.get(j))) {
//                                typeChipList[j].setChecked(true);
                            checkStatus=true;
                        }
                    }
                    if(checkStatus){
                        typeChipList[i].setChecked(true);
                    }else{
                        typeChipList[i].setChecked(false);
                    }
                }
                List<Float> rangeList=new ArrayList<>();
                rangeList.add(Float.valueOf(sellFilterPreference.getString("valueFrom"," ")));
                rangeList.add(Float.valueOf(sellFilterPreference.getString("valueTo"," ")));
                rangeSlider.setValues(rangeList);
            }
        }

        showResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ArrayList<String> filterType=new ArrayList<>();
                for (int i = 0; i < booleanArrayList.size(); i++) {
                    if(booleanArrayList.get(i)){
                        filterType.add(typeList.get(i));
                    }
                    Log.e("RESULT", typeList.get(i)  + " :" + booleanArrayList.get(i));
                }
                if(filteredCategory==null){
                    filteredCategory=category;
                }
                filterListener.onCallback(filterType,filteredCategory,filterRangeSlider.get(0),filterRangeSlider.get(1));
                if(filteredCategory.contains("Sell")){
                    SharedPreferences.Editor editor=sellFilterPreference.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();

                    Set<String> filterTypeSet = new HashSet<String>();
                    filterTypeSet.addAll(filterType);
                    SharedPreferences.Editor editor2 = sellFilterPreference.edit();
                    editor2.putString("category","sell");
//                    for (int i = 0; i < filterType.size(); i++) {
//                        editor.putBoolean(filterType.get(i), booleanArrayList.get(i));
//                    }
                    editor2.putStringSet("types", filterTypeSet);
                    editor2.putString("valueFrom", filterRangeSlider.get(0).toString());
                    editor2.putString("valueTo", filterRangeSlider.get(1).toString());
                    editor2.commit();
                }else {
                    SharedPreferences.Editor editor=rentFilterPreference.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();

                    Set<String> filterTypeSet = new HashSet<String>();
                    filterTypeSet.addAll(filterType);
                    SharedPreferences.Editor editor2 = rentFilterPreference.edit();
                    editor2.putString("category","rent");
//                    for (int i = 0; i < filterType.size(); i++) {
//                        editor.putBoolean(filterType.get(i), booleanArrayList.get(i));
//                    }
                    editor2.putStringSet("types", filterTypeSet);
                    editor2.putString("valueFrom", filterRangeSlider.get(0).toString());
                    editor2.putString("valueTo", filterRangeSlider.get(1).toString());
                    editor2.commit();
                }
                Toast.makeText(getActivity(), "showing result", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset slider values
                resetData();
                if(category.contains("Sell")) {
                    SharedPreferences.Editor editor = sellFilterPreference.edit();
                    editor.clear();
                    editor.apply();
                    editor.commit();
                }else {
                    SharedPreferences.Editor editor2 = rentFilterPreference.edit();
                    editor2.clear();
                    editor2.apply();
                    editor2.commit();
                }
                filterListener.onCallback(typeList,category,filterRangeSlider.get(0),filterRangeSlider.get(1));
                Toast.makeText(getActivity(), "reset", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
                dismiss();
            }
        });


        typeChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip != null)
                    Toast.makeText(getContext().getApplicationContext(), "Chip is " + chip.getText().toString(), Toast.LENGTH_SHORT).show();

                Log.e("OnCheckedChangeListener", "Called");
            }
        });

        return v;
    }

    private void resetData() {
        filterRangeSlider.clear();
        filterRangeSlider.add(Float.valueOf(valueFrom));
        filterRangeSlider.add(Float.valueOf(valueTo));
        rangeSlider.setValues(filterRangeSlider);
        for (int i=0;i<typeList.size();i++){
            typeChipList[i].setChecked(true);
        }

        if(category.contains("Rent")){
            categoryChipList[0].setChecked(true);
        }
        else {
            categoryChipList[1].setChecked(true);
        }
//        if(category.contains("Rent")){
//        }
//        else {
//
//            ArrayList<String> filterType=new ArrayList<>();
//            for (int i = 0; i < booleanArrayList.size(); i++) {
//                if(booleanArrayList.get(i)){
//                    filterType.add(typeList.get(i));
//                }
//                Log.e("filterList", typeList.get(i)  + " :" + booleanArrayList.get(i));
//            }
//            SharedPreferences.Editor editor=sellFilterPreference.edit();
//
//            for(int i=0;i<filterType.size();i++){
//                editor.putBoolean(filterType.get(i),booleanArrayList.get(i));
//            }
//            editor.putString("valueFrom",filterRangeSlider.get(0).toString());
//            editor.putString("valueTo",filterRangeSlider.get(1).toString());
//            Log.e("editor",editor.toString());
//            editor.commit();
//        }

    }

    //
//    ChipGroup.OnCheckedChangeListener onCheckedChangeListener = new ChipGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(ChipGroup chipGroup, int i) {
//            category= String.valueOf(chipGroup.getCheckedChipId());
//            Log.e("cate",category);
//        }
//    };
    public  void  getTypesFromDatabase(){
        dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("o_parameter");
        Query query = dbreference.child("name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        try {
//            String connectionResult;
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query;
//
//                query = "SELECT * FROM `o_property_type`";
//                Statement st = connect.createStatement();
//                ResultSet rs = st.executeQuery(query);
//
//                while (rs.next()){
//                    typeList.add(rs.getString("tname"));
//                    tidList.add(rs.getString("tid"));
//                }
//                connectionResult = "Successful fetched";
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//        }

    }
    public  void getRangeFromDatabase(){
        dbreference = FirebaseDatabase.getInstance().getReference("RentSell").child("o_parameter");
        Query query = dbreference.orderByChild("name").equalTo("valueFrom");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        try {
//            String connectionResult;
//            C00_connectionHelper connectionHelper = new C00_connectionHelper();
//            connect = connectionHelper.connectionClass();
//            if (connect != null) {
//                String query;
//
//                query = "SELECT * FROM `o_parameter` where name='valueTo' OR name='valueFrom' OR name='stepSize'";
//                Statement st = connect.createStatement();
//                ResultSet rs = st.executeQuery(query);
//
//                while (rs.next()){
//                    if(rs.getString("name").contains("valueTo")){
//                        valueTo=rs.getString("value");
//                        Log.e("to =", rs.getString("value"));
//
//                    }
//
//                    if(rs.getString("name").contains("valueFrom")){
//                        valueFrom=rs.getString("value");
//                        Log.e("from =", rs.getString("value"));
//                    }
//
//                    if(rs.getString("name").contains("stepSize")){
//                        stepSize=rs.getString("value");
//                        Log.e("step =", rs.getString("value"));
//                    }
//                }
//                connectionResult = "Successful fetched";
//            } else {
//                connectionResult = "Check connection";
//            }
//            connect.close();
//        } catch (Exception ex) {
//            Log.e("Error in", ex.getMessage());
//        }        //setting range on rangesSlider
        filterRangeSlider.add(Float.parseFloat(valueFrom));
        filterRangeSlider.add(Float.parseFloat(valueTo));
        rangeSlider.setValueFrom(Float.parseFloat(valueFrom));
        rangeSlider.setValueTo(Float.parseFloat(valueTo));
        rangeSlider.setStepSize(Float.parseFloat(stepSize));
        rangeSlider.setValues(filterRangeSlider);

    }
    public interface filterCallback{
        void onCallback(ArrayList<String> types,String category,Float valueFrom,Float valueTo);
    }

}
