    package com.example.asm.NavFarg;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;

    import com.bumptech.glide.Glide;
    import com.example.asm.Model.SharedViewModel;
    import com.example.asm.R;
    import com.google.android.material.imageview.ShapeableImageView;

    public class FragTrangChu extends Fragment {

        private ShapeableImageView headerImage;
        private SharedViewModel sharedViewModel;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frag_trangchu, container, false);
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
            return view;
        }
    }
