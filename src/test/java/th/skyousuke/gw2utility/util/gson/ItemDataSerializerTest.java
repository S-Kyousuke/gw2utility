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
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemRarity;

import java.util.HashMap;
import java.util.Map;

public class ItemDataSerializerTest {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GsonHelper.itemDataType, new ItemDataDeserializer())
            .registerTypeAdapter(GsonHelper.itemDataType, new ItemDataSerializer())
            .setPrettyPrinting().create();

    @Test
    public void serialize_SimpleItemData_ValidJson() throws Exception {
        final Map<Integer, Item> items = new HashMap<>();
        items.put(0, new Item(0, "Item Name 0", ItemRarity.BASIC, "Icon Path 0", false, false, 0));
        items.put(1, new Item(1, "Item Name 1", ItemRarity.RARE, "Icon Path 1", false, false, 0));
        items.put(2, new Item(2, "Item Name 2", ItemRarity.EXOTIC, "Icon Path 2", false, false, 0));

        final String json = gson.toJson(items, GsonHelper.itemDataType);
        final Map<Integer, Item> deserializedItems = gson.fromJson(json, GsonHelper.itemDataType);

        Assert.assertEquals(items, deserializedItems);
    }

}