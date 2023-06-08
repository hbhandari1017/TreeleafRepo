package com.example.treeleafquiz.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.treeleafquiz.MainActivity;
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

        // Check if the activity reference is not null and of the correct type
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

    private void initView() {
        int result = QuizPreference.getResult();
        String resultString = Integer.toString(result);
       resultBinding.showResult.setText(resultString);
       resultBinding.bottomText.setText("Your Score is "+resultString +" Points");
       resultBinding.userNameHere.setText("Well Played "+ QuizPreference.getName());
    }




}