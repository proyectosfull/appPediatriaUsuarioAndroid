package com.diazmiranda.juanjose.mibebe.roomdata.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.diazmiranda.juanjose.mibebe.roomdata.entities.BabyData;

import java.util.List;

@Dao
public interface BabyDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BabyData data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<BabyData> data);

    @Update
    void update(BabyData data);

    @Delete
    void delete(BabyData data);

    @Delete
    void delete(List<BabyData> data);

    @Query("SELECT * FROM babyData")
    List<BabyData> getList();

    @Query("SELECT * FROM babyData WHERE dependienteId = :dependienteId")
    List<BabyData> getList(int dependienteId);
}
