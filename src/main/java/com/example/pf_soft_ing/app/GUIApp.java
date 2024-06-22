package com.example.pf_soft_ing.app;

import com.example.pf_soft_ing.mvc.view.GUIView;
import javafx.application.Application;
import javafx.stage.Stage;

public class GUIApp extends Application {

    private static String ip;
    private static String connectionType;

    public static void main(String[] args) {
        ip = args[0];
        connectionType = args[1];

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        new GUIView(stage, ip, connectionType);
    }
}
