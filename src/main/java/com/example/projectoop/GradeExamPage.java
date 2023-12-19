package com.example.projectoop;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class GradeExamPage {
    @FXML
    private Label titleLabel;

    @FXML
    private VBox questionsBox;

    @FXML
    private Button backButton;

    private Stage parentStage;

    private User selectedUserId = JavaPSQL.getSelecterUser();

    private Exam selectedExam = JavaPSQL.getSelecterExam();

    private List<Question> questions;
    private List<String> answers;

    public void setQuestionsAndAnswers(List<Question> questions, List<String> answers) {
        this.questions = questions;
        this.answers = answers;
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void initialize() {
        System.out.println(selectedExam);
        if (selectedExam != null) {
            titleLabel.setText(selectedExam.getTitle());

            List<Question> questions = JavaPSQL.getQuestionsForExam(selectedExam.getId());
            List<String> answers = JavaPSQL.getAnswersForUserAndExam(selectedUserId.getId(), selectedExam.getId());

            setQuestionsAndAnswers(questions, answers);

            for (int i = 0; i < questions.size(); i++) {
                Label questionLabel = new Label("Question " + (i + 1));
                Label answerTextField = new Label("Answer: " + answers.get(i));
                TextField gradeTextField = new TextField();

                VBox questionBox = new VBox(questionLabel, answerTextField, gradeTextField);
                questionsBox.getChildren().add(questionBox);
            }
        } else {
            System.out.println("selectedExam is null! 1");
        }
    }

    @FXML
    private void handleBack() {
        closeCurrentStage();
    }

    @FXML
    private void handleFinish() {
        int userId = selectedUserId.getId();
        int examId = selectedExam.getId();

        float totalGrade = 0.0f;

        for (int i = 0; i < questionsBox.getChildren().size(); i++) {
            VBox questionBox = (VBox) questionsBox.getChildren().get(i);
            Label answerLabel = (Label) questionBox.getChildren().get(1);
            TextField gradeTextField = (TextField) questionBox.getChildren().get(2);

            float grade = 0.0f;

            try {
                grade = Float.parseFloat(gradeTextField.getText());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            totalGrade += grade;

            JavaPSQL.addQuestionResult(userId, questions.get(i).getId(), grade);
        }

        float averageGrade = totalGrade / questionsBox.getChildren().size();

        averageGrade = (float) (Math.round(averageGrade * 100.0) / 100.0);

        JavaPSQL.addResult(userId, examId, averageGrade);

        closeCurrentStage();
    }

    private void closeCurrentStage() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
    }
}
