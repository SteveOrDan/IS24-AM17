package com.example.pf_soft_ing.JSONDeserializers.side;

import com.example.pf_soft_ing.card.side.Side;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SideDeserializer implements JsonDeserializer<Side> {
    @Override
    public Side deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String sideType = jsonObject.get("sideType").getAsString();

        switch (sideType){
            case "Front":
                FrontDeserializer frontDeserializer = new FrontDeserializer();

                Gson fGson = new GsonBuilder()
                        .registerTypeAdapter(Side.class, frontDeserializer)
                        .create();

                return fGson.fromJson(jsonObject, Side.class);

            case "Back":
                BackDeserializer backDeserializer = new BackDeserializer();

                Gson bGson = new GsonBuilder()
                        .registerTypeAdapter(Side.class, backDeserializer)
                        .create();

                return bGson.fromJson(jsonObject, Side.class);

            default:
                return null;
        }
    }
}
