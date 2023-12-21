package com.example.projectoop;

import java.util.List;

public class ExamData {
    private Exam exam;
    private List<User> usersToGrade;

    public ExamData(Exam exam, List<User> usersToGrade) {
        this.exam = exam;
        this.usersToGrade = usersToGrade;
    }

    public Exam getExam() {
        return exam;
    }

    public List<User> getUsersToGrade() {
        return usersToGrade;
    }
}
