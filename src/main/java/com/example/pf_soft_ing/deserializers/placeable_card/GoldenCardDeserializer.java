package com.example.pf_soft_ing.deserializers.placeable_card;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.GoldenCard;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.deserializers.side.SideDeserializer;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GoldenCardDeserializer implements JsonDeserializer<GoldenCard> {
    @Override
    public GoldenCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int points = jsonObject.get("points").getAsInt();
        int id = jsonObject.get("id").getAsInt();
        CardElementType elementType = CardElementType.cardElementTypeFromString(jsonObject.get("elementType").getAsString());
        boolean isPointPerResource = jsonObject.get("isPointPerResource").getAsBoolean();

        ResourceType pointPerResourceRes = null;

        if (isPointPerResource) {
            pointPerResourceRes = ResourceType.resourceTypeFromString(jsonObject.get("pointPerResourceRes").getAsString());
        }

        JsonElement requiredResourcesElement = jsonObject.get("requiredResources");

        Gson mapGson = new Gson();

        Type mapType = new TypeToken<Map<ResourceType, Integer>>() {}.getType();
        HashMap<ResourceType, Integer> requiredResources = mapGson.fromJson(requiredResourcesElement, mapType);

        JsonElement frontElement = jsonObject.get("front");
        // JsonElement backElement = jsonObject.get("back");

        SideDeserializer sideDeserializer = new SideDeserializer();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Side.class, sideDeserializer)
                .create();

        Side front = gson.fromJson(frontElement, Side.class);
        // Side back = gson.fromJson(backElement, Side.class);

        ResourceType resourceType = ResourceType.resourceTypeFromString(jsonObject.get("elementType").getAsString());
        Side back = new Back(new EmptyCorner(), new EmptyCorner(), new EmptyCorner(), new EmptyCorner(), new ArrayList<>(){{add(resourceType);}});

        return new GoldenCard(elementType, id, front, back, points, requiredResources, isPointPerResource,pointPerResourceRes );
    }
}
