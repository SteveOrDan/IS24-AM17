package com.example.pf_soft_ing.deserializers.placeable_card;

import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.deserializers.side.SideDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ResourceCardDeserializer implements JsonDeserializer<ResourceCard> {

    @Override
    public ResourceCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int points = jsonObject.get("points").getAsInt();
        int id = jsonObject.get("id").getAsInt();
        CardElementType elementType = CardElementType.cardElementTypeFromString(jsonObject.get("elementType").getAsString());

        JsonElement frontElement = jsonObject.get("front");
        JsonElement backElement = jsonObject.get("back");

        SideDeserializer sideDeserializer = new SideDeserializer();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Side.class, sideDeserializer)
                .create();

        Side front = gson.fromJson(frontElement, Side.class);
        Side back = gson.fromJson(backElement, Side.class);

        return new ResourceCard(points, elementType, id, front, back);
    }
}
