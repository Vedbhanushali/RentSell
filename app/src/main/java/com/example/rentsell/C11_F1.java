package com.example.rentsell;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.util.ArrayList;

public class C11_F1 extends Fragment {
    Connection connect;
    TextView filterBtn,sortBtn,searchView;
    C11F1_C11F2_A f1_adapterVertical,f1_adapterHorizontal;
    ArrayList<C11F1_C11F2_M> c11_f1_horizontalPropertyList=new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f1_verticalPropertyList=new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f1_horizontalPropertyList2=new ArrayList<>();
    ArrayList<C11F1_C11F2_M> c11_f1_verticalPropertyList2=new ArrayList<>();

    SharedPreferences sortSharedPreferences;
    TextView recommendForU,popularForU;

    RecyclerView f1_recyclerViewHorizontal,f1_recyclerViewVertical;
    LinearLayoutManager f1_layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

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
        return inflater.inflate(R.layout.fragment_c11_f1, container, false);
    }
}