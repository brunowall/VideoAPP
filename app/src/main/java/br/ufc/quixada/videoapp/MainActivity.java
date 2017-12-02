package br.ufc.quixada.videoapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity{
    private EditText titulo;
    private boolean possuiCartao;
    private boolean suportaCartao;
    private VideoRepository videoRepository;
    private Uri uri;
    private double latitude;
    private double longitude;
    Video video;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titulo = (EditText) findViewById(R.id.titulo);
        possuiCartao = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        suportaCartao = Environment.isExternalStorageRemovable();
        videoRepository = new VideoRepository(this);
        video = new Video();
        getLocationManager();
    }



    public void gravarVideoClicked(View View){
        this.setArquivoVideo();
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,20);
        startActivityForResult(intent,1);

    }

    private void setArquivoVideo(){
        File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(! possuiCartao){
            diretorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File pathVideo = new File(diretorio+"/"+this.titulo.getText().toString()+".mp4");

        if(android.os.Build.VERSION.SDK_INT >= 23){
            String authority = this.getApplicationContext().getPackageName() + ".fileprovider";
            uri = FileProvider.getUriForFile(this, authority, pathVideo);
        }else{
            uri = Uri.fromFile(pathVideo);
        }
    }


    private void getPermissoes() {
        String CAMERA = Manifest.permission.CAMERA;
        String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
        int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
        boolean camera = ActivityCompat.checkSelfPermission(this,CAMERA) == PERMISSION_GRANTED;
        boolean escrita = ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
        boolean leitura = ActivityCompat.checkSelfPermission(this,READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED;

        if(camera && escrita && leitura){
            return;
        }else {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                gravarVideoClicked(null);
            } else {
                Toast.makeText(this, "Sem permissão para uso de câmera.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){

            video.setTitulo(this.titulo.getText().toString());
            video.setCaminho(this.uri.toString());
            video.setLatitude(this.latitude);
            video.setLongitude(this.longitude);
            video.setCaminho(data.getData().toString());
            videoRepository.addVideo(video);
            Intent intent = new Intent(this,MostrarVideo.class);
            startActivity(intent);
        }

    }



    public void getLocationManager(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    2);

            return;
        }

       locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
           @Override
           public void onLocationChanged(Location location) {
                MainActivity.this.longitude = location.getLongitude();
                MainActivity.this.latitude = location.getLatitude();
           }

           @Override
           public void onStatusChanged(String s, int i, Bundle bundle) {

           }

           @Override
           public void onProviderEnabled(String s) {

           }

           @Override
           public void onProviderDisabled(String s) {

           }
       });

    }
}
