module com.example.pf_soft_ing {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;
    requires java.smartcardio;

    exports com.example.pf_soft_ing.card;
    opens com.example.pf_soft_ing.card to javafx.fxml;
    exports com.example.pf_soft_ing.card.side;
    opens com.example.pf_soft_ing.card.side to javafx.fxml;
    exports com.example.pf_soft_ing.card.objectiveCards;
    opens com.example.pf_soft_ing.card.objectiveCards to javafx.fxml;
    exports com.example.pf_soft_ing.card.corner;
    opens com.example.pf_soft_ing.card.corner to javafx.fxml;
    exports com.example.pf_soft_ing.JSONDeserializers.side;
    opens com.example.pf_soft_ing.JSONDeserializers.side to javafx.fxml;
    exports com.example.pf_soft_ing.JSONDeserializers.objective_card;
    opens com.example.pf_soft_ing.JSONDeserializers.objective_card to javafx.fxml;
    exports com.example.pf_soft_ing.JSONDeserializers.placeable_card;
    opens com.example.pf_soft_ing.JSONDeserializers.placeable_card to javafx.fxml;
    exports com.example.pf_soft_ing.JSONDeserializers.corner;
    opens com.example.pf_soft_ing.JSONDeserializers.corner to javafx.fxml;

    exports com.example.pf_soft_ing.game;
    opens com.example.pf_soft_ing.game to javafx.fxml;
    exports com.example.pf_soft_ing.MVC.model.player;
    opens com.example.pf_soft_ing.MVC.model.player to javafx.fxml;
    exports com.example.pf_soft_ing.card.decks;
    opens com.example.pf_soft_ing.card.decks to javafx.fxml;
    exports com.example.pf_soft_ing.app;
    opens com.example.pf_soft_ing.app to javafx.fxml;

    exports com.example.pf_soft_ing.network.messages;
    opens com.example.pf_soft_ing.network.messages to javafx.fxml;
    exports com.example.pf_soft_ing.network.messages.answers;
    opens com.example.pf_soft_ing.network.messages.answers to javafx.fxml;
    exports com.example.pf_soft_ing.network.messages.requests;
    opens com.example.pf_soft_ing.network.messages.requests to javafx.fxml;
    exports com.example.pf_soft_ing.network.server;
    opens com.example.pf_soft_ing.network.server to javafx.fxml;
    exports com.example.pf_soft_ing.network.client;
    opens com.example.pf_soft_ing.network.client to javafx.fxml;
    exports com.example.pf_soft_ing.exceptions.cards;
    exports com.example.pf_soft_ing.exceptions.player;
    exports com.example.pf_soft_ing.exceptions.match;
    exports com.example.pf_soft_ing.utils;
    opens com.example.pf_soft_ing.utils to javafx.fxml;
    exports com.example.pf_soft_ing.MVC.view;
    opens com.example.pf_soft_ing.MVC.view to javafx.fxml;
    exports com.example.pf_soft_ing.network.server.RMI;
    opens com.example.pf_soft_ing.network.server.RMI to javafx.fxml;
    exports com.example.pf_soft_ing.network.server.socket;
    opens com.example.pf_soft_ing.network.server.socket to javafx.fxml;
    exports com.example.pf_soft_ing.network.client.RMI;
    opens com.example.pf_soft_ing.network.client.RMI to javafx.fxml;
    exports com.example.pf_soft_ing.network.client.socket;
    opens com.example.pf_soft_ing.network.client.socket to javafx.fxml;
    exports com.example.pf_soft_ing.MVC.model.game;
    opens com.example.pf_soft_ing.MVC.model.game to javafx.fxml;
    exports com.example.pf_soft_ing.MVC.controller;
    opens com.example.pf_soft_ing.MVC.controller to javafx.fxml;
}