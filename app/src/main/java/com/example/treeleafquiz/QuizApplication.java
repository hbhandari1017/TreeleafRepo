package com.example.treeleafquiz;

import android.app.Application;

import com.example.treeleafquiz.util.QuizPreference;

import dagger.hilt.android.HiltAndroidApp;


@HiltAndroidApp
public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QuizPreference.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Perform cleanup tasks here
    }


}
