package com.example.pf_soft_ing.deserializers.objective_card;

import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.objectiveCards.TLBRDiagonalObjectiveCard;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TLBRDiagonalObjectiveCardDeserializer implements JsonDeserializer<TLBRDiagonalObjectiveCard> {
    @Override
    public TLBRDiagonalObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new TLBRDiagonalObjectiveCard(
                jsonElement.getAsJsonObject().get("id").getAsInt(),
                CardElementType.cardElementTypeFromString(jsonElement.getAsJsonObject().get("elementType").getAsString())
        );
    }
}
