package com.quantizedsam.timetide.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.quantizedsam.timetide.models.Habit;

import java.util.List;

@Dao
public interface DaoHabit {

    @Insert
    void insertHabit(Habit habit);

    @Query("SELECT * FROM Habit ORDER BY time asc")
    List<Habit> getAllHabits();

    @Query("SELECT * FROM Habit WHERE id = :habitID")
    Habit getHabit(int habitID);

    @Query("SELECT * FROM Habit WHERE name = :habitName")
    Habit getHabit(String habitName);

    @Update
    void updateHabit(Habit habit);

    @Delete
    void deleteHabit(Habit habit);

    @Query("DELETE FROM Habit WHERE id = :habitID")
    void deleteHabit(int habitID);

    @Query("DELETE FROM Habit WHERE name = :habitName")
    void deleteHabit(String habitName);
}
