package com.example.pf_soft_ing.deserializers.corner;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ResourceCornerDeserializer implements JsonDeserializer<ResourceCorner> {
    @Override
    public ResourceCorner deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new ResourceCorner(ResourceType.resourceTypeFromString(jsonElement.getAsString()));
    }
}
