package com.example.pf_soft_ing.jsonDeserializers.side;

import com.example.pf_soft_ing.card.corner.CardCorner;
import com.example.pf_soft_ing.card.side.Front;
import com.example.pf_soft_ing.jsonDeserializers.corner.CornerDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;

public class FrontDeserializer implements JsonDeserializer<Front> {
    @Override
    public Front deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        JsonElement BLCornerElement = jsonObject.get("BLCorner");
        JsonElement BRCornerElement = jsonObject.get("BRCorner");
        JsonElement TLCornerElement = jsonObject.get("TLCorner");
        JsonElement TRCornerElement = jsonObject.get("TRCorner");

        CornerDeserializer cornerDeserializer = new CornerDeserializer();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CardCorner.class, cornerDeserializer)
                .create();

        CardCorner BLCorner = gson.fromJson(BLCornerElement, CardCorner.class);
        CardCorner BRCorner = gson.fromJson(BRCornerElement, CardCorner.class);
        CardCorner TLCorner = gson.fromJson(TLCornerElement, CardCorner.class);
        CardCorner TRCorner = gson.fromJson(TRCornerElement, CardCorner.class);

        return new Front(BLCorner, BRCorner, TLCorner, TRCorner);
    }
}
