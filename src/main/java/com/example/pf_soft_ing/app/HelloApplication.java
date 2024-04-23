package com.example.pf_soft_ing.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();

        // Create buttons with label
        Button button1 = new Button("Button 1");
        Button button2 = new Button("Button 2");

        // Set actions event for each button with parameter
        button1.setOnAction(event -> buttonClick("Parameter 1"));
        button2.setOnAction(event -> buttonClick("Parameter 2"));

        // Create view
        VBox root = new VBox(10);
        root.getChildren().addAll(button1, button2);

        // Create scene and set it in the stage
        Scene scene = new Scene(root, 200, 150);
        stage.setScene(scene);
        stage.setTitle("JavaFX test window");
        stage.show();
    }

    private void buttonClick(String parameter){
        System.out.println("Clicked button with parameter: " + parameter);
    }

    public static void main(String[] args) {
        launch();
    }
}
