package com.example.asm.NavFarg;

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

import com.example.asm.Adapter.BMIRecordAdapter;
import com.example.asm.Model.BMIRecord;
import com.example.asm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragRenLuyenAnUong extends Fragment {
    private EditText edtcannang, edtchieucao;
    private Button btntinh;
    private TextView txtBMI, txtloaibeo, txtnenan;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerViewBMI;
    private BMIRecordAdapter adapter;
    private List<BMIRecord> bmiRecordList;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_anuong, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtcannang = view.findViewById(R.id.edt_cannang);
        edtchieucao = view.findViewById(R.id.edt_chieucao);
        btntinh = view.findViewById(R.id.btn_tinh);
        txtBMI = view.findViewById(R.id.txtBMI);
        txtloaibeo = view.findViewById(R.id.txtloaibeo);
        txtnenan = view.findViewById(R.id.txtnenan);
        recyclerViewBMI = view.findViewById(R.id.recyclerViewBMI);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        bmiRecordList = new ArrayList<>();
        adapter = new BMIRecordAdapter(bmiRecordList);
        recyclerViewBMI.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewBMI.setAdapter(adapter);

        // Load data when fragment is created
        loadBMIRecords();

        btntinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tinhchisoBMI();
            }
        });
    }

    private void tinhchisoBMI() {
        String weightStr = edtcannang.getText().toString();
        String heightStr = edtchieucao.getText().toString();

        if (TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(heightStr)) {
            Toast.makeText(getActivity(), "Vui lòng nhập đủ cân nặng và chiều cao", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float weight = Float.parseFloat(weightStr);
            float height = Float.parseFloat(heightStr);

            if (weight <= 0 || height <= 0) {
                Toast.makeText(getActivity(), "Cân nặng và chiều cao phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weight > 100 || height > 200) {
                Toast.makeText(getActivity(), "Cân nặng không được vượt quá 100 kg và chiều cao không được vượt quá 200 cm", Toast.LENGTH_SHORT).show();
                return;
            }

            height = height / 100; // chuyển sang mét
            float bmi = weight / (height * height);
            txtBMI.setText(String.format("BMI: %.2f", bmi));

            String bmiCategory;
            String chedoan;

            if (bmi < 16) {
                bmiCategory = "Gầy độ III";
                chedoan = "Tăng lượng calo với các thực phẩm giàu dinh dưỡng, ăn nhiều bữa trong ngày, bổ sung các loại thực phẩm như thịt, cá, trứng, sữa, và các loại hạt.";
            } else if (bmi >= 16 && bmi < 17) {
                bmiCategory = "Gầy độ II";
                chedoan = "Tăng lượng calo với các thực phẩm giàu dinh dưỡng, ăn nhiều bữa trong ngày, bổ sung các loại thực phẩm như thịt, cá, trứng, sữa, và các loại hạt.";
            } else if (bmi >= 17 && bmi < 18.5) {
                bmiCategory = "Gầy độ I";
                chedoan = "Tăng lượng calo với các thực phẩm giàu dinh dưỡng, ăn nhiều bữa trong ngày, bổ sung các loại thực phẩm như thịt, cá, trứng, sữa, và các loại hạt.";
            } else if (bmi >= 18.5 && bmi < 25) {
                bmiCategory = "Bình thường";
                chedoan = "Duy trì chế độ ăn cân bằng, bao gồm nhiều trái cây, rau, ngũ cốc nguyên hạt, protein nạc và sữa.";
            } else if (bmi >= 25 && bmi < 30) {
                bmiCategory = "Thừa cân";
                chedoan = "Cân nhắc chế độ ăn giàu trái cây, rau và protein nạc, giảm lượng thực phẩm giàu chất béo và đường, tăng cường hoạt động thể chất.";
            } else if (bmi >= 30 && bmi < 35) {
                bmiCategory = "Béo phì độ I";
                chedoan = "Tham khảo ý kiến của chuyên gia y tế, cân nhắc chế độ ăn giảm calo, tăng cường hoạt động thể chất, theo dõi sức khỏe định kỳ.";
            } else if (bmi >= 35 && bmi < 40) {
                bmiCategory = "Béo phì độ II";
                chedoan = "Tham khảo ý kiến của chuyên gia y tế, cân nhắc chế độ ăn giảm calo, tăng cường hoạt động thể chất, theo dõi sức khỏe định kỳ.";
            } else {
                bmiCategory = "Béo phì độ III";
                chedoan = "Tham khảo ý kiến của chuyên gia y tế, cân nhắc chế độ ăn giảm calo, tăng cường hoạt động thể chất, theo dõi sức khỏe định kỳ.";
            }

            txtloaibeo.setText("Loại béo: " + bmiCategory);
            txtnenan.setText("Bạn nên ăn: " + chedoan);

            saveToFirestore(weight, height, bmi, bmiCategory, chedoan);

        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Vui lòng nhập đúng định dạng số", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToFirestore(float weight, float height, float bmi, String bmiCategory, String chedoan) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            BMIRecord record = new BMIRecord(weight, height, bmi, bmiCategory, chedoan, uid);
            firestore.collection("BMIRecords").add(record)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getActivity(), "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
                        loadBMIRecords(); // Load updated records
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Lỗi lưu dữ liệu", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void loadBMIRecords() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            firestore.collection("BMIRecords")
                    .whereEqualTo("uid", uid) // Lọc theo UID
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                bmiRecordList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    BMIRecord record = document.toObject(BMIRecord.class);
                                    bmiRecordList.add(record);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getActivity(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
