package com.example.calorietrackerapp.model.room_database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepDao {
    @Query("SELECT * FROM Step")
    List<Step> getAll();

    @Query("SELECT * FROM Step WHERE user_id = :userId")
    List<Step> findByUserId(String userId);

    @Query("SELECT * FROM step WHERE uid = :uid LIMIT 1")
    Step findByID(int uid);

    @Insert
    void insertAll(Step... step);

    @Insert
    long insert(Step step);

    @Delete
    void delete(Step step);

    @Update(onConflict = REPLACE)
    void updateSteps(Step... steps);

    @Query("DELETE FROM step")
    void deleteAll();

    @Query("DELETE FROM Step WHERE user_id = :userId")
    void deleteByUserId(String userId);


}
