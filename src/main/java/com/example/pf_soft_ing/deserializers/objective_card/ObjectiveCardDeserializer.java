package com.example.pf_soft_ing.deserializers.objective_card;

import com.example.pf_soft_ing.card.objectiveCards.ObjectiveCard;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ObjectiveCardDeserializer implements JsonDeserializer<ObjectiveCard> {
    @Override
    public ObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String objectiveType = jsonObject.get("objectiveType").getAsString();

        return switch (objectiveType) {
            case "ResourcesCountObjectiveCard" ->
                    new ResourcesCountObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            case "TrinityObjectiveCard" ->
                    new TrinityObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            case "TRBLDiagonalObjectiveCard" ->
                    new TRBLDiagonalObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            case "TLBRDiagonalObjectiveCard" ->
                    new TLBRDiagonalObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            case "BLLShapeObjectiveCard" ->
                    new BLLShapeObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            case "BRLShapeObjectiveCard" ->
                    new BRLShapeObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            case "TLLShapeObjectiveCard" ->
                    new TLLShapeObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            case "TRLShapeObjectiveCard" ->
                    new TRLShapeObjectiveCardDeserializer().deserialize(jsonElement, type, jsonDeserializationContext);

            default -> throw new JsonParseException("Unknown objective type: " + objectiveType);
        };
    }
}
