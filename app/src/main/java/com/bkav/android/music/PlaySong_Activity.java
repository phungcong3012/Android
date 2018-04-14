package com.bkav.android.music;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.bkav.android.music.R.id.ImageAlbum;
import static com.bkav.android.music.R.id.seekBar;

public class PlaySong_Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageAlbum;
    private TextView NameSong;
    private TextView NameAlbum;
    private ImageView ShowMenu;
    private ImageView mLapLaiSong;
    private ImageView mXaoTronSong;
    private ImageView mLikeSong;
    private ImageView mBackSong;
    private ImageView mPlaySong;
    private ImageView mNextSong;
    private ImageView mDisLikeSong;
    private ImageView mImageSinger;
    private SeekBar mSeekBar;
    private TextView TimeSum;
    private TextView TimeFirst;
    private ArrayList<String > arrayList;
    private MediaPlayer mediaPlayer;
    private int mPositionSong;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song_);
        init();
        addData();
    }

    private void init() {
        mLapLaiSong = (ImageView) findViewById(R.id.LapLai);
        mXaoTronSong = (ImageView) findViewById(R.id.XaoTron);
        mLikeSong = (ImageView) findViewById(R.id.Like);
        mDisLikeSong = (ImageView) findViewById(R.id.DisLike);
        mBackSong = (ImageView) findViewById(R.id.Back);
        mPlaySong = (ImageView) findViewById(R.id.Play);
        mNextSong = (ImageView) findViewById(R.id.Next);
        mImageSinger = (ImageView) findViewById(R.id.AnhCaSi);
        mSeekBar =(SeekBar) findViewById(seekBar);
        TimeFirst =(TextView) findViewById(R.id.Thoigiandau);
        TimeSum = (TextView) findViewById(R.id.Thoigiancuoi);
        mImageAlbum = (ImageView) findViewById(ImageAlbum);
        NameSong = (TextView) findViewById(R.id.NameSong);
        NameAlbum = (TextView) findViewById(R.id.NameAlbum);
        ShowMenu = (ImageView) findViewById(R.id.showMenu);

        mPlaySong.setOnClickListener(this);
        mBackSong.setOnClickListener(this);
        mNextSong.setOnClickListener(this);



        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                if (TimeFirst.equals(simpleDateFormat.format(mediaPlayer.getCurrentPosition())+"")){
                    mPositionSong +=1;
                    playSongPosition();
                    mPlaySong.setImageResource(R.drawable.pause);
                    setSumTimeOneSong();
                    updateTime();
                }
            }
        });
    }
    private void addData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dulieu");
        mPositionSong = bundle.getInt("vitri");
        arrayList = bundle.getStringArrayList("tenbai");

        playSongPosition();
        setSumTimeOneSong();
        updateTime();
        updateSong();
    }

    private void playSongPosition(){
        uri = Uri.parse(arrayList.get(mPositionSong).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        updateSong();
    }

    private void setSumTimeOneSong(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        TimeSum.setText(simpleDateFormat.format(mediaPlayer.getDuration()) +"");

        // gán seekbar = mediaPlayer.getDuration()
        mSeekBar.setMax(mediaPlayer.getDuration());
    }

    private void updateTime(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                // mediaPlayer.getCurrentPosition() : vi tri hien tai cua bai hát
                TimeFirst.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                // cập nhập thời gian theo bài hát trên seekbar
                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        }, 100);
    }

    private void updateSong() {
        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(arrayList.get(mPositionSong));

            String tenAlbum = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            NameSong.setText(ten);
            NameAlbum.setText(tenAlbum);
            mImageAlbum.setImageResource(R.drawable.hoangson);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Play: {
                if (mediaPlayer.isPlaying()) {
                    // bài hát đang chạy -> pause -  đồi hình icon play
                    mediaPlayer.pause();
                    mPlaySong.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    mPlaySong.setImageResource(R.drawable.pause);
                }
                setSumTimeOneSong();
                updateTime();
                break;
            }
            case R.id.Back:{
                mPositionSong -= 1;
                if(mPositionSong <0){
                    mPositionSong = arrayList.size()-1;
                }
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                playSongPosition();
                mPlaySong.setImageResource(R.drawable.pause);
                setSumTimeOneSong();
                updateTime();
                break;
            }
            case R.id.Next:{
                mPositionSong +=1;
                if (mPositionSong > arrayList.size()-1){
                    mPositionSong =0;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                playSongPosition();
                mPlaySong.setImageResource(R.drawable.pause);
                setSumTimeOneSong();
                updateTime();
                break;
            }
        }
    }

}
