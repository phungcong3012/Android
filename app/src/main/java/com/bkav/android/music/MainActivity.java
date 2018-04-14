package com.bkav.android.music;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.bkav.android.music.R.id.ImageAlbum;
import static com.bkav.android.music.R.id.NameAlbum;
import static com.bkav.android.music.R.id.NameSong;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private RelativeLayout relativeLayout;
    private ArrayList<String> paths; // lưu tất cả đường dẫn của các bài hát


    private ImageView mImageAlbum;
    private TextView mNameSong;
    private TextView mNameAlbum;
    private ImageView mPlayPause;
    private MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = (RelativeLayout) findViewById(R.id.recycleraAlbum);
        relativeLayout.setVisibility(View.GONE);
        init();
        initList();
        showSong();
        clickSong();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seach, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Seach:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mImageAlbum = (ImageView) findViewById(ImageAlbum);
        mNameSong = (TextView) findViewById(NameSong);
        mNameAlbum = (TextView) findViewById(NameAlbum);
        mPlayPause = (ImageView) findViewById(R.id.PlayPause);
    }

    private void initList() {
        paths = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        File file = new File(path);
        File[] files = file.listFiles(); // lấy tất cả các file trong thư mục Download
        for (int i = 0; i < files.length; i++) {
            // đọc tất cả các file có trong Download thêm vào list nhạc
            String s = files[i].getName();
            if (s.endsWith(".mp3")) {
                // kiểm tra xem có phải đuôi mp3 hay ko?
                // là tệp ảnh hay thư mục khác sẽ gây ra lỗi
                paths.add(files[i].getAbsolutePath());
            }
        }

    }


    private void showSong() {
        songAdapter = new SongAdapter(getApplicationContext(), paths);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(songAdapter);
    }

    private void clickSong() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String path = paths.get(position);

                try {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setDataSource(path);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MediaMetadataRetriever retriever = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
                    retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(paths.get(position));

                    String tenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                    mNameSong.setText(ten);
                    mNameAlbum.setText(tenAlbum);
                    mImageAlbum.setImageResource(R.drawable.chaukhaiphong);
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                Intent intent = new Intent(MainActivity.this, PlaySong_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("vitri", position);
                bundle.putStringArrayList("tenbai", paths);
                intent.putExtra("dulieu", bundle);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}
