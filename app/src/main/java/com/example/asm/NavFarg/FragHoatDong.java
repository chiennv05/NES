package com.example.asm.NavFarg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.asm.HoatDongTab.BuocChanFrag;
import com.example.asm.HoatDongTab.HoatDongFragTab;
import com.example.asm.HoatDongTab.ThangFrag;
import com.example.asm.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class FragHoatDong extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 pager2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.layout_hoatdong,container,false);
            tabLayout = view.findViewById(R.id.tab_layout);
            pager2 = view.findViewById(R.id.view_pager);

        FragmentActivity activity = getActivity();
        if(activity != null) {
            HoatDongFragTab hoatDongFragTab = new HoatDongFragTab(activity);
            pager2.setAdapter(hoatDongFragTab);

            new TabLayoutMediator(tabLayout, pager2, (tab, position) -> {
                switch (position){
                    case 0:
                        tab.setText("Hôm nay");
                        break;
                    case 1:
                        tab.setText("Tháng này");
                }
            }).attach();

        }
        return view;

    }
}
