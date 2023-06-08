package com.example.treeleafquiz.database.Entity;


import static com.example.treeleafquiz.util.AppConstant.TABLE_NAME;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = TABLE_NAME)
public class QuestionEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String question;
    private String description;
    private String answersJson;
    private boolean multiple_correct_answers;
    private String correct_answersJson;
    private String correct_answer;
    private String explanation;
    private String tip;
    private String tagsJson;
    private String category;
    private String difficulty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswersJson() {
        return answersJson;
    }

    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }

    public boolean isMultiple_correct_answers() {
        return multiple_correct_answers;
    }

    public void setMultiple_correct_answers(boolean multiple_correct_answers) {
        this.multiple_correct_answers = multiple_correct_answers;
    }

    public String getCorrect_answersJson() {
        return correct_answersJson;
    }

    public void setCorrect_answersJson(String correct_answersJson) {
        this.correct_answersJson = correct_answersJson;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTagsJson() {
        return tagsJson;
    }

    public void setTagsJson(String tagsJson) {
        this.tagsJson = tagsJson;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
// Getters and setters
}

