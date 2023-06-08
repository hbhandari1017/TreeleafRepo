package com.example.treeleafquiz.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.treeleafquiz.R;
import com.example.treeleafquiz.database.Entity.QuestionEntity;
import com.example.treeleafquiz.databinding.FragmentLoginBinding;
import com.example.treeleafquiz.databinding.FragmentQuestionBinding;
import com.example.treeleafquiz.util.QuizPreference;
import com.example.treeleafquiz.util.Resource;
import com.example.treeleafquiz.util.SoftKeyboardUtil;
import com.example.treeleafquiz.viewmodel.LoginViewModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuizQuestionFrag extends Fragment {
    private LoginViewModel mViewModel ;
    private FragmentQuestionBinding quizBinding;

    private CountDownTimer countDownTimer;

    private Boolean firstTime = true;

    public static QuizQuestionFrag newInstance() {
        return new QuizQuestionFrag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        quizBinding = FragmentQuestionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        return quizBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListeners();
        initObservers();
        mViewModel.getAllQuestions();

    }

    private void initObservers() {
        mViewModel.getQuestionsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.getState() == Resource.State.LOADING) {

              quizBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                quizBinding.progressBar.setVisibility(View.GONE);
                List<QuestionEntity> questions = resource.getData();
                // Handle success state and display the questions
            } else if (resource.getState() == Resource.State.ERROR) {
                quizBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getIndividualQuestionsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.getState() == Resource.State.LOADING) {

                quizBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                quizBinding.progressBar.setVisibility(View.GONE);
                mViewModel.setQuestionNumber(mViewModel.getQuestionNumber()+1);
                QuestionEntity question = resource.getData();
                Gson gson = new Gson();
                String questionJson = gson.toJson(question);
               loadQuiz(question,questionJson);

                Log.e("DB", "no of added questions: " + gson.toJson(question));
                // Handle success state and display the questions
            } else if (resource.getState() == Resource.State.ERROR) {
                quizBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getApiStateLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.getState() == Resource.State.LOADING) {
                quizBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                quizBinding.progressBar.setVisibility(View.GONE);
               mViewModel.getIndividualQuestionFromDB(mViewModel.getQuestionNumber());
            } else if (resource.getState() == Resource.State.ERROR) {
                quizBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void startCountdownTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Convert milliseconds to minutes and seconds
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                // Format the time as "mm:ss" and update the TextView
                String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                quizBinding.timerTextView.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                quizBinding.timerTextView.setText("00:00"); // Update the TextView when the countdown finishes
            }
        };

        countDownTimer.start(); // Start the countdown timer
    }

    private void loadQuiz(QuestionEntity question,String questionJson) {
        if(firstTime){
            startCountdownTimer(120000);
            firstTime = false;
        }
        quizBinding.questionTextView.setText(question.getQuestion());
        quizBinding.tagsTextView.setText(question.getTagsJson());
        try {
            JSONObject jsonObject = new JSONObject(questionJson);


            String answersJsonString = jsonObject.getString("answersJson");
            JSONObject answersJson = new JSONObject(answersJsonString);



            String answerA = answersJson.optString("answer_a", "");

            if(answerA.isEmpty() ) {
                quizBinding.optionA.setVisibility(View.GONE);
            } else
            {
                quizBinding.optionA.setVisibility(View.VISIBLE);
                quizBinding.optionA.setText(answerA);
            }

            String answerB = answersJson.optString("answer_b", "");

            if(answerB.isEmpty() ) {
                quizBinding.optionB.setVisibility(View.GONE);
            } else
            {
                quizBinding.optionB.setVisibility(View.VISIBLE);
                quizBinding.optionB.setText(answerB);
            }


            String answerC = answersJson.optString("answer_c", "");
            if(answerC.isEmpty() ) {
                quizBinding.optionC.setVisibility(View.GONE);
            } else
            {
                quizBinding.optionC.setVisibility(View.VISIBLE);
                quizBinding.optionC.setText(answerB);
            }

            String answerD = answersJson.optString("answer_d", "");
            if(answerD.isEmpty() ) {
                quizBinding.optionD.setVisibility(View.GONE);
            } else
            {
                quizBinding.optionD.setVisibility(View.VISIBLE);
            quizBinding.optionD.setText(answerD);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    private void initListeners() {

        quizBinding.answerSubmitButton.setOnClickListener(v -> {
            mViewModel.getIndividualQuestionFromDB(mViewModel.getQuestionNumber());
        });

    }
}