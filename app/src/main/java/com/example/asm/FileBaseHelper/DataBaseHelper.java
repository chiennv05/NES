package com.example.asm.FileBaseHelper;

import com.google.firebase.firestore.FirebaseFirestore;

public class DataBaseHelper {
    private FirebaseFirestore firestore;

    public DataBaseHelper() {
        firestore = FirebaseFirestore.getInstance();
    }

    public FirebaseFirestore getDatabase() {
        return firestore;
    }

}
