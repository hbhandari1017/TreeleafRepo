package com.suman.treeleafquiz;

import android.app.Application;

import com.suman.treeleafquiz.util.QuizPreference;

import dagger.hilt.android.HiltAndroidApp;


@HiltAndroidApp
public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QuizPreference.init(getApplicationContext());
        QuizPreference.setResult(0);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Perform cleanup tasks here
    }


}
