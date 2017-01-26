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

import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

public class CurrencyDataDeserializerTest {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GsonHelper.currencyDataType, new CurrencyDataDeserializer())
            .setPrettyPrinting().create();

    @Test
    public void deserialize_SimpleCurrencyData_ValidCurrencyData() throws Exception {
        try (Reader reader = new FileReader(getClass().getResource("/currency-data.json").getFile())){
            Map<Integer, Currency> currencies = gson.fromJson(reader, GsonHelper.currencyDataType);

            Assert.assertTrue(currencies.get(1).getName().equals("Currency Name 1"));
            Assert.assertTrue(currencies.get(1).getIconPath().equals("Icon Path 1"));

            Assert.assertTrue(currencies.get(2).getName().equals("Currency Name 2"));
            Assert.assertTrue(currencies.get(2).getIconPath().equals("Icon Path 2"));
        }
    }

}