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
import com.example.treeleafquiz.fragments.ResultFrag;
import com.example.treeleafquiz.model.Question;
import com.example.treeleafquiz.network.QuizApi;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,LoginFragment.newInstance());
        transaction.commit();

    }
    public void moveToQuestionFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, QuizQuestionFrag.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

    }
    public void moveToResultFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ResultFrag.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();

    }




}