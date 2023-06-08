package com.example.treeleafquiz.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.treeleafquiz.viewmodel.LoginViewModel;
import com.google.gson.Gson;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuizQuestionFrag extends Fragment {
    private LoginViewModel mViewModel ;
    private FragmentQuestionBinding loginBinding;

    public static QuizQuestionFrag newInstance() {
        return new QuizQuestionFrag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        loginBinding = FragmentQuestionBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        return loginBinding.getRoot();

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

              loginBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                loginBinding.progressBar.setVisibility(View.GONE);
                List<QuestionEntity> questions = resource.getData();
                // Handle success state and display the questions
            } else if (resource.getState() == Resource.State.ERROR) {
                loginBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getIndividualQuestionsLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.getState() == Resource.State.LOADING) {

                loginBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                loginBinding.progressBar.setVisibility(View.GONE);
                QuestionEntity question = resource.getData();
                Gson gson = new Gson();
                Log.e("DB", "no of added questions: " + gson.toJson(question));
                // Handle success state and display the questions
            } else if (resource.getState() == Resource.State.ERROR) {
                loginBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getApiStateLiveData().observe(getViewLifecycleOwner(), resource -> {
            if (resource.getState() == Resource.State.LOADING) {
                loginBinding.progressBar.setVisibility(View.VISIBLE);
            } else if (resource.getState() == Resource.State.SUCCESS) {
                loginBinding.progressBar.setVisibility(View.GONE);
               mViewModel.getIndividualQuestionFromDB(1);
            } else if (resource.getState() == Resource.State.ERROR) {
                loginBinding.progressBar.setVisibility(View.GONE);
                String errorMessage = resource.getMessage();
                Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initListeners() {

    }
}