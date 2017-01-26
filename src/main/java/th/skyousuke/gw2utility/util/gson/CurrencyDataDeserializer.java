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
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import th.skyousuke.gw2utility.datamodel.Currency;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CurrencyDataDeserializer implements JsonDeserializer<Map<Integer, Currency>> {

    @Override
    public Map<Integer, Currency> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject currenciesInJson = json.getAsJsonObject();
        final HashMap<Integer, Currency> currencies = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : currenciesInJson.entrySet()) {

            JsonObject currencyInJson = entry.getValue().getAsJsonObject();

            final int id = Integer.parseInt(entry.getKey());

            JsonPrimitive jsonPrimitive = currencyInJson.getAsJsonPrimitive("name");
            final String name = jsonPrimitive.isJsonNull() ? "" : jsonPrimitive.getAsString();

            jsonPrimitive = currencyInJson.getAsJsonPrimitive("iconPath");
            final String iconPath = jsonPrimitive.isJsonNull() ? "" : jsonPrimitive.getAsString();

            currencies.put(id, new Currency(id, name, iconPath));
        }
        return currencies;
    }
}
