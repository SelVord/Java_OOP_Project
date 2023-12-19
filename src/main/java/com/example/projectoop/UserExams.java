package com.example.projectoop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserExams {
    @FXML
    private ListView<Exam> userExamsList;

    private ObservableList<Exam> userExams = FXCollections.observableArrayList();

    public void initialize() {
        loadUserExams();
        userExamsList.setItems(userExams);
    }

    private void loadUserExams() {
        List<Exam> exams = JavaPSQL.getUserExams(AppData.getCurrentUser());
        userExams.setAll(exams);
    }

    @FXML
    private void handleCreateExam() {
    }

    @FXML
    private void handleCheckExam() {
        Exam selectedExam = userExamsList.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            openExamDoneUsersPage(selectedExam);
        }
    }

    private void openCreateExamPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create_exam.fxml"));
            Parent root = loader.load();

            CreateExam createExamController = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openExamDoneUsersPage(Exam exam) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("exam_done_users.fxml"));
            Parent root = loader.load();

            ExamDoneUsers controller = loader.getController();
            controller.setSelectedExam(exam);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleBack() {
        openPreviousPage();
        closeCurrentStage();
    }

    private void openPreviousPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("after_login.fxml"));
            Parent root = loader.load();

            After_login controller = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage currentStage = (Stage) userExamsList.getScene().getWindow();
        currentStage.close();
    }
}
