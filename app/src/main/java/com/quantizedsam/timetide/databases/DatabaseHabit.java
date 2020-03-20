package com.quantizedsam.timetide.databases;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.quantizedsam.timetide.models.Habit;

@Database(entities = {Habit.class}, version = 1, exportSchema = false)
public abstract class DatabaseHabit extends RoomDatabase {

    public abstract DaoHabit daoHabit();
}
