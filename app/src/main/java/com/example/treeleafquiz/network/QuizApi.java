package com.example.treeleafquiz.network;


import com.example.treeleafquiz.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizApi {
    @GET("v1/questions")
    Call<List<Question>> getAllQuestions();
}
