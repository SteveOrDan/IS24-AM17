package com.example.pf_soft_ing.app;

import com.example.pf_soft_ing.network.client.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;

public class GUIApp extends Application {

    private static String ip;

    public static void main(String[] args) {
        ip = args[0];

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        new GUIView(stage, ip);
    }
}
