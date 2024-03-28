package com.example.pf_soft_ing.deserializers;

import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ObjectiveCardDeserializer implements JsonDeserializer<ObjectiveCard> {
    @Override
    public ObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}
