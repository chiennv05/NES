package com.example.asm.NavFarg;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.asm.Model.SharedViewModel;
import com.example.asm.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

public class FragBatDauAn extends Fragment {

    private ImageView btnbatdauchay;
    private ShapeableImageView headerImage;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_bat_dau_an, container, false);
        btnbatdauchay = view.findViewById(R.id.btnaddanuong);
        headerImage = view.findViewById(R.id.headerImage);

        // Khởi tạo ViewModel chia sẻ
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Đăng ký quan sát để cập nhật ảnh khi có thay đổi từ ViewModel
        sharedViewModel.getProfileImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                Glide.with(requireContext())
                        .load(uri)
                        .centerCrop()
                        .circleCrop()
                        .placeholder(R.drawable.imgtron)
                        .into(headerImage);
            }
        });

        btnbatdauchay.setOnClickListener(view1 -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragRenLuyenAnUong fraganuong = new FragRenLuyenAnUong();
            fragmentTransaction.replace(R.id.content_frame, fraganuong);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }

    public void updateHeader(Uri photoUri) {
        // Cập nhật ảnh trong NavigationView
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        if (navigationView != null) {
            View headerView = navigationView.getHeaderView(0);
            ShapeableImageView navHeaderImage = headerView.findViewById(R.id.headerImage);
            Glide.with(this)
                    .load(photoUri)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.imgtron)
                    .into(navHeaderImage);
        }

        // Cập nhật ảnh trong Fragment
        if (photoUri != null) {
            Glide.with(this)
                    .load(photoUri)
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.imgtron)
                    .into(headerImage);
        }
    }
}