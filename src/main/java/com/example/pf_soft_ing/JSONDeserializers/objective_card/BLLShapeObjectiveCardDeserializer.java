package com.example.pf_soft_ing.JSONDeserializers.objective_card;

import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.objectiveCards.BLLShapeObjectiveCard;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BLLShapeObjectiveCardDeserializer implements JsonDeserializer<BLLShapeObjectiveCard> {
    @Override
    public BLLShapeObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new BLLShapeObjectiveCard(
                jsonElement.getAsJsonObject().get("id").getAsInt(),
                CardElementType.cardElementTypeFromString(jsonElement.getAsJsonObject().get("mainElementType").getAsString()),
                CardElementType.cardElementTypeFromString(jsonElement.getAsJsonObject().get("secondaryElementType").getAsString())
        );
    }
}
