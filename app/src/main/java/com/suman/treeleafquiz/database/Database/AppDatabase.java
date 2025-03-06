package com.suman.treeleafquiz.database.Database;


import static com.suman.treeleafquiz.util.AppConstant.DB_VERSION;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.suman.treeleafquiz.database.Converters.Converters;
import com.suman.treeleafquiz.database.Dao.QuestionDao;
import com.suman.treeleafquiz.database.Entity.QuestionEntity;

@Database(entities = {QuestionEntity.class}, version = DB_VERSION, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuestionDao questionDao();
}

