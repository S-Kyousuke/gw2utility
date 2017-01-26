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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import th.skyousuke.gw2utility.datamodel.Currency;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CurrencyDataSerializer implements JsonSerializer<Map<Integer, Currency>> {

    @Override
    public JsonElement serialize(Map<Integer, Currency> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src != null) {
            Map<Integer, Currency> copySrc = new HashMap<>(src);
            JsonObject currencyJson = new JsonObject();
            for (Map.Entry<Integer, Currency> entry : copySrc.entrySet()) {
                Currency currency = entry.getValue();
                JsonObject currencyInJson = new JsonObject();
                currencyInJson.addProperty("name", currency.getName());
                currencyInJson.addProperty("iconPath", String.valueOf(currency.getIconPath()));
                final String currencyId = String.valueOf(entry.getKey());
                currencyJson.add(currencyId, currencyInJson);
            }
            return currencyJson;
        }
        return null;
    }

}
