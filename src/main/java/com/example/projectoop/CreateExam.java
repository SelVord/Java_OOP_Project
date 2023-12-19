package com.example.projectoop;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CreateExam {

    @FXML
    private VBox questionsVBox;
    @FXML
    private TextField examTitleTextField;

    private List<TextField> questionTextFields = new ArrayList<>();

    @FXML
    private void handleBack() {
        closeCurrentStage();
    }

    @FXML
    private void addQuestion() {
        Label questionLabel = new Label("Question " + (questionTextFields.size() + 1));
        TextField questionTextField = new TextField();
        questionTextFields.add(questionTextField);

        HBox questionHBox = new HBox(10);
        questionHBox.getChildren().addAll(questionLabel, questionTextField);

        questionsVBox.getChildren().add(questionHBox);
    }

    @FXML
    private void handleCreate() {
        String examTitle = examTitleTextField.getText();

        int examId = JavaPSQL.createExam(AppData.getCurrentUser(), examTitle);

        for (int i = 0; i < questionTextFields.size(); i++) {
            String questionText = questionTextFields.get(i).getText();
            JavaPSQL.createQuestion(examId, questionText);
        }

        closeCurrentStage();
    }

    private void closeCurrentStage() {
        Stage currentStage = (Stage) questionsVBox.getScene().getWindow();
        currentStage.close();
    }
}
