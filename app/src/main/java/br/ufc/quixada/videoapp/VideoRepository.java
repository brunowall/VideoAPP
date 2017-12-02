package br.ufc.quixada.videoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by darkbyte on 01/12/17.
 */

public class VideoRepository {
    private SQLiteDatabase database;
    private PersistenceHelper persistenceHelper;
    public VideoRepository(Context context){
        persistenceHelper = new PersistenceHelper(context);
    }

    public void addVideo(Video video){
        database=persistenceHelper.getWritableDatabase();
        database.insert("videos",null,getValues(video));
        database.close();
        database = null;
    }

    public List<Video> getVideos(){
        this.database = persistenceHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from videos",null);
        List<Video> videos = new LinkedList<Video>();
        while (cursor.moveToNext()){
            videos.add(buildVideo(cursor));
        }
        cursor.close();
        this.database.close();
        return videos;
    }


    public ContentValues getValues(Video video){
        ContentValues contentValues = new ContentValues();
        contentValues.put("latitude",video.getLatitude());
        contentValues.put("longitude",video.getLongitude());
        contentValues.put("caminho",video.getCaminho());
        contentValues.put("titulo",video.getTitulo());
        return  contentValues;
    }

    public Video buildVideo(Cursor cursor){
        Video v = new Video();
        v.setId(cursor.getLong(cursor.getColumnIndex("id")));
        v.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
        v.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
        v.setCaminho(cursor.getString(cursor.getColumnIndex("caminho")));
        v.setTitulo(cursor.getString(cursor.getColumnIndex("titulo")));
        return v;
    }

    public Video getLastVideo(){
        this.database = persistenceHelper.getReadableDatabase();
        String sql= "SELECT * FROM  videos  WHERE   ID = (SELECT MAX(id)  FROM videos);";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        return buildVideo(cursor);
    }
}
