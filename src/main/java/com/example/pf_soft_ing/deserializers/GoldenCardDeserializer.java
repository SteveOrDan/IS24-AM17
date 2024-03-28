package com.example.pf_soft_ing.deserializers;

import com.example.pf_soft_ing.ResourceType;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.GoldenCard;
import com.example.pf_soft_ing.card.side.Side;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;

public class GoldenCardDeserializer implements JsonDeserializer<GoldenCard> {
    @Override
    public GoldenCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int points = jsonObject.get("points").getAsInt();
        int id = jsonObject.get("id").getAsInt();
        CardElementType elementType = CardElementType.stringToCardElementType(jsonObject.get("elementType").getAsString());
        boolean isPointPerResource = jsonObject.get("isPointPerResource").getAsBoolean();
        ResourceType pointPerResourceRes = ResourceType.resourceTypeFromString(jsonObject.get("pointPerResourceRes").getAsString());

        HashMap<ResourceType, Integer> requiredResources = jsonObject.get("requiredResources").getAsJsonObject().entrySet().stream()
                .collect(HashMap::new, (map, entry) -> map.put(ResourceType.resourceTypeFromString(entry.getKey()), entry.getValue().getAsInt()), HashMap::putAll);

        JsonElement frontElement = jsonObject.get("front");
        JsonElement backElement = jsonObject.get("back");

        SideDeserializer sideDeserializer = new SideDeserializer();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Side.class, sideDeserializer)
                .create();

        Side front = gson.fromJson(frontElement, Side.class);
        Side back = gson.fromJson(backElement, Side.class);

        return new GoldenCard(elementType, id, front, back, points, requiredResources, isPointPerResource,pointPerResourceRes );
    }
}
