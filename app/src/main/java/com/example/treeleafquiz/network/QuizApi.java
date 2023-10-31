package com.example.treeleafquiz.network;


import static com.example.treeleafquiz.util.ApiConstant.getAllQuestions;

import com.example.treeleafquiz.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuizApi {
    @GET(getAllQuestions)
    Call<List<Question>> getAllQuestions();
}
