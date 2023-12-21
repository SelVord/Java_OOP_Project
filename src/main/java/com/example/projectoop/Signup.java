package com.example.projectoop;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Signup {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password1;
    @FXML
    private PasswordField password2;
    @FXML
    private Label wrong_psw;

    HelloApplication m = new HelloApplication();

    public void signup_func() throws IOException {
        if (username.getText().isEmpty() || password1.getText().isEmpty() || !password1.getText().equals(password2.getText())) {
            wrong_psw.setText("Wrong username or password");
        } else if (!JavaPSQL.checkIfUserExist(username.getText())) {
            JavaPSQL.registerUser(username.getText(), password1.getText());
            m.change_scene("hello-view.fxml");
        } else {
            wrong_psw.setText("User already exist");
        }
    }

    public void back_func() throws IOException {
        m.change_scene("hello-view.fxml");
    }
}
