package com.example.treeleafquiz.di.module;

import com.example.treeleafquiz.database.Dao.QuestionDao;
import com.example.treeleafquiz.network.QuizApi;
import com.example.treeleafquiz.repo.SharedRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;


@Module
@InstallIn(SingletonComponent.class)
public class RepoModule {

    @Provides
    public static SharedRepository provideRepo(QuizApi api, QuestionDao dao) {
        return new SharedRepository(api,dao);

    }

}

