package com.example.pf_soft_ing.deserializers.side;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.deserializers.corner.CornerDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BackDeserializer implements JsonDeserializer<Back> {
    @Override
    public Back deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonElement BLCornerElement = jsonObject.get("BLCorner");
        JsonElement BRCornerElement = jsonObject.get("BRCorner");
        JsonElement TLCornerElement = jsonObject.get("TLCorner");
        JsonElement TRCornerElement = jsonObject.get("TRCorner");

        JsonElement permanentResourcesElement = jsonObject.get("permanentResources");

        CornerDeserializer cornerDeserializer = new CornerDeserializer();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CardCorner.class, cornerDeserializer)
                .create();

        CardCorner BLCorner = gson.fromJson(BLCornerElement, CardCorner.class);
        CardCorner BRCorner = gson.fromJson(BRCornerElement, CardCorner.class);
        CardCorner TLCorner = gson.fromJson(TLCornerElement, CardCorner.class);
        CardCorner TRCorner = gson.fromJson(TRCornerElement, CardCorner.class);

        List<ResourceType> permanentResources = new ArrayList<>();

        permanentResourcesElement.getAsJsonArray().forEach(jsonArrayElement -> permanentResources.add(ResourceType.resourceTypeFromString(jsonArrayElement.getAsString())));

        return new Back(BLCorner, BRCorner, TLCorner, TRCorner, permanentResources);
    }
}
