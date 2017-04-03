package com.movies.slp.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.movies.slp.popularmovies.utilities.MovieContants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoActivity extends AppCompatActivity implements MovieContants {

    private static final String VIDEOS_TITLE = "Videos";
    private TextView textView;
    private static int movieId;
    private static final int VIDEO_LOADER  = 10;
    private List<VideoDetail> videos ;
    @Bind(R.id.video_list)
    ListView videoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        this.setTitle(VIDEOS_TITLE);
        ButterKnife.bind(this);
        videos = (ArrayList<VideoDetail>) getIntent().getSerializableExtra(VIDEOS);
        displayVideos();

    }

    private void displayVideos() {
        List<String> videoDescriptions = new ArrayList<>();
        for(VideoDetail video:videos)
            videoDescriptions.add(video.getVideoDescription());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.video_list,R.id.video_description,videoDescriptions);
        videoList.setAdapter(adapter);
        videoList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoLink = videos.get(position).getVideoLink();
                Uri uri = Uri.parse(videoLink);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                Log.i("Video link",videoLink);
            }
        });
    }

}
