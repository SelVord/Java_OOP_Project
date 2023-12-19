package com.example.projectoop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ExamDetails {
    @FXML
    private Label titleLabel;

    @FXML
    private Label gradeLabel;

    @FXML
    private VBox detailsVBox;

    private String selectedExamTitle;

    public void initData(String examTitle) {
        selectedExamTitle = examTitle;
        initializeLabels();
        loadQuestionsAndAnswers();
    }

    private void initializeLabels() {
        titleLabel.setText(selectedExamTitle);
        String grade = String.valueOf(JavaPSQL.getGradeForExam(JavaPSQL.getUserId(AppData.getCurrentUser()), JavaPSQL.getIdByTitle(selectedExamTitle)));
        gradeLabel.setText("Grade: " + (grade != null ? grade : "Not graded"));
    }

    private void loadQuestionsAndAnswers() {
        List<QuestionResult> questionResults = JavaPSQL.getQuestionResultsForExamAndUser(JavaPSQL.getUserId(AppData.getCurrentUser()), JavaPSQL.getIdByTitle(selectedExamTitle));

        for (QuestionResult questionResult : questionResults) {
            Label questionLabel = new Label("Question: " + questionResult.getQuestionText());
            Label userAnswerLabel = new Label("User Answer: " + questionResult.getUserAnswer());
            Label gradeLabel = new Label("Grade: " + (questionResult.getGrade() != null ?
                    String.format("%.2f", questionResult.getGrade()) : "Not graded"));

            VBox questionBox = new VBox(questionLabel, userAnswerLabel, gradeLabel);
            detailsVBox.getChildren().add(questionBox);
        }
    }

    public void closeBtn() {
        Stage currentStage = (Stage) detailsVBox.getScene().getWindow();
        currentStage.close();
    }
}
