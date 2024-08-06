    package com.example.asm;

    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.activity.result.ActivityResult;
    import androidx.activity.result.ActivityResultCallback;
    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.NotificationCompat;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;
    import androidx.drawerlayout.widget.DrawerLayout;
    import androidx.fragment.app.FragmentManager;

    import com.bumptech.glide.Glide;
    import com.example.asm.Adapter.NotificationUtil;
    import com.example.asm.ChuyenGia.ChuyenGiaFragment;
    import com.example.asm.ChuyenGia.PhanHoiChuyenGiaFragment;
    import com.example.asm.Login.Login;
    import com.example.asm.NavFarg.BatDauNguFrag;
    import com.example.asm.NavFarg.FargBieton;
    import com.example.asm.NavFarg.FragAccount;
    import com.example.asm.NavFarg.FragBatDauAn;
    import com.example.asm.NavFarg.FragBatDauChay;
    import com.example.asm.NavFarg.FragBatDauThien;
    import com.example.asm.NavFarg.FragBatdauBietOn;
    import com.example.asm.NavFarg.FragRenLuyenAnUong;
    import com.example.asm.NavFarg.FragSleep;
    import com.example.asm.NavFarg.FragTrangChu;
    import com.example.asm.NavFarg.FragTuVan;
    import com.example.asm.NavFarg.Fragbatdautuvan;
    import com.example.asm.NavFarg.FriendFragment;
    import com.example.asm.NavFarg.ThienAmNhac;
    import com.google.android.material.imageview.ShapeableImageView;
    import com.google.android.material.navigation.NavigationView;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.FirebaseFirestore;

    import java.io.IOException;
    import java.util.Calendar;

    public class MainActivity extends AppCompatActivity {
        private SharedPreferences sharedPreferences;
        FragmentManager fm;
        FragBatdauBietOn fargBieton;
        NavigationView nav;
        FragBatDauAn fragRenLuyenAnUong;
        FragBatDauChay fragHoatDong;
        FragBatDauThien fragThienNhac;
        BatDauNguFrag fragSleep;
        FragTrangChu fragTrangChu;
        DrawerLayout drawerLayout;
        FriendFragment friendFragment;
        Fragbatdautuvan fragTuVan;
        FragAccount fragAccount;
        ChuyenGiaFragment chuyenGiaFragment;
        PhanHoiChuyenGiaFragment phanHoiChuyenGiaFragment;

        final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                if(o.getResultCode() == RESULT_OK) {
                    Intent intent = o.getData();
                    if(intent == null) {
                        return;
                    }
                    Uri uri = intent.getData();
                    fragAccount.setImageUri(uri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        fragAccount.setBitMapImage(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            drawerLayout = findViewById(R.id.main);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            fm = getSupportFragmentManager();
            fargBieton = new FragBatdauBietOn();
            fragRenLuyenAnUong = new FragBatDauAn();
            fragHoatDong = new FragBatDauChay();
            fragThienNhac = new FragBatDauThien();
            fragSleep = new BatDauNguFrag();
            fragTrangChu = new FragTrangChu();
            friendFragment = new FriendFragment();
            fragTuVan = new Fragbatdautuvan();
            fragAccount = new FragAccount();
            chuyenGiaFragment = new ChuyenGiaFragment();
            phanHoiChuyenGiaFragment = new PhanHoiChuyenGiaFragment();
            nav = findViewById(R.id.nav_view);
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            setHeaderInfo(); // Add this line to set header info on app launch

            if (savedInstanceState == null) {
                fm.beginTransaction().replace(R.id.content_frame, fragTrangChu).commit();
            }

            Toast.makeText(this, "Đã đến lúc viết lời biết ơn", Toast.LENGTH_SHORT).show();

            nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    if (id == R.id.nav_loibieton) {
                        fm.beginTransaction().replace(R.id.content_frame, fargBieton).addToBackStack(null).commit();
                    } else if (id == R.id.nav_suckhoeanuong) {
                        fm.beginTransaction().replace(R.id.content_frame, fragRenLuyenAnUong).addToBackStack(null).commit();
                    } else if (id == R.id.nav_hoatdong) {
                        fm.beginTransaction().replace(R.id.content_frame, fragHoatDong).addToBackStack(null).commit();
                    } else if (id == R.id.nav_thien) {
                        fm.beginTransaction().replace(R.id.content_frame, fragThienNhac).addToBackStack(null).commit();
                    } else if (id == R.id.nav_giacngu) {
                        fm.beginTransaction().replace(R.id.content_frame, fragSleep).addToBackStack(null).commit();
                    }
                    else if (id == R.id.nav_trangchu) {
                        fm.beginTransaction().replace(R.id.content_frame, fragTrangChu).commit();
                    }
//                    else if (id == R.id.nav_ketban) {
//                        fm.beginTransaction().replace(R.id.content_frame, friendFragment).addToBackStack(null).commit();
                   // }
                    else if (id == R.id.nav_tuvan) {
                        fm.beginTransaction().replace(R.id.content_frame, fragTuVan).addToBackStack(null).commit();
                    } else if (id == R.id.nav_taikhoan) {
                        fm.beginTransaction().replace(R.id.content_frame, fragAccount).addToBackStack(null).commit();
                    } else if (id == R.id.nav_dangxuat) {
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }else if (id == R.id.nav_themCG) {
                        fm.beginTransaction().replace(R.id.content_frame, chuyenGiaFragment).addToBackStack(null).commit();
                    }else if (id == R.id.nav_phanhoi) {
                        fm.beginTransaction().replace(R.id.content_frame, phanHoiChuyenGiaFragment).addToBackStack(null).commit();
                    }

                    drawerLayout.closeDrawer(nav);
                    return true;
                }
            });

            ImageView imgMenu = findViewById(R.id.imgmenu);
            imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(nav);
                }
            });

            ImageView bieton = findViewById(R.id.imgbieton);
            bieton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.closeDrawer(nav);
                    fm.beginTransaction().replace(R.id.content_frame, fargBieton).commit();
                }
            });

            ImageView chaybo = findViewById(R.id.imgchay);
            chaybo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.closeDrawer(nav);
                    fm.beginTransaction().replace(R.id.content_frame, fragHoatDong).commit();
                }
            });

            ImageView chiso = findViewById(R.id.imgchiso);
            chiso.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.closeDrawer(nav);
                    fm.beginTransaction().replace(R.id.content_frame, fragRenLuyenAnUong).commit();
                }
            });
            //thay đổi bên trong menu
            //admin
                Intent i = getIntent();
                String role = i.getStringExtra("role");
                if(role.equals("admin")){
                    showAdminMenu();
                } else if(role.equals("chuyengia")){
                showChuyenGiaMenu();
                } else if(role.equals("user")){
                    showNguoiDungMenu();
                }

        }
        //thay đổi menu cho admin
        private void showAdminMenu() {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            MenuItem navThemCG = menu.findItem(R.id.nav_themCG);
            navThemCG.setVisible(true);
        }

        //thay đổi admin cho chuyên gia
        private void showChuyenGiaMenu() {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            MenuItem nav_phanhoi = menu.findItem(R.id.nav_phanhoi);
            nav_phanhoi.setVisible(true);
        }
        private void showNguoiDungMenu() {
            NavigationView navigationView = findViewById(R.id.nav_view);
            Menu menu = navigationView.getMenu();
            MenuItem nav_nguoidung = menu.findItem(R.id.nav_tuvan);
            nav_nguoidung.setVisible(true);
        }

        //update anh
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == FragAccount.PICK_IMAGE_REQUEST) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Toast.makeText(this, "Vui lòng cấp quyền truy cập vào bộ nhớ để chọn ảnh.", Toast.LENGTH_SHORT).show();
                }
            }
        }


        private void setHeaderInfo() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                updateHeader(user.getDisplayName(), user.getEmail(), user.getPhotoUrl());
            }
        }

        public void updateHeader(String name, String email, Uri photoUri) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            ShapeableImageView headerImage = headerView.findViewById(R.id.headerImage);
            TextView headerText = headerView.findViewById(R.id.headerText);
            TextView headerEmail = headerView.findViewById(R.id.txEmail);

            headerText.setText(name);
            headerEmail.setText(email);

            Glide.with(this)
                    .load(photoUri)
                    .centerCrop()
                    .circleCrop() // Chuyển ảnh thành hình tròn
                    .placeholder(R.drawable.imgtron) // Ảnh placeholder
                    .into(headerImage);
        }

        public  void  openGallery() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
        }

        //giờ ngủ
        @Override
        protected void onResume() {
            super.onResume();
            checkSleepDataAfterLogin();
        }

        private void checkSleepDataAfterLogin() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy ID người dùng hiện tại

            db.collection("users").document(userId).collection("sleepData")
                    .document("currentSleep") // Có thể đổi tên document tùy ý
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String sleepTimeStr = documentSnapshot.getString("sleep_time");
                            String wakeTimeStr = documentSnapshot.getString("wake_time");

                            if (sleepTimeStr != null && wakeTimeStr != null) {
                                checkSleepSufficiency(sleepTimeStr, wakeTimeStr);
                            } else {
                                Log.d("Login", "Dữ liệu giấc ngủ không tồn tại");
                            }
                        } else {
                            Log.d("Login", "Tài liệu không tồn tại");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Login", "Lỗi tải dữ liệu giấc ngủ", e));
        }

        private void checkSleepSufficiency(String sleepTimeStr, String wakeTimeStr) {
            String[] sleepTimeParts = sleepTimeStr.split(":");
            String[] wakeTimeParts = wakeTimeStr.split(":");

            int sleepHour = Integer.parseInt(sleepTimeParts[0]);
            int sleepMinute = Integer.parseInt(sleepTimeParts[1]);
            int wakeHour = Integer.parseInt(wakeTimeParts[0]);
            int wakeMinute = Integer.parseInt(wakeTimeParts[1]);

            Calendar sleepTime = Calendar.getInstance();
            sleepTime.set(Calendar.HOUR_OF_DAY, sleepHour);
            sleepTime.set(Calendar.MINUTE, sleepMinute);

            Calendar wakeTime = Calendar.getInstance();
            wakeTime.set(Calendar.HOUR_OF_DAY, wakeHour);
            wakeTime.set(Calendar.MINUTE, wakeMinute);

            // Nếu thời gian thức trước thời gian ngủ, thêm một ngày cho thời gian thức
            if (wakeTime.before(sleepTime)) {
                wakeTime.add(Calendar.DATE, 1);
            }

            long sleepDurationMillis = wakeTime.getTimeInMillis() - sleepTime.getTimeInMillis();
            int sleepDurationHours = (int) (sleepDurationMillis / (1000 * 60 * 60));
            int sleepDurationMinutes = (int) ((sleepDurationMillis / (1000 * 60)) % 60);

            float totalSleepHours = sleepDurationHours + (sleepDurationMinutes / 60.0f);

            // Thay đổi điều kiện kiểm tra đủ giấc để phù hợp hơn
            if (totalSleepHours >= 6.5 && totalSleepHours <= 8) {
                sendNotification("Thông báo giấc ngủ", "Bạn đã ngủ đủ giấc. Hãy duy trì thói quen này.");

            } else {
                sendNotification("Thông báo giấc ngủ", "Bạn chưa ngủ đủ giấc. Hãy điều chỉnh lại thời gian ngủ của bạn.");
            }
        }

        private void sendNotification(String title, String message) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        NotificationUtil.CHANNEL_ID,
                        "Sleep Notifications",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription("Channel for sleep notifications");
                notificationManager.createNotificationChannel(channel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NotificationUtil.CHANNEL_ID)
                    .setSmallIcon(R.drawable.calo) // Thay đổi hình ảnh biểu tượng theo nhu cầu của bạn
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            notificationManager.notify(1, builder.build());
        }

    }
