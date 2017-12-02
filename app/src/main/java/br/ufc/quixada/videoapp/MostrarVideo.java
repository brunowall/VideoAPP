package br.ufc.quixada.videoapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

/**
 * Created by darkbyte on 01/12/17.
 */

public class MostrarVideo extends Activity{
    private  VideoRepository videoRepository;
    private Video video;
    private VideoView videoView;
    private TextView latitude;
    private TextView longitude;
    private TextView titulo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_video);
        videoRepository = new VideoRepository(this);
        video = videoRepository.getLastVideo(); //carrega o ultimo video
        this.videoView = findViewById(R.id.videoView);
        this.latitude = findViewById(R.id.latitude);
        this.longitude = findViewById(R.id.longitude);
        MediaController mediaController = new MediaController(this);
        this.videoView.setMediaController(mediaController);

        Uri uri = Uri.parse(video.getCaminho());
        this.videoView.setVideoURI(uri);

        this.latitude.setText(video.getLatitude()+"");
        this.longitude.setText(video.getLongitude()+"");
        this.titulo = findViewById(R.id.titulo);
        titulo.setText(video.getTitulo());
        videoView.start();
    }


}
