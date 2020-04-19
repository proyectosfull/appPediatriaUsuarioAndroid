package com.diazmiranda.juanjose.mibebe.roomdata;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.diazmiranda.juanjose.mibebe.roomdata.dao.BabyDataDao;
import com.diazmiranda.juanjose.mibebe.roomdata.dao.DependienteDao;
import com.diazmiranda.juanjose.mibebe.roomdata.dao.DoctorDao;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.BabyData;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Dependiente;
import com.diazmiranda.juanjose.mibebe.roomdata.entities.Doctor;

@Database( version = 5, entities = {Dependiente.class, BabyData.class, Doctor.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "app";
    private static AppDatabase INSTANCE = null;

    abstract public DependienteDao dependiente();
    abstract public BabyDataDao babyData();
    abstract public DoctorDao doctor();

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();
        return INSTANCE;
    }
}
