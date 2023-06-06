package com.example.treeleafquiz.model;

import java.util.List;

public class Question {
    private int id;
    private String question;
    private String description;
    private Answers answers;
    private boolean multiple_correct_answers;
    private CorrectAnswers correct_answers;
    private String correct_answer;
    private String explanation;
    private String tip;
    private List<Tag> tags;
    private String category;
    private String difficulty;

    // Getters and setters

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

    public Answers getAnswers() {
        return answers;
    }

    public void setAnswers(Answers answers) {
        this.answers = answers;
    }

    public boolean isMultiple_correct_answers() {
        return multiple_correct_answers;
    }

    public void setMultiple_correct_answers(boolean multiple_correct_answers) {
        this.multiple_correct_answers = multiple_correct_answers;
    }

    public CorrectAnswers getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(CorrectAnswers correct_answers) {
        this.correct_answers = correct_answers;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
}





