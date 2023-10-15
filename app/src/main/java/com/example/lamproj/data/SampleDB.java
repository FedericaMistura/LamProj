package com.example.lamproj.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Sample.class}, version = 1, exportSchema = false)
public abstract class SampleDB extends RoomDatabase {
    public abstract SampleDao sampleDao();
}
