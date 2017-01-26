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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;
import org.junit.Test;
import th.skyousuke.gw2utility.datamodel.Currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyDataSerializerTest {


    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GsonHelper.itemDataType, new CurrencyDataDeserializer())
            .registerTypeAdapter(GsonHelper.itemDataType, new CurrencyDataSerializer())
            .setPrettyPrinting().create();

    @Test
    public void serialize_SimpleItemData_ValidJson() throws Exception {
        final Map<Integer, Currency> currencies = new HashMap<>();
        currencies.put(0, new Currency(0, "Currency Name 0", "Icon Path 0"));
        currencies.put(1, new Currency(1, "Currency Name 1", "Icon Path 1"));
        currencies.put(2, new Currency(2, "Currency Name 2", "Icon Path 2"));

        final String json = gson.toJson(currencies, GsonHelper.currencyDataType);
        final Map<Integer, Currency> deserializedCurrencies = gson.fromJson(json, GsonHelper.currencyDataType);

        Assert.assertEquals(currencies, deserializedCurrencies);
    }

}