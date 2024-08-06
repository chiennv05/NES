package com.example.asm.HoatDongTab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class HoatDongFragTab extends FragmentStateAdapter {

    public HoatDongFragTab(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BuocChanFrag();
            case 1:
                return new ThangFrag();
            default:
                return new BuocChanFrag();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
