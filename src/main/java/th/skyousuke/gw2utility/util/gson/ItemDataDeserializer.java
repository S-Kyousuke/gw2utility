/*
 * Copyright 2017 Surasek Nusati <surasek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.skyousuke.gw2utility.util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemRarity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ItemDataDeserializer implements JsonDeserializer<Map<Integer, Item>> {

    @Override
    public Map<Integer, Item> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonObject itemsInJson = json.getAsJsonObject();
        final HashMap<Integer, Item> items = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : itemsInJson.entrySet()) {

            JsonObject itemInJson = entry.getValue().getAsJsonObject();

            final int id = Integer.parseInt(entry.getKey());

            JsonPrimitive jsonPrimitive = itemInJson.getAsJsonPrimitive("name");
            final String name = jsonPrimitive.isJsonNull() ? "" : jsonPrimitive.getAsString();

            final ItemRarity rarity = ItemRarity.fromString(itemInJson.getAsJsonPrimitive("rarity").getAsString());

            jsonPrimitive = itemInJson.getAsJsonPrimitive("iconPath");
            final String iconPath = jsonPrimitive.isJsonNull() ? "" : jsonPrimitive.getAsString();

            items.put(id, new Item(id, name, rarity, iconPath));
        }
        return items;
    }
}
