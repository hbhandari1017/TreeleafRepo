package com.suman.treeleafquiz.database.Dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.suman.treeleafquiz.database.Entity.QuestionEntity;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long>  insertQuestions(List<QuestionEntity> question);

    @Query("SELECT * FROM quiz_question_tbl LIMIT 10")
    List<QuestionEntity> getAllQuestions();
    @Query("SELECT * FROM quiz_question_tbl WHERE id = :questionId LIMIT 1")
    QuestionEntity getQuestionById(int questionId);

}

