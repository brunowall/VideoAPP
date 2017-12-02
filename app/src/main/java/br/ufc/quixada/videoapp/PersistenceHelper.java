package br.ufc.quixada.videoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by darkbyte on 01/12/17.
 */

public class PersistenceHelper extends SQLiteOpenHelper {
    private static final String NAME_DB = "Videos";
    private static final int VERSION = 1;

    PersistenceHelper(Context context){
        super(context,NAME_DB,null,VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table videos(id INTEGER PRIMARY KEY AUTOINCREMENT,latitude REAL, longitude REAL, caminho Text, titulo Text);";
        sqLiteDatabase.execSQL(sql);
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
