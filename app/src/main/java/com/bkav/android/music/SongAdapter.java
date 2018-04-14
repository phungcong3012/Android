package com.bkav.android.music;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 4/11/2018.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{
    private Context context;
    private ArrayList<String> paths;

    public SongAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView NameSong;
        private TextView Stt;
        private TextView TimeSong;
        ImageView btMenu;
        public ViewHolder(View itemView) {
            super(itemView);
            NameSong = (TextView) itemView.findViewById(R.id.NameSong);
            Stt =(TextView) itemView.findViewById(R.id.stt);
            TimeSong =(TextView) itemView.findViewById(R.id.TimeSong);
            btMenu =(ImageView) itemView.findViewById(R.id.showMenu);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_layout_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        MediaMetadataRetriever retriever = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(paths.get(position));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

            String ten = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            int tgian = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            holder.Stt.setText(position+1+"");
            holder.NameSong.setText(ten);
            holder.TimeSong.setText(simpleDateFormat.format(tgian)+"");
        }
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }
}
