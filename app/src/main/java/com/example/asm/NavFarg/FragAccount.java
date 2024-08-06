package com.example.asm.NavFarg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.asm.MainActivity;
import com.example.asm.Model.SharedViewModel;
import com.example.asm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FragAccount extends Fragment {
    private ImageView imgProfile;
    private EditText edName, edemail;
    private Button btnSave;
    private Uri imageUri;
    private SharedViewModel sharedViewModel;
    public static final int PICK_IMAGE_REQUEST = 10;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        imgProfile = view.findViewById(R.id.imgavt);
        edName = view.findViewById(R.id.etFullName);
        edemail = view.findViewById(R.id.etEmail_chu);
        btnSave = view.findViewById(R.id.btnSave);

        // Khởi tạo ViewModel chia sẻ
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setUserInformation();
        initListeners();

        return view;
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        edName.setText(user.getDisplayName());
        edemail.setText(user.getEmail());
        Glide.with(this)
                .load(user.getPhotoUrl())
                .centerCrop()
                .circleCrop()
                .into(imgProfile);
    }

    private void initListeners() {
        imgProfile.setOnClickListener(v -> onClickImageChooser());
        btnSave.setOnClickListener(v -> onClickSaveProfile());
    }

    private void onClickImageChooser() {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null) {
            return;
        }
        activity.openGallery();
    }

    public void setBitMapImage(Bitmap bitmap) {
        imgProfile.setImageBitmap(bitmap);
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        // Cập nhật ảnh thông qua ViewModel chia sẻ
        sharedViewModel.setProfileImageUri(imageUri);
    }

    private void onClickSaveProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }

        String newName = edName.getText().toString().trim();
        String newEmail = edemail.getText().toString().trim();
        if (!newName.isEmpty() && !newEmail.isEmpty()) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .setPhotoUri(imageUri)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updateEmail(newEmail).addOnCompleteListener(emailUpdateTask -> {
                                if (emailUpdateTask.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
                                    setUserInformation();
                                    updateHeaderInfo(newName, newEmail, imageUri);
                                } else {
                                    Toast.makeText(getActivity(), "Cập nhật email thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Cập nhật hồ sơ thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Vui lòng nhập tên và email hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHeaderInfo(String name, String email, Uri photoUri) {
        MainActivity activity = (MainActivity) getActivity();
        FragBatDauAn fragBatDauAn = new FragBatDauAn();
        if (activity != null) {
            activity.updateHeader(name, email, photoUri);
        } else if(fragBatDauAn != null) {
            fragBatDauAn.updateHeader(photoUri);
        }
    }
}
