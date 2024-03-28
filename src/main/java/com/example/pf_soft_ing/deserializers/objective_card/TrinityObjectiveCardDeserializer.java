package com.example.pf_soft_ing.deserializers.objective_card;

import com.example.pf_soft_ing.card.objectiveCards.TrinityObjectiveCard;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TrinityObjectiveCardDeserializer implements JsonDeserializer<TrinityObjectiveCard> {
    @Override
    public TrinityObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new TrinityObjectiveCard(jsonElement.getAsJsonObject().get("id").getAsInt());
    }
}
