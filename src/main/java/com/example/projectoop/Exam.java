package com.example.projectoop;

public class Exam {
    private int id;
    private String title;
    private String author;
    private int questionCount;

    public Exam(int id, String title, String author, int questionCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.questionCount = questionCount;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
