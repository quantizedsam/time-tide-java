package com.quantizedsam.timetide.databases;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.quantizedsam.timetide.models.Task;

import java.util.List;

@Dao
public interface DaoTask {

    @Insert
    void insertTask(Task task);

    @Query("SELECT * FROM Task ORDER BY due_date asc")
    List<Task> getAllTasks();

    @Query("SELECT * FROM Task WHERE id = :taskID")
    Task getTask(int taskID);

    @Query("SELECT * FROM Task WHERE name = :taskName")
    Task getTask(String taskName);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM Task WHERE id = :taskID")
    void deleteTask(int taskID);

    @Query("DELETE FROM Task WHERE name = :taskName")
    void deleteTask(String taskName);
}
