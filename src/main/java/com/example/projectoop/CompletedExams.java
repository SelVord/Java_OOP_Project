// Обновленный класс CompletedExams
package com.example.projectoop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CompletedExams {
    @FXML
    private ListView<String> completedExamsListView;

    @FXML
    private Button backButton;

    private ObservableList<String> completedExams = FXCollections.observableArrayList();

    public void initialize() {
        loadCompletedExams();
        completedExamsListView.setItems(completedExams);
    }

    private void loadCompletedExams() {
        List<String> exams = JavaPSQL.getCompletedExams(AppData.getCurrentUser());
        completedExams.setAll(exams);
    }

    @FXML
    private void handleBack() {
        closeCurrentStage();
    }

    @FXML
    private void handleDetails() {
        String selectedExamTitle = completedExamsListView.getSelectionModel().getSelectedItem();
        if (selectedExamTitle != null) {
            openExamDetailsPage(selectedExamTitle);
        }
    }

    private void openExamDetailsPage(String examTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("exam_details.fxml"));
            Parent root = loader.load();

            ExamDetails controller = loader.getController();
            controller.initData(examTitle);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Exam Details");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
    }
}
