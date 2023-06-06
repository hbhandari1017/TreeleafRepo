package com.example.treeleafquiz.di.module;

import static com.example.treeleafquiz.network.Url.API_KEY;
import static com.example.treeleafquiz.network.Url.BASE_URL;

import android.content.Context;

import androidx.room.Room;

import com.example.treeleafquiz.database.Dao.QuestionDao;
import com.example.treeleafquiz.database.Database.AppDatabase;
import com.example.treeleafquiz.network.QuizApi;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class DbModule {

    @Provides
    public static AppDatabase provideAppDatabase(@ApplicationContext Context context)  {
        return  Room.databaseBuilder(context,
                        AppDatabase.class, "TreeLeafQuiz")
                .build();
    }

    @Provides
    public static QuestionDao provideQuizDb(AppDatabase appDatabase) {
        return appDatabase.questionDao();

    }


}