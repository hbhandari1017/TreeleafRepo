package com.example.treeleafquiz.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.treeleafquiz.R;
import com.example.treeleafquiz.database.Entity.QuestionEntity;
import com.example.treeleafquiz.databinding.FragmentQuestionBinding;
import com.example.treeleafquiz.util.NetworkUtil;
import com.example.treeleafquiz.util.QuizPreference;
import com.example.treeleafquiz.util.Resource;
import com.example.treeleafquiz.viewmodel.SharedViewModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuizQuestionFrag extends Fragment {
    private SharedViewModel mViewModel;
    private FragmentQuestionBinding quizBinding;


    private CountDownTimer countDownTimer;

    RadioButton selectedRadioButton;

    private Boolean firstTime = true;

    public static QuizQuestionFrag newInstance() {
        return new QuizQuestionFrag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        quizBinding = FragmentQuestionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        return quizBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quizBinding.answerSubmitButton.setEnabled(false);
        initListeners();
        initObservers();
        if (NetworkUtil.isConnected(requireContext())){
            mViewModel.getAllQuestions();
        } else {
            mViewModel.getQuestionsFromDB();
        }



    }

    private void initObservers() {
        mViewModel.getQuestionsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.getState() == Resource.State.LOADING) {

                quizBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                quizBinding.progressBar.setVisibility(View.GONE);
                List<QuestionEntity> questions = resource.getData();
                mViewModel.getIndividualQuestionFromDB(mViewModel.getQuestionNumber());
                // Handle success state and display the questions
            } else if (resource.getState() == Resource.State.ERROR) {
                quizBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                if(errorMessage == null){
                    errorMessage = "";
                }
                Toast.makeText(getContext(), errorMessage+" check internet connection", Toast.LENGTH_SHORT).show();
                checkInternetConnection();
            }
        });
        mViewModel.getIndividualQuestionsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.getState() == Resource.State.LOADING) {

                quizBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                quizBinding.progressBar.setVisibility(View.GONE);
                if (mViewModel.getQuestionNumber() == 11) {
                    moveToResultScreen();
                }
                Log.e("IMp", "question no " + mViewModel.getQuestionNumber());
                mViewModel.setQuestionNumber(mViewModel.getQuestionNumber() + 1);
                QuestionEntity question = resource.getData();
                Gson gson = new Gson();
                String questionJson = gson.toJson(question);
                Log.e("result", "res " + gson.toJson(question));
                loadQuiz(question, questionJson);


                // Handle success state and display the questions
            } else if (resource.getState() == Resource.State.ERROR) {
                quizBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                if(errorMessage == null){
                    errorMessage = "";
                }
                Toast.makeText(getContext(), errorMessage+" check internet connection", Toast.LENGTH_SHORT).show();
                checkInternetConnection();
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
                if(errorMessage == null){
                    errorMessage = "";
                }
                Toast.makeText(getContext(), errorMessage+" check internet connection", Toast.LENGTH_SHORT).show();
                checkInternetConnection();
            }
        });

    }

    private void checkInternetConnection() {
        if (NetworkUtil.isConnected(requireContext())){
            mViewModel.getAllQuestions();
        }
    }

    private void moveToResultScreen() {
        Activity activity = getActivity();

        // Check if the activity reference is not null and of the correct type
        if (activity instanceof MainActivity) {
            MainActivity hostingActivity = (MainActivity) activity;
            hostingActivity.moveToResultFragment();
        }
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
                quizBinding.timerTextView.setText("00:00");
                moveToResultScreen();
            }
        };

        countDownTimer.start(); // Start the countdown timer
    }

    private void loadQuiz(QuestionEntity question, String questionJson) {
        if (firstTime) {
            startCountdownTimer(120000);
            firstTime = false;
            quizBinding.questionCount.setText("1/10");
        }
        int newQuestionCount = mViewModel.questionCount++;
        quizBinding.questionCount.setText(newQuestionCount+"/10");
        resetAnswerBg();
        quizBinding.questionTextView.setText(question.getQuestion());
        quizBinding.tagsTextView.setText(question.getTagsJson());
        findCorrectAnswer(questionJson);
        try {
            JSONObject jsonObject = new JSONObject(questionJson);
            String answersJsonString = jsonObject.getString("answersJson");
            String tagJson = jsonObject.getString("tagsJson");
            JSONObject answersJson = new JSONObject(answersJsonString);
            String answerA = answersJson.optString("answer_a", "");

            JSONArray titleObject = new JSONArray(tagJson);
            JSONObject titleFinalObj = titleObject.getJSONObject(0);
            String finalTitle = titleFinalObj.optString("name", "");

            quizBinding.tagsTextView.setText(finalTitle);
            if (answerA.isEmpty()) {
                quizBinding.optionA.setVisibility(View.GONE);
            } else {
                quizBinding.optionA.setVisibility(View.VISIBLE);
                quizBinding.optionA.setText(answerA);
            }

            String answerB = answersJson.optString("answer_b", "");

            if (answerB.isEmpty()) {
                quizBinding.optionB.setVisibility(View.GONE);
            } else {
                quizBinding.optionB.setVisibility(View.VISIBLE);
                quizBinding.optionB.setText(answerB);
            }


            String answerC = answersJson.optString("answer_c", "");
            if (answerC.isEmpty()) {
                quizBinding.optionC.setVisibility(View.GONE);
            } else {
                quizBinding.optionC.setVisibility(View.VISIBLE);
                quizBinding.optionC.setText(answerC);
            }

            String answerD = answersJson.optString("answer_d", "");
            if (answerD.isEmpty()) {
                quizBinding.optionD.setVisibility(View.GONE);
            } else {
                quizBinding.optionD.setVisibility(View.VISIBLE);
                quizBinding.optionD.setText(answerD);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    private void findCorrectAnswer(String questionJson) {

        try {
            JSONObject jsonObject = new JSONObject(questionJson);


            String answersJsonString = jsonObject.getString("correct_answersJson");
            JSONObject answersJson = new JSONObject(answersJsonString);

            String isAnswerACorrect = answersJson.optString("answer_a_correct", "");
            mViewModel.setIsAnswerACorrect(isAnswerACorrect);
            String isAnswerBCorrect = answersJson.optString("answer_b_correct", "");
            mViewModel.setIsAnswerBCorrect(isAnswerBCorrect);
            String isAnswerCCorrect = answersJson.optString("answer_c_correct", "");
            mViewModel.setIsAnswerCCorrect(isAnswerCCorrect);
            String isAnswerDCorrect = answersJson.optString("answer_d_correct", "");
            mViewModel.setIsAnswerDCorrect(isAnswerDCorrect);
            String isAnswerECorrect = answersJson.optString("answer_e_correct", "");
            mViewModel.setIsAnswerECorrect(isAnswerECorrect);
            String isAnswerFCorrect = answersJson.optString("answer_f_correct", "");
            mViewModel.setIsAnswerFCorrect(isAnswerFCorrect);

        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        countDownTimer.cancel();
        quizBinding = null;
    }


    private void initListeners() {
        quizBinding.answerOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    Log.e("VeryImp", "the radio button selected is" + checkedId);
                    selectedRadioButton = quizBinding.getRoot().findViewById(checkedId);
                    quizBinding.answerSubmitButton.setEnabled(true);
                } else {
                    // Disable the button when no radio button is selected
                    quizBinding.answerSubmitButton.setEnabled(false);
                }
            }
        });


        quizBinding.answerSubmitButton.setOnClickListener(v -> {
            quizBinding.progressBar.setVisibility(View.VISIBLE);

            if (Objects.equals(mViewModel.getIsAnswerACorrect(), "true")) {
                quizBinding.optionA.setBackgroundColor(getResources().getColor(R.color.green));


                Log.e("IMp", "answer actually is  a");
                if (selectedRadioButton.getId() == R.id.optionA) {

                    QuizPreference.setResult(QuizPreference.getResult() + 1);
                }
            }
            if (Objects.equals(mViewModel.getIsAnswerBCorrect(), "true")) {
                quizBinding.optionB.setBackgroundColor(getResources().getColor(R.color.green));
                Log.e("IMp", "answer actually is  b");
                if (selectedRadioButton.getId() == R.id.optionB) {
                    QuizPreference.setResult(QuizPreference.getResult() + 1);
                }

            }
            if (Objects.equals(mViewModel.getIsAnswerCCorrect(), "true")) {
                quizBinding.optionC.setBackgroundColor(getResources().getColor(R.color.green));
                Log.e("IMp", "answer actually is  c");
                if (selectedRadioButton.getId() == R.id.optionC) {
                    QuizPreference.setResult(QuizPreference.getResult() + 1);
                }

            }
            if (Objects.equals(mViewModel.getIsAnswerDCorrect(), "true")) {
                quizBinding.optionD.setBackgroundColor(getResources().getColor(R.color.green));
                Log.e("IMp", "answer actually is  d");
                if (selectedRadioButton.getId() == R.id.optionD) {
                    QuizPreference.setResult(QuizPreference.getResult() + 1);
                }
            }
            quizBinding.progressBar.setVisibility(View.GONE);
            Log.e("IMp", "  a" + R.id.optionA);
            Log.e("IMp", "b" + R.id.optionB);
            Log.e("IMp", "c" + R.id.optionC);
            Log.e("IMp", "d" + R.id.optionD);
            Log.e("IMp", "answer selected is  " + selectedRadioButton.getId());
            new Handler().postDelayed(() -> {

            mViewModel.getIndividualQuestionFromDB(mViewModel.getQuestionNumber());

            }, 500);
        });

    }

    private void resetAnswerBg() {
        quizBinding.optionA.setBackgroundColor(getResources().getColor(R.color.white));
        quizBinding.optionB.setBackgroundColor(getResources().getColor(R.color.white));
        quizBinding.optionC.setBackgroundColor(getResources().getColor(R.color.white));
        quizBinding.optionD.setBackgroundColor(getResources().getColor(R.color.white));
    }
}