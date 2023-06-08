package com.example.treeleafquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import com.example.treeleafquiz.database.Converters.Converters;
import com.example.treeleafquiz.database.Dao.QuestionDao;
import com.example.treeleafquiz.database.Entity.QuestionEntity;
import com.example.treeleafquiz.fragments.LoginFragment;
import com.example.treeleafquiz.fragments.QuizQuestionFrag;
import com.example.treeleafquiz.model.Question;
import com.example.treeleafquiz.network.QuizApi;

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

    @Inject
    QuestionDao questionDao;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,LoginFragment.newInstance());
        transaction.commit();
       // getALlQuestions();
    }
    public void moveToQuestionFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, QuizQuestionFrag.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

    }


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