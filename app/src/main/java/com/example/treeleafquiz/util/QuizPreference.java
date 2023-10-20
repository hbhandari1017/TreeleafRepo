package com.example.treeleafquiz.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class QuizPreference {

    public static final String USER = "USER";
    private static final String RESULT = "RESULT";


    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mEditor;

    public static final String KEY_APP = "QuizPrefs";

    public static void deleteEverything() {
        mEditor.clear();
        mEditor.commit();
    }

    public static void init(Context context) {
        mPrefs = context.getSharedPreferences(KEY_APP, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    public static void setName(String name) {
        mEditor.putString(USER, name).apply();
        mEditor.apply();



    }
    public static String getName() {
        return mPrefs.getString(USER, "");
    }

    public static void setResult(int count) {
        mEditor.putInt(RESULT, count);
        mEditor.apply();

    }

    public static int getResult() {
        return mPrefs.getInt(RESULT, 0);
    }


}
