package com.example.rentsell;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class C11_Sort extends BottomSheetDialogFragment {

    private sortCallback mBListener;
    public static final String sortPrefernce="sortPref";
    public static final String sortId="";
    int CheckedId;

    public C11_Sort(sortCallback callback,int checkedId){
        mBListener=callback;
        CheckedId=checkedId;
    }
    public static SharedPreferences sortSharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.c11_sort_layout,
                container, false);


        //if user already login then open C11 class
        sortSharedPreferences = getActivity().getSharedPreferences(sortPrefernce, Context.MODE_PRIVATE);
        RadioGroup radioGroup=(RadioGroup) v.findViewById(R.id.c11_sort_radioGroup);
        ImageButton cancelBtn=(ImageButton)v.findViewById(R.id.c11_sort_cancelBtn);
        radioGroup.check(CheckedId);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Toast.makeText(v.getContext(), String.valueOf(group.getCheckedRadioButtonId()), Toast.LENGTH_SHORT).show();
                Log.e("item", String.valueOf(group.getCheckedRadioButtonId()));
                mBListener.onCallback(group.getCheckedRadioButtonId());
                SharedPreferences.Editor editor= sortSharedPreferences.edit();
                editor.putInt(sortId, checkedId);
                editor.commit();
                radioGroup.check(checkedId);
                dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }


    public interface sortCallback{
        void onCallback(int checkedId);
    }

}
