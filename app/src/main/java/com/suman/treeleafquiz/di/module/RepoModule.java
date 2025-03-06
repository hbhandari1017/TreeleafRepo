package com.suman.treeleafquiz.di.module;

import com.suman.treeleafquiz.database.Dao.QuestionDao;
import com.suman.treeleafquiz.network.QuizApi;
import com.suman.treeleafquiz.repo.SharedRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public class RepoModule {

    @Provides
    public static SharedRepository provideRepo(QuizApi api, QuestionDao dao) {
        return new SharedRepository(api,dao);

    }

}

