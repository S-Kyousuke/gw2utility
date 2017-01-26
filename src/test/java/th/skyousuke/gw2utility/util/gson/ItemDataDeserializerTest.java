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

import java.io.FileReader;
import java.io.Reader;
import java.util.Map;


public class ItemDataDeserializerTest {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(GsonHelper.itemDataType, new ItemDataDeserializer())
            .setPrettyPrinting().create();

    @Test
    public void deserialize_SimpleItemData_ValidItemData() throws Exception {
        try (Reader reader = new FileReader(getClass().getResource("/item-data.json").getFile())){
            Map<Integer, Item> items = gson.fromJson(reader, GsonHelper.itemDataType);

            Assert.assertTrue(items.get(0).getName().equals("Item Name 0"));
            Assert.assertTrue(items.get(0).getRarity() == ItemRarity.BASIC);
            Assert.assertTrue(items.get(0).getIconPath().equals("Icon Path 0"));

            Assert.assertTrue(items.get(1).getName().equals("Item Name 1"));
            Assert.assertTrue(items.get(1).getRarity() == ItemRarity.RARE);
            Assert.assertTrue(items.get(1).getIconPath().equals("Icon Path 1"));

            Assert.assertTrue(items.get(2).getName().equals("Item Name 2"));
            Assert.assertTrue(items.get(2).getRarity() == ItemRarity.EXOTIC);
            Assert.assertTrue(items.get(2).getIconPath().equals("Icon Path 2"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deserialize_WrongItemData_ThrowException() throws Exception {
        try (Reader reader = new FileReader(getClass().getResource("/wrong-item-data.json").getFile())){
            gson.fromJson(reader, GsonHelper.itemDataType);
        }
    }

    @Test(expected = NullPointerException.class)
    public void deserialize_IncompleteItemData_ThrowException() throws Exception {
        try (Reader reader = new FileReader(getClass().getResource("/incomplete-item-data.json").getFile())){
            gson.fromJson(reader, GsonHelper.itemDataType);
        }
    }

}