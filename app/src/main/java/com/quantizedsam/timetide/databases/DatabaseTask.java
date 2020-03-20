package com.quantizedsam.timetide.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.quantizedsam.timetide.models.Task;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class DatabaseTask extends RoomDatabase {

    public abstract DaoTask daoTask();
}
