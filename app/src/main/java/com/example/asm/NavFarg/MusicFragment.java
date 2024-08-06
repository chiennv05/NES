package com.example.asm.NavFarg;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm.R;
import com.example.asm.Amnhac.MusicAdapter;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MusicFragment extends Fragment {

    private ExoPlayer player;
    private EditText etLink;
    private Button btnAdd;
    private ImageView btnPlayPause, btnPrevious, btnNext, ivAlbumArt;
    private SeekBar seekBar;
    private TextView tvTime;
    private RecyclerView rvMusicList;
    private MusicAdapter musicAdapter;
    private List<String> musicNames = new ArrayList<>();
    private int currentTrackIndex = 0;
    private boolean isPlaying = false;
    private Handler handler = new Handler();

    private FirebaseFirestore firestore;
    private CollectionReference musicCollection;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        etLink = view.findViewById(R.id.etLink);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnPlayPause = view.findViewById(R.id.btnPlayPause);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        ivAlbumArt = view.findViewById(R.id.ivAlbumArt);
        seekBar = view.findViewById(R.id.seekBar);
        tvTime = view.findViewById(R.id.tvTime);
        rvMusicList = view.findViewById(R.id.rvMusicList);

        player = new ExoPlayer.Builder(getContext()).build();
        firestore = FirebaseFirestore.getInstance();
        musicCollection = firestore.collection("music");

        addSampleMusic();

        musicAdapter = new MusicAdapter(musicNames);
        rvMusicList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMusicList.setAdapter(musicAdapter);

        loadMusicFromFirebase();

        btnAdd.setOnClickListener(v -> {
            String link = etLink.getText().toString();
            if (!TextUtils.isEmpty(link)) {
                addMusicToFirebase(link);
                etLink.setText("");
            }
        });

        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                player.pause();
                btnPlayPause.setImageResource(R.drawable.dungnhac);
            } else {
                player.play();
                btnPlayPause.setImageResource(R.drawable.tieptucnhac);
            }
            isPlaying = !isPlaying;
        });

        btnNext.setOnClickListener(v -> {
            if (!musicNames.isEmpty()) {
                currentTrackIndex = (currentTrackIndex + 1) % musicNames.size();
                playMusic(currentTrackIndex);
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (!musicNames.isEmpty()) {
                currentTrackIndex = (currentTrackIndex - 1 + musicNames.size()) % musicNames.size();
                playMusic(currentTrackIndex);
            }
        });

        musicAdapter.setOnItemClickListener((position, name) -> {
            playMusic(position);
            updateAlbumArt(name);
        });

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY && isPlaying) {
                    int duration = (int) player.getDuration();
                    seekBar.setMax(duration);
                    handler.post(updateSeekBar);
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                btnPlayPause.setImageResource(isPlaying ? R.drawable.tieptucnhac : R.drawable.dungnhac);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    player.seekTo(progress);
                }
                tvTime.setText(formatTime(player.getDuration() - progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return view;
    }

    private void addSampleMusic() {
        String sampleMusic1 = "WEAN – shhhhhhh.. feat tlinh";
        String sampleMusic2 = "Lặng - Rhymastic";

        if (!musicNames.contains(sampleMusic1)) {
            musicNames.add(sampleMusic1);
        }
        if (!musicNames.contains(sampleMusic2)) {
            musicNames.add(sampleMusic2);
        }
    }

    private void loadMusicFromFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        musicCollection.whereEqualTo("userId", userId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            musicNames.clear();
            addSampleMusic();
            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                String link = document.getString("link");
                if (link != null && !musicNames.contains(link)) {
                    musicNames.add(link);
                }
            }
            musicAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> e.printStackTrace());
    }

    private void addMusicToFirebase(String link) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (!musicNames.contains(link)) {
            Map<String, Object> musicData = new HashMap<>();
            musicData.put("link", link);
            musicData.put("userId", userId);

            musicCollection.add(musicData)
                    .addOnSuccessListener(documentReference -> {
                        musicNames.add(link);
                        musicAdapter.notifyItemInserted(musicNames.size() - 1);
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Lỗi khi thêm nhạc: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Nhạc đã tồn tại trong danh sách.", Toast.LENGTH_SHORT).show();
        }
    }

    private void playMusic(int position) {
        currentTrackIndex = position;
        String name = musicNames.get(position);
        String link;

        int rawResourceId = getRawResourceIdFromName(name);
        if (rawResourceId != 0) {
            link = "android.resource://" + getActivity().getPackageName() + "/" + rawResourceId;
        } else {
            link = name; // Assume the link is a URL or other valid path
        }

        MediaItem mediaItem = new MediaItem.Builder().setUri(link).build();
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
        isPlaying = true;
        btnPlayPause.setImageResource(R.drawable.tieptucnhac);
    }

    private int getRawResourceIdFromName(String name) {
        switch (name) {
            case "WEAN – shhhhhhh.. feat tlinh":
                return R.raw.shhh;
            case "Lặng - Rhymastic":
                return R.raw.nhac2;
            default:
                return 0;
        }
    }

    private void updateAlbumArt(String name) {
        Log.d("MusicFragment", "Updating album art for: " + name);
        if (name.equals("WEAN – shhhhhhh.. feat tlinh")) {
            ivAlbumArt.setImageResource(R.drawable.nhacshhhhh);
        } else if (name.equals("Lặng - Rhymastic")) {
            ivAlbumArt.setImageResource(R.drawable.nhaclang);
        } else {
            ivAlbumArt.setImageResource(R.drawable.hinhanhthienkhithem);
        }
    }

    private String formatTime(long milliseconds) {
        int minutes = (int) (milliseconds / 60000);
        int seconds = (int) ((milliseconds % 60000) / 1000);
        return String.format("%02d:%02d", minutes, seconds);
    }

    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (player.isPlaying()) {
                seekBar.setProgress((int) player.getCurrentPosition());
                tvTime.setText(formatTime(player.getDuration() - player.getCurrentPosition()));
                handler.postDelayed(this, 1000);
            }
        }
    };
}
