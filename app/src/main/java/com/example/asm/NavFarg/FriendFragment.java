package com.example.asm.NavFarg;

import android.os.Bundle;
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
import com.example.asm.test.FriendAdapter;
import com.example.asm.test.RequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendFragment extends Fragment {

    private EditText edtFriendEmail;
    private Button btnAddFriend;
    private RecyclerView rvFriendList;
    private RecyclerView rvRequests;
    private TextView tvFriendCount;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private FriendAdapter friendAdapter;
    private List<String> friendList;

    private RequestAdapter requestAdapter;
    private List<String> requestList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        edtFriendEmail = view.findViewById(R.id.edtFriendEmail);
        btnAddFriend = view.findViewById(R.id.btnAddFriend);
        rvFriendList = view.findViewById(R.id.rvFriendList);
        rvRequests = view.findViewById(R.id.rvRequests);
        tvFriendCount = view.findViewById(R.id.tvFriendCount);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(getActivity(), friendList);
        rvFriendList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFriendList.setAdapter(friendAdapter);

        requestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(getActivity(), requestList, this::acceptFriendRequest);
        rvRequests.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRequests.setAdapter(requestAdapter);

        btnAddFriend.setOnClickListener(view1 -> addFriend());
        getFriendsList();
        getFriendRequests();

        return view;
    }

    private void addFriend() {
        String email = edtFriendEmail.getText().toString().trim();
        if (!email.isEmpty()) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                Map<String, Object> request = new HashMap<>();
                request.put("email", email);
                request.put("status", "pending");

                db.collection("friendRequests").document(userId).collection("requests")
                        .document(email).set(request)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getActivity(), "Yêu cầu kết bạn đã được gửi", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Lỗi khi gửi yêu cầu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        } else {
            Toast.makeText(getActivity(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFriendsList() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            db.collection("friends").document(userId).collection("list")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            friendList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String friendEmail = document.getString("email");
                                friendList.add(friendEmail);
                            }
                            friendAdapter.notifyDataSetChanged();
                            tvFriendCount.setText("Số lượng bạn bè: " + friendList.size());
                        } else {
                            Toast.makeText(getActivity(), "Lỗi khi tải danh sách bạn bè", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void getFriendRequests() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            db.collection("friendRequests").document(userId).collection("requests")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            requestList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String friendEmail = document.getString("email");
                                requestList.add(friendEmail);
                            }
                            requestAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Lỗi khi tải yêu cầu kết bạn", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void acceptFriendRequest(String friendEmail) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            Map<String, Object> requestUpdate = new HashMap<>();
            requestUpdate.put("status", "accepted");

            db.collection("friendRequests").document(userId).collection("requests")
                    .document(friendEmail).update(requestUpdate)
                    .addOnSuccessListener(aVoid -> {
                        Map<String, Object> friendData = new HashMap<>();
                        friendData.put("email", friendEmail);

                        db.collection("friends").document(userId).collection("list")
                                .document(friendEmail).set(friendData, SetOptions.merge())
                                .addOnSuccessListener(aVoid1 -> {
                                    db.collection("friends").document(friendEmail).collection("list")
                                            .document(userId).set(friendData, SetOptions.merge())
                                            .addOnSuccessListener(aVoid2 -> {
                                                Toast.makeText(getActivity(), "Đã chấp nhận yêu cầu kết bạn", Toast.LENGTH_SHORT).show();
                                                getFriendsList();
                                                getFriendRequests();
                                            });
                                });
                    });
        }
    }
}
