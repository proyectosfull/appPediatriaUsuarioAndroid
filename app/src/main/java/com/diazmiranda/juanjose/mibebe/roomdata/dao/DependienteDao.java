package com.diazmiranda.juanjose.mibebe.roomdata.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.diazmiranda.juanjose.mibebe.roomdata.entities.Dependiente;

import java.util.List;

@Dao
public interface DependienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Dependiente data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Dependiente> data);

    @Update
    void update(Dependiente data);

    @Delete
    void delete(Dependiente data);

    @Delete
    void delete(List<Dependiente> data);

    @Query("SELECT * FROM dependiente")
    List<Dependiente> getList();

    @Query("SELECT * FROM dependiente WHERE id = :id")
    Dependiente get(int id);
}
