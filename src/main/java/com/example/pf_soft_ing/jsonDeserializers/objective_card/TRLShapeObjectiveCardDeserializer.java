package com.example.pf_soft_ing.jsonDeserializers.objective_card;

import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.objectiveCards.TRLShapeObjectiveCard;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TRLShapeObjectiveCardDeserializer implements JsonDeserializer<TRLShapeObjectiveCard> {
    @Override
    public TRLShapeObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new TRLShapeObjectiveCard(
                jsonElement.getAsJsonObject().get("id").getAsInt(),
                CardElementType.cardElementTypeFromString(jsonElement.getAsJsonObject().get("mainElementType").getAsString()),
                CardElementType.cardElementTypeFromString(jsonElement.getAsJsonObject().get("secondaryElementType").getAsString())
        );
    }
}
