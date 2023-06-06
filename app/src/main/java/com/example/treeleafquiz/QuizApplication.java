package com.example.treeleafquiz;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;


@HiltAndroidApp
public class QuizApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Perform initialization tasks here
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Perform cleanup tasks here
    }


}
