package com.example.treeleafquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import com.example.treeleafquiz.database.Converters.Converters;
import com.example.treeleafquiz.database.Dao.QuestionDao;
import com.example.treeleafquiz.database.Database.AppDatabase;
import com.example.treeleafquiz.database.Entity.QuestionEntity;
import com.example.treeleafquiz.model.Question;
import com.example.treeleafquiz.network.QuizApi;
import com.example.treeleafquiz.network.RetrofitClient;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
   QuizApi apiClient;
  // QuizApi apiClient = RetrofitClient.getClient().create(QuizApi.class);
  //  AppDatabase appDatabase ;

    @Inject
    QuestionDao questionDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initDB();
        getALlQuestions();
    }

//    private void initDB() {
//        appDatabase = Room.databaseBuilder(getApplicationContext(),
//                        AppDatabase.class, "TreeLeafQuiz")
//                .build();
//        questionDao = appDatabase.questionDao();
//
//    }

    private void getALlQuestions() {

        apiClient.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(@NonNull Call<List<Question>> call, @NonNull Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    List<Question> resultFromApi = response.body();
                    // Process the resultFromApi
                    addQuestionToDB(resultFromApi);
                    // Log statement for successful response
                  //  Log.d("API Response", "Success! Result: " + resultFromApi);
                    Log.d("API Response", "Success! Result: " + resultFromApi.size());
                } else {
                    // Handle error response

                    // Log statement for error response
                    Log.e("API Response", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                // Handle failure

                // Log statement for failure
                Log.e("API Response", "Failure: " + t.getMessage());
            }
        });


    }

    private void addQuestionToDB(List<Question> resultFromApi) {
        // Convert List<Question> to List<QuestionEntity>
        List<QuestionEntity> questionEntities = new ArrayList<>();
        for (Question question : resultFromApi) {
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setId(question.getId());
            questionEntity.setQuestion(question.getQuestion());
            questionEntity.setDescription(question.getDescription());
            // Set other properties
            questionEntity.setAnswersJson(Converters.toAnswersJson(question.getAnswers()));
            questionEntity.setMultiple_correct_answers(question.isMultiple_correct_answers());
            questionEntity.setCorrect_answersJson(Converters.toCorrectAnswersJson(question.getCorrect_answers()));
            questionEntity.setExplanation(question.getExplanation());
            questionEntity.setTip(question.getTip());
            questionEntity.setTagsJson(Converters.toTagsJson(question.getTags()));
            questionEntity.setCategory(question.getCategory());
            questionEntity.setDifficulty(question.getDifficulty());
            questionEntities.add(questionEntity);
        }

        new Thread(() -> {
            Gson gson = new Gson();
            List<Long> noOfDataAdded =questionDao.insertQuestions(questionEntities);
            Log.e("DB", "no of added question: " + noOfDataAdded);
            List<QuestionEntity> storedQuestions = questionDao.getAllQuestions();
            Log.e("DB", "allquestion: " + gson.toJson(storedQuestions));
        }).start();


    }

}