package com.example.projectoop;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label wrong_psw;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    HelloApplication m = new HelloApplication();

    @FXML
    public void login_func(ActionEvent event) throws IOException {
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            wrong_psw.setText("Wrong username or password");
        } else if (JavaPSQL.getUsers(username.getText(), password.getText())){
            AppData.setCurrentUser(username.getText());
            m.change_scene("after_login.fxml");
        }
        else {
            wrong_psw.setText("Wrong username or password");
        }
    }

    @FXML
    public void signup_func(ActionEvent event) throws IOException {
        m.change_scene("signup.fxml");
    }
}