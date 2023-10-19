package com.example.treeleafquiz.viewmodel;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.treeleafquiz.callback.RepositoryCallback;
import com.example.treeleafquiz.database.Entity.QuestionEntity;
import com.example.treeleafquiz.model.Question;
import com.example.treeleafquiz.repo.SharedRepository;
import com.example.treeleafquiz.util.Resource;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SharedViewModel extends ViewModel {

    @Inject
    public SharedViewModel(SharedRepository repository) {
        this.repository = repository;
        questionsLiveData = new MutableLiveData<>();
        questionsFromApiState = new MutableLiveData<>();
        individualQuestionsLiveData = new MutableLiveData<>();
    }
    String isAnswerACorrect;
    String isAnswerBCorrect;



    public String getIsAnswerACorrect() {
        return isAnswerACorrect;
    }

    public void setIsAnswerACorrect(String isAnswerACorrect) {
        this.isAnswerACorrect = isAnswerACorrect;
    }

    public String getIsAnswerBCorrect() {
        return isAnswerBCorrect;
    }

    public void setIsAnswerBCorrect(String isAnswerBCorrect) {
        this.isAnswerBCorrect = isAnswerBCorrect;
    }

    public String getIsAnswerCCorrect() {
        return isAnswerCCorrect;
    }

    public void setIsAnswerCCorrect(String isAnswerCCorrect) {
        this.isAnswerCCorrect = isAnswerCCorrect;
    }

    public String getIsAnswerDCorrect() {
        return isAnswerDCorrect;
    }

    public void setIsAnswerDCorrect(String isAnswerDCorrect) {
        this.isAnswerDCorrect = isAnswerDCorrect;
    }

    public String getIsAnswerECorrect() {
        return isAnswerECorrect;
    }

    public void setIsAnswerECorrect(String isAnswerECorrect) {
        this.isAnswerECorrect = isAnswerECorrect;
    }

    public String getIsAnswerFCorrect() {
        return isAnswerFCorrect;
    }

    public void setIsAnswerFCorrect(String isAnswerFCorrect) {
        this.isAnswerFCorrect = isAnswerFCorrect;
    }

    String isAnswerCCorrect;
    String isAnswerDCorrect;
    String isAnswerECorrect;
    String isAnswerFCorrect;

    private SharedRepository repository;

    Handler mainHandler = new Handler(Looper.getMainLooper());

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int questionNumber = 1 ;
    private MutableLiveData<Resource<List<QuestionEntity>>> questionsLiveData;
    private MutableLiveData<Resource<QuestionEntity>> individualQuestionsLiveData;
    private MutableLiveData<Resource<String>> questionsFromApiState;

    public LiveData<Resource<List<QuestionEntity>>> getQuestionsLiveData() {
        return questionsLiveData;
    }

    public LiveData<Resource<QuestionEntity>> getIndividualQuestionsLiveData() {
        return individualQuestionsLiveData;
    }

    public LiveData<Resource<String>> getApiStateLiveData() {
        return questionsFromApiState;
    }
    public void getQuestionsFromDB() {
        questionsLiveData.setValue(new Resource<>(null, null, Resource.State.LOADING));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                final List<QuestionEntity> questions = repository.getQuestionsFromDB();
                Log.e("DB", "no of added questions: " + gson.toJson(questions));

                // Update the UI on the main thread
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (questions != null) {
                            questionsLiveData.setValue(new Resource<>(questions, null, Resource.State.SUCCESS));
                        } else {
                            questionsLiveData.setValue(new Resource<>(null, "Error retrieving questions", Resource.State.ERROR));
                        }
                    }
                });
            }
        });

       // List<QuestionEntity> questions = repository.getQuestionsFromDB();

    }


    public void getIndividualQuestionFromDB(int questionId) {
        individualQuestionsLiveData.setValue(new Resource<>(null, null, Resource.State.LOADING));
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                final QuestionEntity questions = repository.getQuestionsFromDB(questionId);
                Log.e("DB", "no of added questions: " + gson.toJson(questions));

                // Update the UI on the main thread
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (questions != null) {
                            individualQuestionsLiveData.setValue(new Resource<>(questions, null, Resource.State.SUCCESS));
                        } else {
                            individualQuestionsLiveData.setValue(new Resource<>(null, "Error retrieving questions", Resource.State.ERROR));
                        }
                    }
                });
            }
        });

        // List<QuestionEntity> questions = repository.getQuestionsFromDB();

    }


    public void getAllQuestions() {
        questionsFromApiState.setValue(new Resource<>("data success", null, Resource.State.LOADING));
        repository.getAllQuestions(new RepositoryCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> resultFromApi) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionsFromApiState.setValue(new Resource<>("data success", null, Resource.State.SUCCESS));
                    }
                });

            }

            @Override
            public void onError(int errorCode) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionsFromApiState.setValue(new Resource<>("http error", null, Resource.State.ERROR));
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionsFromApiState.setValue(new Resource<>("exception", null, Resource.State.ERROR));
                    }
                });

            }

            @Override
            public void onOtherError(String error) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        questionsFromApiState.setValue(new Resource<>("other  error", null, Resource.State.ERROR));
                    }
                });

            }
        });
    }

}




