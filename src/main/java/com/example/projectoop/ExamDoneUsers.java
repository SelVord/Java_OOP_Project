package com.example.projectoop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ExamDoneUsers {
    @FXML
    private ListView<User> usersListView;
    private Exam selectedExam;
    private Stage parentStage;

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void setSelectedExam(Exam exam) {
        this.selectedExam = exam;
        initializeListView();
    }

    private void initializeListView() {
        List<User> usersToGrade = JavaPSQL.getUsersToGrade(selectedExam);
        ObservableList<User> observableUsers = FXCollections.observableArrayList(usersToGrade);

        usersListView.setItems(observableUsers);

        usersListView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getName());
                }
            }
        });
    }

    @FXML
    private void handleGrade() {
        User selectedUser = usersListView.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {

            JavaPSQL.setSelectedUserAndExam(selectedUser, selectedExam);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GradeExamPage.fxml"));
                Parent root = loader.load();

                GradeExamPage controller = loader.getController();
                controller.setParentStage(parentStage);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void handleBack() {
        Stage currentStage = (Stage) usersListView.getScene().getWindow();
        currentStage.close();
    }
}
