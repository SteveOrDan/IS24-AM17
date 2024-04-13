package com.example.pf_soft_ing.deserializers.placeable_card;

import com.example.pf_soft_ing.card.StarterCard;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.deserializers.side.SideDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;

public class StarterCardDeserializer implements JsonDeserializer<StarterCard> {
    @Override
    public StarterCard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        int id = jsonObject.get("id").getAsInt();

        JsonElement frontElement = jsonObject.get("front");
        JsonElement backElement = jsonObject.get("back");

        SideDeserializer sideDeserializer = new SideDeserializer();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Side.class, sideDeserializer)
                .create();

        Side front = gson.fromJson(frontElement, Side.class);
        Side back = gson.fromJson(backElement, Side.class);

        return new StarterCard(id, front, back);
    }
}
