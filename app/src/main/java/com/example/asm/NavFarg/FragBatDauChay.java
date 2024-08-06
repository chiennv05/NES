package com.example.asm.NavFarg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.asm.Model.SharedViewModel;
import com.example.asm.R;
import com.google.android.material.imageview.ShapeableImageView;


public class FragBatDauChay extends Fragment {
    private ImageView btnbatdauchay;
    private ShapeableImageView headerImage;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_batdauchay, container, false);
        btnbatdauchay = view.findViewById(R.id.btnbatdauchay);
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
            FragHoatDong fragHoatDong = new FragHoatDong();
            fragmentTransaction.replace(R.id.content_frame, fragHoatDong);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}
