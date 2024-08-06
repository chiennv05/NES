package com.example.asm.NavFarg;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.ChuyenGia.NotificationHelper;
import com.example.asm.ChuyenGia.QuestionResponse;
import com.example.asm.ChuyenGia.ResponseAdapter;
import com.example.asm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragTuVan extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView rvResponses;
    private ResponseAdapter responseAdapter;
    private List<QuestionResponse> responseList;
    private NotificationHelper notificationHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tu_van, container, false);

        db = FirebaseFirestore.getInstance();

        Button btnHealth = view.findViewById(R.id.btnHealth);
        Button btnNutrition = view.findViewById(R.id.btnNutrition);
        Button btnExercise = view.findViewById(R.id.btnExercise);
        Button btnPsychology = view.findViewById(R.id.btnPsychology);
        Button btnFinance = view.findViewById(R.id.btnFinance);
        notificationHelper = new NotificationHelper(getContext());

        btnHealth.setOnClickListener(v -> showQuestionDialog("Tư vấn sức khỏe"));
        btnNutrition.setOnClickListener(v -> showQuestionDialog("Tư vấn dinh dưỡng"));
        btnExercise.setOnClickListener(v -> showQuestionDialog("Tư vấn thể dục"));
        btnPsychology.setOnClickListener(v -> showQuestionDialog("Tư vấn tâm lý"));
        btnFinance.setOnClickListener(v -> showQuestionDialog("Tư vấn tài chính"));

        rvResponses = view.findViewById(R.id.rvResponses);
        rvResponses.setLayoutManager(new LinearLayoutManager(getContext()));

        responseList = new ArrayList<>();
        responseAdapter = new ResponseAdapter(responseList);
        rvResponses.setAdapter(responseAdapter);

        loadResponses();

        // Register the broadcast receiver
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(refreshReceiver, new IntentFilter("REFRESH_RESPONSES"));

        return view;
    }

    // BroadcastReceiver to handle refresh requests
    private final BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadResponses();
        }
    };

    // Unregister the broadcast receiver when the fragment is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(refreshReceiver);
    }

    private void showQuestionDialog(String topic) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Đặt câu hỏi về " + topic);

        final EditText input = new EditText(getContext());
        builder.setView(input);

        builder.setPositiveButton("Gửi", (dialog, which) -> {
            String question = input.getText().toString();
            if (!TextUtils.isEmpty(question)) {
                sendQuestionToFirestore(topic, question);
            } else {
                Toast.makeText(getContext(), "Câu hỏi không được để trống", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void sendQuestionToFirestore(String topic, String question) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("userId", userId);
            questionData.put("email", user.getEmail());
            questionData.put("topic", topic);
            questionData.put("question", question);
            questionData.put("response", "");

            db.collection("questions")
                    .add(questionData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Câu hỏi đã được gửi", Toast.LENGTH_SHORT).show();
                        notificationHelper.showNotification("Thông báo", "Câu hỏi đã được gửi");
                        // Broadcast to refresh responses
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("REFRESH_RESPONSES"));
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getContext(), "Lỗi khi gửi câu hỏi", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadResponses() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("questions")
                    .whereEqualTo("userId", userId)
                    .whereNotEqualTo("response", "")
                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                        if (e != null) {
                            return;
                        }
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            responseList.clear();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                QuestionResponse response = document.toObject(QuestionResponse.class);
                                responseList.add(response);
                            }
                            responseAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}
