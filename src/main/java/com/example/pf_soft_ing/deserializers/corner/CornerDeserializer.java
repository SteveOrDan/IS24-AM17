package com.example.pf_soft_ing.deserializers.corner;

import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.corner.HiddenCorner;
import com.example.pf_soft_ing.card.corner.ResourceCorner;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CornerDeserializer implements JsonDeserializer<CardCorner> {
    @Override
    public CardCorner deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String cornerType = jsonObject.get("cornerType").getAsString();

        switch (cornerType){
            case "ResourceCorner":
                ResourceCornerDeserializer resourceCornerDeserializer = new ResourceCornerDeserializer();

                Gson rGson = new GsonBuilder()
                        .registerTypeAdapter(CardCorner.class, resourceCornerDeserializer)
                        .create();

                return rGson.fromJson(jsonObject, ResourceCorner.class);

            case "EmptyCorner":
                EmptyCornerDeserializer emptyCornerDeserializer = new EmptyCornerDeserializer();

                Gson eGson = new GsonBuilder()
                        .registerTypeAdapter(CardCorner.class, emptyCornerDeserializer)
                        .create();

                return eGson.fromJson(jsonObject, EmptyCorner.class);

            case "HiddenCorner":
                HiddenCornerDeserializer hiddenCornerDeserializer = new HiddenCornerDeserializer();

                Gson hGson = new GsonBuilder()
                        .registerTypeAdapter(CardCorner.class, hiddenCornerDeserializer)
                        .create();

                return hGson.fromJson(jsonObject, HiddenCorner.class);

            default:
                return null;
        }
    }
}
