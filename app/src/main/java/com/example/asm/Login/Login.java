package com.example.asm.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asm.MainActivity;
import com.example.asm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    String TAG = "zzzzzzzzz";
    private EditText edEmail, edPass;
    private Button btnLogin;
    TextView btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        edEmail = findViewById(R.id.edemail_dn);
        edPass = findViewById(R.id.edPass_dn);
        btnLogin = findViewById(R.id.btnDangNhap);
        btnRegister = findViewById(R.id.btnDangKy);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String pass = edPass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Bạn chưa nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Login.this, "Bạn chưa nhập pass", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            firestore.collection("users").document(userId).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    String role = document.getString("role");
                                                    Log.d(TAG, "onComplete: role: "+role);

                                                    if (role != null) {
                                                        switch (role) {
                                                            case "admin":
                                                                Toast.makeText(Login.this, "Admin dang nhap", Toast.LENGTH_SHORT).show();
                                                                Intent i = new Intent(Login.this, MainActivity.class);
                                                                i.putExtra("role", role);
                                                                startActivity(i);
                                                                break;
                                                            case "chuyengia":
                                                                Toast.makeText(Login.this, "Chuyen gia", Toast.LENGTH_SHORT).show();
                                                                Intent b = new Intent(Login.this, MainActivity.class);
                                                                b.putExtra("role", role);
                                                                startActivity(b);
                                                                break;
                                                            case "user":
                                                                Toast.makeText(Login.this, "User dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                                                                Intent a = new Intent(Login.this, MainActivity.class);
                                                                a.putExtra("role", role);
                                                                startActivity(a);
                                                                break;
                                                            default:
                                                                Toast.makeText(Login.this, "Role không hợp lệ", Toast.LENGTH_SHORT).show();
                                                                break;
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(Login.this, "Không tìm thấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(Login.this, "Lỗi khi lấy dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Login.this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, register.class));
            }
        });
    }

}