package com.suman.treeleafquiz.repo;


import android.util.Log;

import androidx.annotation.NonNull;

import com.suman.treeleafquiz.callback.RepositoryCallback;
import com.suman.treeleafquiz.database.Converters.Converters;
import com.suman.treeleafquiz.database.Dao.QuestionDao;
import com.suman.treeleafquiz.database.Entity.QuestionEntity;
import com.suman.treeleafquiz.model.Question;
import com.suman.treeleafquiz.network.QuizApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SharedRepository {
    private final QuizApi apiService;
    private final QuestionDao dao;

    @Inject
    public SharedRepository(QuizApi apiService, QuestionDao dao) {
        this.apiService = apiService;
        this.dao = dao;
    }

    public void getAllQuestions(RepositoryCallback<List<Question>> callback) {
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(@NonNull Call<List<Question>> call, @NonNull Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    List<Question> resultFromApi = response.body();
                    if(resultFromApi !=null) addQuestionsToDB(resultFromApi, callback);
                    else  callback.onOtherError("no data from api");

                } else {
                    callback.onError(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void addQuestionsToDB(List<Question> resultFromApi,RepositoryCallback<List<Question>> callback) {
        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (Question question : resultFromApi) {
            QuestionEntity questionEntity = createQuestionEntity(question);
            questionEntities.add(questionEntity);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Long> noOfDataAdded = dao.insertQuestions(questionEntities);
            if(!noOfDataAdded.isEmpty()){
                callback.onSuccess(resultFromApi);
                Log.e("DB", "no of added questions: " + noOfDataAdded);
            } else {
                callback.onOtherError("no data added");
            }

        });
    }



    private QuestionEntity createQuestionEntity(Question question) {
        QuestionEntity questionEntity = new QuestionEntity();

        questionEntity.setQuestion(question.getQuestion());
        questionEntity.setDescription(question.getDescription());

        questionEntity.setAnswersJson(Converters.toAnswersJson(question.getAnswers()));
        questionEntity.setMultiple_correct_answers(question.isMultiple_correct_answers());
        questionEntity.setCorrect_answersJson(Converters.toCorrectAnswersJson(question.getCorrect_answers()));
        questionEntity.setExplanation(question.getExplanation());
        questionEntity.setTip(question.getTip());
        questionEntity.setTagsJson(Converters.toTagsJson(question.getTags()));
        questionEntity.setCategory(question.getCategory());
        questionEntity.setDifficulty(question.getDifficulty());
        return questionEntity;

    }

    public List<QuestionEntity> getQuestionsFromDB() {
        return dao.getAllQuestions();
    }

    public QuestionEntity getQuestionsFromDB(int questionId) {
        return dao.getQuestionById(questionId);
    }
}

