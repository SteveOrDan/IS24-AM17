package com.example.pf_soft_ing.jsonDeserializers.placeable_card;

import com.example.pf_soft_ing.card.ResourceType;
import com.example.pf_soft_ing.card.ResourceCard;
import com.example.pf_soft_ing.card.CardElementType;
import com.example.pf_soft_ing.card.corner.EmptyCorner;
import com.example.pf_soft_ing.card.side.Back;
import com.example.pf_soft_ing.card.side.Side;
import com.example.pf_soft_ing.jsonDeserializers.side.SideDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ResourceCardDeserializer implements JsonDeserializer<ResourceCard> {

    @Override
    public ResourceCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int points = jsonObject.get("points").getAsInt();
        int id = jsonObject.get("id").getAsInt();
        CardElementType elementType = CardElementType.cardElementTypeFromString(jsonObject.get("elementType").getAsString());

        JsonElement frontElement = jsonObject.get("front");

        SideDeserializer sideDeserializer = new SideDeserializer();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Side.class, sideDeserializer)
                .create();

        Side front = gson.fromJson(frontElement, Side.class);

        ResourceType resourceType = ResourceType.resourceTypeFromString(jsonObject.get("elementType").getAsString());
        Side back = new Back(new EmptyCorner(), new EmptyCorner(), new EmptyCorner(), new EmptyCorner(), new ArrayList<>(){{add(resourceType);}});

        return new ResourceCard(points, elementType, id, front, back);
    }
}
