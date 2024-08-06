package com.example.asm.Model;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Uri> profileImageUri = new MutableLiveData<>();

    public void setProfileImageUri(Uri uri) {
        profileImageUri.setValue(uri);
    }

    public LiveData<Uri> getProfileImageUri() {
        return profileImageUri;
    }
}

