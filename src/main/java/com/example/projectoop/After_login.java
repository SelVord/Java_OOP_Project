package com.example.projectoop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class After_login {
    @FXML
    private MenuButton profile_btn;
    @FXML
    private TextField search_txt;
    @FXML
    private ListView<Exam> exams_list;

    public void initialize() {
        List<Exam> exams = JavaPSQL.getAllExams();
        ObservableList<Exam> observableExams = FXCollections.observableArrayList(exams);
        exams_list.setItems(observableExams);

        exams_list.setOnMouseClicked(event -> {
            Exam selectedExam = exams_list.getSelectionModel().getSelectedItem();
            if (selectedExam != null) {
                openExamPage(selectedExam);
            }
        });
    }

    public void searchById() {
        List<Exam> exams = JavaPSQL.searchById(Integer.parseInt(search_txt.getText()));
        ObservableList<Exam> observableExams = FXCollections.observableArrayList(exams);
        exams_list.setItems(observableExams);
    }

    public void searchByTitle() {
        List<Exam> exams = JavaPSQL.searchByTitle(search_txt.getText());
        ObservableList<Exam> observableExams = FXCollections.observableArrayList(exams);
        exams_list.setItems(observableExams);
    }

    public void logout() throws IOException {
        HelloApplication m = new HelloApplication();
        m.change_scene("hello-view.fxml");
    }

    private void openExamPage(Exam exam) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("exam_page.fxml"));
            Parent root = loader.load();

            ExamPage controller = loader.getController();
            controller.initData(exam);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openUserExamsPage() {
        openUserExamsPage1();
        closeCurrentStage();
    }

    public void openUserExamsPage1() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user_exams.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openCompletedExams() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("completed_exams.fxml"));
            Parent root = loader.load();

            CompletedExams controller = loader.getController();

            controller.initialize();

            Stage stage = new Stage();
            stage.setTitle("Completed Exams");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeCurrentStage() {
        Stage currentStage = (Stage) profile_btn.getScene().getWindow();
        currentStage.close();
    }
}
