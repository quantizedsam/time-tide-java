package com.quantizedsam.timetide.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.quantizedsam.timetide.helpers.TimestampConverter;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Task implements Serializable {

    // variables
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    @ColumnInfo(name = "due_date")
    private String dueDate;
    @ColumnInfo(name = "alarm_on_due")
    private boolean alarmOnDue;
    @ColumnInfo(name = "start_date")
    private String startDate;
    @ColumnInfo(name = "alarm_on_start")
    private boolean alarmOnStart;
    private String parent;
    private String color;
    private int progress;
    private int status;
    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    private Date createdAt;
    @ColumnInfo(name = "modified_at")
    @TypeConverters({TimestampConverter.class})
    private Date modifiedAt;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isAlarmOnDue() {
        return alarmOnDue;
    }

    public void setAlarmOnDue(boolean alarmOnDue) {
        this.alarmOnDue = alarmOnDue;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public boolean isAlarmOnStart() {
        return alarmOnStart;
    }

    public void setAlarmOnStart(boolean alarmOnStart) {
        this.alarmOnStart = alarmOnStart;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
