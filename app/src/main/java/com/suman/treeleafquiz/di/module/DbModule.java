package com.suman.treeleafquiz.di.module;

import static com.suman.treeleafquiz.util.AppConstant.DB_NAME;

import android.content.Context;

import androidx.room.Room;

import com.suman.treeleafquiz.database.Dao.QuestionDao;
import com.suman.treeleafquiz.database.Database.AppDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DbModule {

    @Provides
    public static AppDatabase provideAppDatabase(@ApplicationContext Context context)  {
        return  Room.databaseBuilder(context,
                        AppDatabase.class, DB_NAME).fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    public static QuestionDao provideQuizDb(AppDatabase appDatabase) {
        return appDatabase.questionDao();

    }


}