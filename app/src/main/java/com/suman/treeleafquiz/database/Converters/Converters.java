package com.suman.treeleafquiz.database.Converters;


import androidx.room.TypeConverter;


import com.suman.treeleafquiz.model.Answers;
import com.suman.treeleafquiz.model.CorrectAnswers;
import com.suman.treeleafquiz.model.Tag;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static Answers fromAnswersJson(String value) {
        Type type = new TypeToken<Answers>() {
        }.getType();
        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String toAnswersJson(Answers answers) {
        return gson.toJson(answers);
    }

    @TypeConverter
    public static CorrectAnswers fromCorrectAnswersJson(String value) {
        Type type = new TypeToken<CorrectAnswers>() {
        }.getType();
        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String toCorrectAnswersJson(CorrectAnswers correctAnswers) {
        return gson.toJson(correctAnswers);
    }

    @TypeConverter
    public static List<Tag> fromTagsJson(String value) {
        Type type = new TypeToken<List<Tag>>() {
        }.getType();
        return gson.fromJson(value, type);
    }

    @TypeConverter
    public static String toTagsJson(List<Tag> tags) {
        return gson.toJson(tags);
    }
}

