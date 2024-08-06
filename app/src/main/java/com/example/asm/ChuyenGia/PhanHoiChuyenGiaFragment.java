package com.example.asm.ChuyenGia;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PhanHoiChuyenGiaFragment extends Fragment {

    private FirebaseFirestore db;
    private TextView questionTextView;
    private EditText responseEditText;
    private Button sendResponseButton;
    private RecyclerView rvUserEmails;
    private EmailAdapter emailAdapter;
    private List<String> emailList;
    private NotificationHelper notificationHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phan_hoi_chuyen_gia, container, false);

        db = FirebaseFirestore.getInstance();
        questionTextView = view.findViewById(R.id.questionTextView);
        responseEditText = view.findViewById(R.id.responseEditText);
        sendResponseButton = view.findViewById(R.id.sendResponseButton);
        rvUserEmails = view.findViewById(R.id.rvUserEmails);
        notificationHelper = new NotificationHelper(getContext());
        emailList = new ArrayList<>();
        emailAdapter = new EmailAdapter(emailList, this::loadQuestionByEmail);

        rvUserEmails.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUserEmails.setAdapter(emailAdapter);

        loadEmails();
        sendResponseButton.setOnClickListener(v -> sendResponse());

        return view;
    }

    private void loadEmails() {
        db.collection("questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        emailList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = document.getString("email");
                            if (email != null && !emailList.contains(email)) {
                                emailList.add(email);
                            }
                        }
                        emailAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadQuestionByEmail(String email) {
        db.collection("questions")
                .whereEqualTo("email", email)
                .whereEqualTo("response", "")
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            questionTextView.setText(document.getString("question"));
                            sendResponseButton.setTag(document.getId());
                        }
                    } else {
                        questionTextView.setText("Không có câu hỏi nào");
                        sendResponseButton.setEnabled(false);
                    }
                });
    }

    private void sendResponse() {
        String response = responseEditText.getText().toString();
        if (!TextUtils.isEmpty(response)) {
            String documentId = (String) sendResponseButton.getTag();
            db.collection("questions").document(documentId)
                    .update("response", response)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Phản hồi đã được gửi", Toast.LENGTH_SHORT).show();
                        notificationHelper.showNotification("Thông báo", "Phản hồi đã được gửi");
                        loadEmails();
                        responseEditText.setText("");
                        questionTextView.setText("");
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Lỗi khi gửi phản hồi", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Phản hồi không được để trống", Toast.LENGTH_SHORT).show();
        }
    }

}
