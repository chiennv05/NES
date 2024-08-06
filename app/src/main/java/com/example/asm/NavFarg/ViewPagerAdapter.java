package com.example.asm.NavFarg;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MusicFragment();
            case 1:
                return new MeditationFragment();
            default:
                return new MusicFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
