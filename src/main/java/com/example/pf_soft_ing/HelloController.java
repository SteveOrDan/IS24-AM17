package com.example.pf_soft_ing;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private int value;

    @FXML
    private Button button1;
    @FXML
    private Button button2;

    @FXML
    protected void onHelloButtonClick() {
        if (welcomeText.getText().isBlank()){
            welcomeText.setText("Welcome to JavaFX Application!");
            button1.setText("Goodbye!");
        }
        else
        {
            welcomeText.setText("");
            button1.setText("Hello!");
        }
    }
}
