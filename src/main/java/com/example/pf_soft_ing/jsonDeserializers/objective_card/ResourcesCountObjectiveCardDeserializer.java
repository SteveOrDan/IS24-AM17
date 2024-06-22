package com.example.pf_soft_ing.jsonDeserializers.objective_card;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.objectiveCards.ResourcesCountObjectiveCard;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ResourcesCountObjectiveCardDeserializer implements JsonDeserializer<ResourcesCountObjectiveCard> {
    @Override
    public ResourcesCountObjectiveCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ResourcesCountObjectiveCard(
                jsonElement.getAsJsonObject().get("id").getAsInt(),
                ResourceType.resourceTypeFromString(jsonElement.getAsJsonObject().get("resourceType").getAsString()),
                jsonElement.getAsJsonObject().get("requiredResourceCount").getAsInt()
        );
    }
}
