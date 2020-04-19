package com.diazmiranda.juanjose.mibebe.roomdata.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.diazmiranda.juanjose.mibebe.roomdata.entities.Doctor;

import java.util.List;

@Dao
public interface DoctorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Doctor doctor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Doctor> doctors);

    @Update
    void update(Doctor doctor);

    @Delete
    void delete(Doctor doctor);

    @Query("SELECT * FROM doctor WHERE id = :id")
    Doctor get(int id);

    @Query("SELECT * FROM doctor ORDER BY nombre ASC")
    List<Doctor> get();
}
