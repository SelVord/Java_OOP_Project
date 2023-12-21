package com.example.projectoop;

public class QuestionResult {
    private String questionText;
    private String userAnswer;
    private Float grade;

    public QuestionResult(String questionText, String userAnswer, Float grade) {
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.grade = grade;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public Float getGrade() {
        return grade;
    }
}
