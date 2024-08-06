package com.example.asm.NavFarg;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.Adapter.MeditationTimesAdapter;
import com.example.asm.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

public class MeditationFragment extends Fragment {

    private TextView tvMeditationTime;
    private Button btnStopMeditation;
    private Handler meditationHandler;
    private Runnable meditationRunnable;
    private long meditationStartTime;
    private boolean isMeditating = false;
    private ImageView btnStartMeditation ;

    private RecyclerView rvMeditationTimes;
    private MeditationTimesAdapter meditationAdapter;
    private List<Long> meditationTimes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meditation, container, false);

        tvMeditationTime = view.findViewById(R.id.tvMeditationTime);
        btnStartMeditation = view.findViewById(R.id.btnStartMeditation);
        btnStopMeditation = view.findViewById(R.id.btnStopMeditation);

        rvMeditationTimes = view.findViewById(R.id.rvMeditationTimes);
        meditationTimes = new ArrayList<>();
        meditationAdapter = new MeditationTimesAdapter(meditationTimes);
        rvMeditationTimes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMeditationTimes.setAdapter(meditationAdapter);

        meditationHandler = new Handler(Looper.getMainLooper());

        btnStartMeditation.setOnClickListener(v -> startMeditation());

        btnStopMeditation.setOnClickListener(v -> stopMeditation());

        // Load existing meditation times from Firebase for the current user
        loadMeditationTimes();

        return view;
    }

    private void startMeditation() {
        if (!isMeditating) {
            meditationStartTime = System.currentTimeMillis();
            meditationRunnable = new Runnable() {
                @Override
                public void run() {
                    long elapsedTime = System.currentTimeMillis() - meditationStartTime;
                    int hours = (int) (elapsedTime / 3600000);
                    int minutes = (int) (elapsedTime % 3600000) / 60000;
                    int seconds = (int) (elapsedTime % 60000) / 1000;
                    tvMeditationTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds));
                    meditationHandler.postDelayed(this, 1000);
                }
            };
            meditationHandler.post(meditationRunnable);
            isMeditating = true;
        }
    }

    private void stopMeditation() {
        if (isMeditating) {
            meditationHandler.removeCallbacks(meditationRunnable);
            long meditationEndTime = System.currentTimeMillis();
            long meditationDuration = meditationEndTime - meditationStartTime;

            // Save meditation time to Firebase with userId
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Map<String, Object> meditationRecord = new HashMap<>();
            meditationRecord.put("userId", userId);
            meditationRecord.put("startTime", meditationStartTime);
            meditationRecord.put("duration", meditationDuration);
            db.collection("meditationTimes")
                    .add(meditationRecord)
                    .addOnSuccessListener(documentReference -> {
                        // Add to local list and update RecyclerView
                        meditationTimes.add(meditationDuration);
                        meditationAdapter.notifyItemInserted(meditationTimes.size() - 1);
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });

            isMeditating = false;
        }
    }

    private void loadMeditationTimes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("meditationTimes")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    meditationTimes.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Long duration = document.getLong("duration");
                        if (duration != null) {
                            meditationTimes.add(duration);
                        }
                    }
                    meditationAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}
