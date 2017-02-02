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

import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import th.skyousuke.gw2utility.datamodel.Bag;
import th.skyousuke.gw2utility.datamodel.Character;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.Equipment;
import th.skyousuke.gw2utility.datamodel.EquipmentSlot;
import th.skyousuke.gw2utility.datamodel.Inventory;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemRarity;
import th.skyousuke.gw2utility.datamodel.ItemSlot;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.file.InvalidPathException;
import java.util.HashMap;
import java.util.Map;

public class GsonHelperTest {

    private static final String JSON_TEST_FILE_PATH = "test.json";

    @AfterClass
    public static void cleanUp() {
        FileUtils.deleteQuietly(new File(JSON_TEST_FILE_PATH));
    }

    @Test
    public void testPrivateConstructors() throws Exception {
        final Constructor<GsonHelper> constructor = GsonHelper.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void writeJsonToFile_JsonObject_True() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Name");
        jsonObject.addProperty("id", 12345);
        jsonObject.addProperty("grade", "3.99");
        Assert.assertTrue(GsonHelper.writeJsonToFile(jsonObject, JSON_TEST_FILE_PATH));
    }

    @Test
    public void writeJsonToFile_CustomObject_True() throws Exception {
        final ObservableList<EquipmentSlot> equipmentSlots = FXCollections.observableArrayList(
                new EquipmentSlot(1, "TypeA"),
                new EquipmentSlot(2, "TypeB")
        );
        final Equipment equipment = new Equipment(equipmentSlots);
        final ObservableList<Bag> bags = FXCollections.observableArrayList(
                new Bag(1, 10, new Inventory(FXCollections.observableArrayList(
                        new ItemSlot(10, 100),
                        new ItemSlot(20, 200)
                ))),
                new Bag(2, 20, new Inventory(FXCollections.observableArrayList(
                        new ItemSlot(30, 300),
                        new ItemSlot(40, 400)
                )))
        );
        Assert.assertTrue(GsonHelper.writeJsonToFile(new Character("Name", equipment, bags), JSON_TEST_FILE_PATH));
    }

    @Test
    public void writeJsonToFile_WriteJsonObjectToWrongFile_False() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Name");
        jsonObject.addProperty("id", 12345);
        jsonObject.addProperty("grade", "3.99");
        Assert.assertFalse(GsonHelper.writeJsonToFile(jsonObject, "//Wrong.json"));
    }

    @Test
    public void readJsonFromFile_ItemData_NotNull() throws Exception {
        final String filePath = new File(getClass().getResource("/item-data.json").getFile()).getAbsolutePath();
        Assert.assertNotNull(GsonHelper.readJsonFromFile(filePath, GsonHelper.itemDataType));
    }

    @Test
    public void readJsonFromFile_NoFile_Null() throws Exception {
        Assert.assertNull(GsonHelper.readJsonFromFile("C:/no-file.json", Object.class));
    }


    @Test(expected = InvalidPathException.class)
    public void readJsonFromFile_WrongFilePath_ThrowException() throws Exception {
        GsonHelper.readJsonFromFile("//wrong file path", Object.class);
    }

    @Test
    public void readItemDataFromFile_SimpleItemData_NotNull() throws Exception {
        final String filePath = new File(getClass().getResource("/item-data.json").getFile()).getAbsolutePath();
        Assert.assertNotNull(GsonHelper.readItemDataFromFile(filePath));
    }

    @Test
    public void readCurrencyDataFromFile_SimpleCurrencyData_NotNull() throws Exception {
        final String filePath = new File(getClass().getResource("/currency-data.json").getFile()).getAbsolutePath();
        Assert.assertNotNull(GsonHelper.readCurrencyDataFromFile(filePath));
    }

    @Test
    public void writeItemDataToFile_SimpleItemData_ValidJsonFile() throws Exception {
        final Map<Integer, Item> items = new HashMap<>();
        items.put(0, new Item(0, "Item Name 0", ItemRarity.BASIC, "Icon Path 0", false, false, 0));
        items.put(1, new Item(1, "Item Name 1", ItemRarity.RARE, "Icon Path 1", false, false, 0));
        items.put(2, new Item(2, "Item Name 2", ItemRarity.EXOTIC, "Icon Path 2", false, false, 0));

        Assert.assertTrue(GsonHelper.writeItemDataToFile(items, JSON_TEST_FILE_PATH));
        final Map<Integer, Item> readItems = GsonHelper.readItemDataFromFile(JSON_TEST_FILE_PATH);
        Assert.assertEquals(items, readItems);
    }

    @Test
    public void writeCurrencyDataToFile_SimpleCurrencyData_ValidJsonFile() throws Exception {
        final Map<Integer, Currency> currencies = new HashMap<>();
        currencies.put(0, new Currency(0, "Currency Name 0", "Icon Path 0"));
        currencies.put(1, new Currency(1, "Currency Name 1", "Icon Path 1"));
        currencies.put(2, new Currency(2, "Currency Name 2", "Icon Path 2"));

        Assert.assertTrue(GsonHelper.writeCurrencyDataToFile(currencies, JSON_TEST_FILE_PATH));
        final Map<Integer, Currency> readItems = GsonHelper.readCurrencyDataFromFile(JSON_TEST_FILE_PATH);
        Assert.assertEquals(currencies, readItems);
    }

    @Test
    public void test() {
    }


}