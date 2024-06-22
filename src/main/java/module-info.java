module com.example.pf_soft_ing {
    requires javafx.controls;

    requires com.google.gson;
    requires java.rmi;

    exports com.example.pf_soft_ing.card;
    exports com.example.pf_soft_ing.card.side;
    exports com.example.pf_soft_ing.card.objectiveCards;
    exports com.example.pf_soft_ing.card.corner;
    exports com.example.pf_soft_ing.jsonDeserializers.side;
    exports com.example.pf_soft_ing.jsonDeserializers.objective_card;
    exports com.example.pf_soft_ing.jsonDeserializers.placeable_card;
    exports com.example.pf_soft_ing.jsonDeserializers.corner;

    exports com.example.pf_soft_ing.mvc.model.player;
    exports com.example.pf_soft_ing.card.decks;
    exports com.example.pf_soft_ing.app;

    exports com.example.pf_soft_ing.network.messages;
    exports com.example.pf_soft_ing.network.messages.answers;
    exports com.example.pf_soft_ing.network.messages.requests;
    exports com.example.pf_soft_ing.network.server;
    exports com.example.pf_soft_ing.network.client;
    exports com.example.pf_soft_ing.exceptions.cards;
    exports com.example.pf_soft_ing.exceptions.player;
    exports com.example.pf_soft_ing.exceptions.match;
    exports com.example.pf_soft_ing.utils;
    exports com.example.pf_soft_ing.mvc.view;
    exports com.example.pf_soft_ing.network.server.rmi;
    exports com.example.pf_soft_ing.network.server.socket;
    exports com.example.pf_soft_ing.network.client.rmi;
    exports com.example.pf_soft_ing.network.client.socket;
    exports com.example.pf_soft_ing.mvc.model.game;
    exports com.example.pf_soft_ing.mvc.controller;
}