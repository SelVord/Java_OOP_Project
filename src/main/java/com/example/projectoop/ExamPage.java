package com.example.projectoop;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ExamPage {
    @FXML
    private Label examTitleLabel;
    @FXML
    private VBox questionsVBox;
    @FXML
    private Button cancel;

    private int userId;
    private int examId;

    public void initData(Exam exam) {
        this.examId = exam.getId();
        this.userId = JavaPSQL.getUserId(AppData.getCurrentUser());
        examTitleLabel.setText("Exam Title: " + exam.getTitle());
        fetchQuestionsFromDatabase(exam.getId());
    }

    private void fetchQuestionsFromDatabase(int examId) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/Java_OOP";
        String username = "postgres";
        String password = "123456";

        String query = "SELECT question FROM questions WHERE exam_id = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, examId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                processResultSetData(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void processResultSetData(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                String questionText = resultSet.getString("question");
                addQuestionUI(questionText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addQuestionUI(String questionText) {
        HBox questionHBox = new HBox(10);

        Label questionLabel = new Label(questionText);
        questionLabel.setMinWidth(300);
        questionLabel.setMaxWidth(Double.MAX_VALUE);
        questionLabel.setWrapText(true);
        questionLabel.setMinHeight(50);
        questionLabel.setMaxHeight(Double.MAX_VALUE);

        TextField answerTextField = new TextField();
        answerTextField.setMinWidth(200);
        answerTextField.setMaxWidth(Double.MAX_VALUE);

        questionHBox.getChildren().addAll(questionLabel, answerTextField);

        questionsVBox.getChildren().add(questionHBox);
    }


    @FXML
    private void handleSubmit() {
        Stage currentStage = (Stage) cancel.getScene().getWindow();

        for (Node node : questionsVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox questionHBox = (HBox) node;

                Label questionLabel = (Label) questionHBox.getChildren().get(0);
                String questionText = questionLabel.getText();

                TextField answerTextField = (TextField) questionHBox.getChildren().get(1);
                String userAnswer = answerTextField.getText();

                saveAnswerToDatabase(userId, examId, questionText, userAnswer);
            }
        }

        currentStage.close();
    }

    private void saveAnswerToDatabase(int userId, int examId, String questionText, String userAnswer) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/Java_OOP";
        String username = "postgres";
        String password = "123456";

        String insertQuery = "INSERT INTO answers (user_id, question_id, answer) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            int questionId = getQuestionIdFromDatabase(examId, questionText);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, questionId);
            preparedStatement.setString(3, userAnswer);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getQuestionIdFromDatabase(int examId, String questionText) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/Java_OOP";
        String username = "postgres";
        String password = "123456";

        String selectQuery = "SELECT id FROM questions WHERE exam_id = ? AND question = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setInt(1, examId);
            preparedStatement.setString(2, questionText);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @FXML
    private void cancel() {
        Stage currentStage = (Stage) cancel.getScene().getWindow();

        currentStage.close();
    }
}
