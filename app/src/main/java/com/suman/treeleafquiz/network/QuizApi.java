package com.suman.treeleafquiz.network;


import static com.suman.treeleafquiz.util.ApiConstant.getAllQuestions;

import com.suman.treeleafquiz.model.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuizApi {
    @GET(getAllQuestions)
    Call<List<Question>> getAllQuestions();
}
