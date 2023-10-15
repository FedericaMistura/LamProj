package com.example.lamproj;
import android.app.Application;
import android.content.res.Configuration;

import androidx.room.Room;

import com.example.lamproj.data.SampleDB;
import com.example.lamproj.data.SampleDao;
public class App extends Application{
    public SampleDB db;
    public SampleDao sampleDao;
    public static App A;

    public App() {
        A=this;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),
                SampleDB.class, "test").allowMainThreadQueries().build();
        sampleDao = db.sampleDao();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
