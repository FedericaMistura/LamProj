package com.example.lamproj.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Sample {
    @PrimaryKey
    public int uid;

    //  @ColumnInfo(name = "time")
//    public LocalDateTime time;

    @ColumnInfo(name = "latitude")
    public double latitude;

    @ColumnInfo(name = "longitude")
    public double longitude;

    @ColumnInfo(name = "lte")
    public double lte;

    @ColumnInfo(name = "wifi")
    public double wifi;

}
