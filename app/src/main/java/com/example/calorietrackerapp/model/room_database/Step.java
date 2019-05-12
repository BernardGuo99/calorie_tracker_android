package com.example.calorietrackerapp.model.room_database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Step {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "user_id")
    public String userId;
    @ColumnInfo(name = "step_count")
    public int stepCount;
    @ColumnInfo(name = "create_time")
    public String createdTime;

    public Step(String userId, int stepCount, String createdTime) {
        this.userId = userId;
        this.stepCount = stepCount;
        this.createdTime = createdTime;
    }

    public int getUid() {
        return uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
