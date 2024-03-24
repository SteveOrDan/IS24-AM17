module com.example.pf_soft_ing {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.pf_soft_ing to javafx.fxml;
    exports com.example.pf_soft_ing;
    exports com.example.pf_soft_ing.card;
    opens com.example.pf_soft_ing.card to javafx.fxml;
    exports com.example.pf_soft_ing.card.side;
    opens com.example.pf_soft_ing.card.side to javafx.fxml;
    exports com.example.pf_soft_ing.card.objectiveCards;
    opens com.example.pf_soft_ing.card.objectiveCards to javafx.fxml;
    exports com.example.pf_soft_ing.card.corner;
    opens com.example.pf_soft_ing.card.corner to javafx.fxml;
}