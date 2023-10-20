package com.example.treeleafquiz.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.treeleafquiz.databinding.FragmentScoreBinding;
import com.example.treeleafquiz.util.QuizPreference;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ResultFrag extends Fragment {


    private FragmentScoreBinding resultBinding;

    public static ResultFrag newInstance() {
        return new ResultFrag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        resultBinding = FragmentScoreBinding.inflate(inflater, container, false);

        return resultBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListeners();



    }
    private void moveToResultScreen() {
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            MainActivity hostingActivity = (MainActivity) activity;
            hostingActivity.moveToQuestionFragment();
        }
    }

    private void initListeners() {
        resultBinding.playAgain.setOnClickListener(v -> {
            QuizPreference.setResult(0);
            moveToResultScreen();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QuizPreference.setResult(0);
    }

    private void initView() {
        int result = QuizPreference.getResult();
        int finalResult = result*10;
        String resultString = Integer.toString(result);

        resultBinding.progressBar.setProgress(finalResult);
       resultBinding.showResult.setText(resultString);
       resultBinding.bottomText.setText(String.format("Your Score is %s Points", resultString));
       if(result>5) resultBinding.userNameHere.setText(String.format("Well Played %s !!", QuizPreference.getName()));
       else resultBinding.userNameHere.setText(String.format("Well tried  %s !", QuizPreference.getName()));
    }




}