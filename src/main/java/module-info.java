module com.example.pf_soft_ing {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;
    requires java.desktop;

    opens com.example.pf_soft_ing to javafx.fxml;
    exports com.example.pf_soft_ing.card;
    opens com.example.pf_soft_ing.card to javafx.fxml;
    exports com.example.pf_soft_ing.card.side;
    opens com.example.pf_soft_ing.card.side to javafx.fxml;
    exports com.example.pf_soft_ing.card.objectiveCards;
    opens com.example.pf_soft_ing.card.objectiveCards to javafx.fxml;
    exports com.example.pf_soft_ing.card.corner;
    opens com.example.pf_soft_ing.card.corner to javafx.fxml;
    exports com.example.pf_soft_ing.deserializers.side;
    opens com.example.pf_soft_ing.deserializers.side to javafx.fxml;
    exports com.example.pf_soft_ing.deserializers.objective_card;
    opens com.example.pf_soft_ing.deserializers.objective_card to javafx.fxml;
    exports com.example.pf_soft_ing.deserializers.placeable_card;
    opens com.example.pf_soft_ing.deserializers.placeable_card to javafx.fxml;
    exports com.example.pf_soft_ing.deserializers.corner;
    opens com.example.pf_soft_ing.deserializers.corner to javafx.fxml;
    exports com.example.pf_soft_ing.network.ServerConnection;
    opens com.example.pf_soft_ing.network.ServerConnection to javafx.fxml;
    exports com.example.pf_soft_ing.observerPattern;

    exports com.example.pf_soft_ing.exceptions;
    exports com.example.pf_soft_ing.network;
    opens com.example.pf_soft_ing.network to javafx.fxml;
    exports com.example.pf_soft_ing.network.RMI;
    opens com.example.pf_soft_ing.network.RMI to javafx.fxml;
    exports com.example.pf_soft_ing.game;
    opens com.example.pf_soft_ing.game to javafx.fxml;
    exports com.example.pf_soft_ing.deserializers;
    opens com.example.pf_soft_ing.deserializers to javafx.fxml;
    exports com.example.pf_soft_ing.player;
    opens com.example.pf_soft_ing.player to javafx.fxml;
    exports com.example.pf_soft_ing.card.decks;
    opens com.example.pf_soft_ing.card.decks to javafx.fxml;
    exports com.example.pf_soft_ing.app;
    opens com.example.pf_soft_ing.app to javafx.fxml;

    exports com.example.pf_soft_ing.app.GUI;
    opens com.example.pf_soft_ing.app.GUI to javafx.fxml;
    exports com.example.pf_soft_ing.ServerConnection;
    opens com.example.pf_soft_ing.ServerConnection to javafx.fxml;
}