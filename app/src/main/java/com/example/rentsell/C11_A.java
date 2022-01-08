package com.example.rentsell;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rentsell.C11_F1;
import com.example.rentsell.C11_F2;

public class C11_A extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public C11_A(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                C11_F1 c11_f1=new C11_F1();
                return c11_f1;
            case 1:
                C11_F2 c11_f2=new C11_F2();
                return c11_f2;
            default:
                C11_F1 c11F1=new C11_F1();
                return c11F1;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

}
