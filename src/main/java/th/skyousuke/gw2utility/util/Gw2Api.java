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

package th.skyousuke.gw2utility.util;

import com.esotericsoftware.minlog.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import th.skyousuke.gw2utility.datamodel.Account;
import th.skyousuke.gw2utility.datamodel.Bag;
import th.skyousuke.gw2utility.datamodel.Character;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.Equipment;
import th.skyousuke.gw2utility.datamodel.EquipmentSlot;
import th.skyousuke.gw2utility.datamodel.Inventory;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemRarity;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.datamodel.Transaction;
import th.skyousuke.gw2utility.datamodel.Wallet;
import th.skyousuke.gw2utility.util.gson.GsonHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Gw2Api {

    private static final String GOOGLE_SITE = "https://www.google.com";

    private static final String API_URL = "https://api.guildwars2.com/v2";

    private static final String ACCOUNT_API_URL = API_URL + "/account";
    private static final String BANK_API_URL = ACCOUNT_API_URL + "/bank";
    private static final String MATERIAL_API_URL = ACCOUNT_API_URL + "/materials";
    private static final String WALLET_API_URL = ACCOUNT_API_URL + "/wallet";

    private static final String ITEM_API_URL = API_URL + "/items";
    private static final String CURRENCY_API_URL = API_URL + "/currencies";

    private static final String CHARACTER_API_URL = API_URL + "/characters";

    private static final String COMMERCE_API_URL = API_URL + "/commerce";
    private static final String PRICES_API_URL = COMMERCE_API_URL + "/prices";
    private static final String TRANSACTION_API_URL = COMMERCE_API_URL + "/transactions";
    private static final String CURRENT_TRANSACTION_API_URL = TRANSACTION_API_URL + "/current";
    private static final String CURRENT_BUY_TRANSACTION_API_URL = CURRENT_TRANSACTION_API_URL + "/buys";
    private static final String CURRENT_SELL_TRANSACTION_API_URL = CURRENT_TRANSACTION_API_URL + "/sells";

    private static final String HISTORY_TRANSACTION_API_URL = TRANSACTION_API_URL + "/history";

    private static final int DEFAULT_TIMEOUT_SEC = 10;
    private static final int BUFFER_SIZE = 4096;

    private static final Gw2Api instance = new Gw2Api();
    private static final int SECOND_TO_MILLISECOND = 1000;

    private final ObservableSet<Integer> itemIdSet = FXCollections.observableSet();
    private CountDownLatch itemIdUpdateFinishedSignal;

    // Manual Initialization currency Id (Get from: https://api.guildwars2.com/v2/currencies)
    public static final int COIN_ID = 1;
    private final ObservableSet<Integer> currencyIdSet = FXCollections.observableSet(
            COIN_ID, 2, 3, 4, 5, 6, 7, 9, 10,
            11, 12, 13, 14, 15, 16, 18, 19, 20,
            22, 23, 24, 25, 26, 27, 28, 29, 30
    );

    private Gw2Api() {
        new Thread(this::updateItemIdSet).start();
    }

    public static Gw2Api getInstance() {
        return instance;
    }

    private String readJsonFromUrl(String urlString) throws IOException {
        return readJsonFromUrl(urlString, DEFAULT_TIMEOUT_SEC);
    }

    private String readJsonFromUrl(String urlString, String apiKey, String additionalRequest) throws IOException {
        return readJsonFromUrl(urlString, apiKey, additionalRequest, DEFAULT_TIMEOUT_SEC);
    }

    private String readJsonFromUrl(String urlString, String apiKey) throws IOException {
        return readJsonFromUrl(urlString, apiKey, "", DEFAULT_TIMEOUT_SEC);
    }

    private String readJsonFromUrl(String urlString, int timeoutSec) throws IOException {
        return readJsonFromUrl(urlString, null, "", timeoutSec);
    }

    private String readJsonFromUrl(String urlString, String apiKey, String additionalRequest, int timeoutSec) throws IOException {
        String fixedUrlString = urlString.replace(" ", "%20");
        if (apiKey != null) {
            fixedUrlString += "?access_token=" + apiKey + additionalRequest;
        }
        URL url = new URL(fixedUrlString);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(timeoutSec * SECOND_TO_MILLISECOND);
        httpURLConnection.setReadTimeout(timeoutSec * SECOND_TO_MILLISECOND);

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        int read;
        char[] chars = new char[BUFFER_SIZE];
        while ((read = reader.read(chars)) != -1)
            stringBuilder.append(chars, 0, read);
        reader.close();
        return stringBuilder.toString();
    }

    public void updateItemIdSet() {
        itemIdUpdateFinishedSignal = new CountDownLatch(1);
        try {
            String itemIdSetJson = readJsonFromUrl(ITEM_API_URL);
            JsonArray jsonArray = GsonHelper.jsonParser.parse(itemIdSetJson).getAsJsonArray();
            itemIdSet.clear();
            // @formatter:off
            itemIdSet.addAll(GsonHelper.gson.fromJson(jsonArray, new TypeToken<Set<Integer>>() {}.getType()));
            // @formatter:on
        } catch (IOException e) {
            Log.error("Exception while updating item id set", e);
        }
        itemIdUpdateFinishedSignal.countDown();
    }

    public ObservableSet<Integer> getItemIdSet() {
        if (itemIdUpdateFinishedSignal != null) {
            try {
                itemIdUpdateFinishedSignal.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return itemIdSet;
    }

    public Item getItem(int id) {
        try {
            String itemJson = readJsonFromUrl(ITEM_API_URL + "/" + id);
            JsonObject itemInJson = GsonHelper.jsonParser.parse(itemJson).getAsJsonObject();
            final String name = itemInJson.getAsJsonPrimitive("name").getAsString();
            final ItemRarity rarity = ItemRarity.fromString(itemInJson.getAsJsonPrimitive("rarity").getAsString());
            final String imagePath = itemInJson.getAsJsonPrimitive("icon").getAsString();
            final int vendorPrice = itemInJson.getAsJsonPrimitive("vendor_value").getAsInt();

            boolean noSell = false;
            boolean boundOnAcquire = false;
            final JsonArray flags = itemInJson.getAsJsonArray("flags");
            for (int i = 0; i < flags.size(); i++) {
                String flag = flags.get(i).getAsString();
                switch (flag) {
                    case "NoSell":
                        noSell = true;
                        break;
                    case "AccountBound":
                    case "SoulbindOnAcquire":
                        boundOnAcquire = true;
                        break;
                    default:
                }
            }
            return new Item(id, name, rarity, imagePath, boundOnAcquire, noSell, vendorPrice);
        } catch (IOException e) {
            Log.warn("Exception while get item: " + id, e);
            return null;
        }
    }

    public Currency getCurrency(int id) {
        try {
            String currencyJson = readJsonFromUrl(CURRENCY_API_URL + "/" + id);
            JsonObject currencyInJson = GsonHelper.jsonParser.parse(currencyJson).getAsJsonObject();
            final String name = currencyInJson.getAsJsonPrimitive("name").getAsString();
            final String imagePath = currencyInJson.getAsJsonPrimitive("icon").getAsString();
            return new Currency(id, name, imagePath);
        } catch (IOException e) {
            Log.warn("Exception while get currency: " + id, e);
            return null;
        }
    }

    public Account getAccount(String apiKey) {
        try {
            String accountJson = readJsonFromUrl(ACCOUNT_API_URL, apiKey);
            JsonObject accountInJson = GsonHelper.jsonParser.parse(accountJson).getAsJsonObject();
            final String id = accountInJson.getAsJsonPrimitive("id").getAsString();
            final String name = accountInJson.getAsJsonPrimitive("name").getAsString();
            final int worldId = accountInJson.getAsJsonPrimitive("world").getAsInt();
            return new Account(id, name, worldId);
        } catch (IOException e) {
            Log.warn("Exception while get account data", e);
            return null;
        }
    }

    public List<String> getCharacterNames(String apiKey) {
        try {
            String characterSetJson = readJsonFromUrl(CHARACTER_API_URL, apiKey);
            JsonArray jsonArray = GsonHelper.jsonParser.parse(characterSetJson).getAsJsonArray();
            // @formatter:off
            return GsonHelper.gson.fromJson(jsonArray, new TypeToken<List<String>>() {}.getType());
            // @formatter:on
        } catch (IOException e) {
            Log.warn("Exception while get character names", e);
            return new ArrayList<>();
        }
    }

    public Map<String, Character> getCharacters(String apiKey) {
        try {
            final String charactersJson = readJsonFromUrl(CHARACTER_API_URL, apiKey, "&page=0");
            final JsonArray charactersJsonArray = GsonHelper.jsonParser.parse(charactersJson).getAsJsonArray();
            final Map<String, Character> characters = new HashMap<>();
            for (int i = 0; i < charactersJsonArray.size(); i++) {
                JsonObject characterJson = charactersJsonArray.get(i).getAsJsonObject();
                final String name = characterJson.getAsJsonPrimitive("name").getAsString();
                characters.put(name, new Character(name, getEquipment(characterJson), getBags(characterJson)));
            }
            return characters;
        } catch (Exception e) {
            Log.error("Exception while get character data", e);
            return null;
        }
    }

    private Equipment getEquipment(JsonObject characterJson) {
        final JsonArray equipmentJson = characterJson.getAsJsonArray("equipment");
        final ObservableList<EquipmentSlot> equipmentSlots = FXCollections.observableArrayList();
        for (int i = 0; i < equipmentJson.size(); i++) {
            final JsonElement equipmentSlotJsonElement = equipmentJson.get(i);
            if (equipmentSlotJsonElement.isJsonObject()) {
                final JsonObject equipmentSlotJson = equipmentSlotJsonElement.getAsJsonObject();
                final int equipmentSlotId = equipmentSlotJson.get("id").getAsInt();
                final String slotType = equipmentSlotJson.get("slot").getAsString();
                equipmentSlots.add(new EquipmentSlot(equipmentSlotId, slotType));
            }
        }
        return new Equipment(equipmentSlots);
    }

    private ObservableList<Bag> getBags(JsonObject characterJson) {
        final JsonArray bagsJson = characterJson.getAsJsonArray("bags");
        final ObservableList<Bag> bags = FXCollections.observableArrayList();
        for (int j = 0; j < bagsJson.size(); j++) {
            final JsonElement bagJsonElement = bagsJson.get(j);
            if (bagJsonElement.isJsonObject()) {
                final JsonObject bagJson = bagJsonElement.getAsJsonObject();
                final int bagSlotId = bagJson.get("id").getAsInt();
                final int size = bagJson.get("size").getAsInt();
                bags.add(new Bag(bagSlotId, size, getInventory(bagJson)));
            }
        }
        return bags;
    }

    private Inventory getInventory(JsonObject bagJson) {
        final JsonArray inventoryInJson = bagJson.get("inventory").getAsJsonArray();
        final ObservableList<ItemSlot> itemSlots = FXCollections.observableArrayList();

        for (int i = 0; i < inventoryInJson.size(); i++) {
            final JsonElement itemSlotJsonElement = inventoryInJson.get(i);
            if (itemSlotJsonElement.isJsonObject()) {
                final JsonObject itemSlotJson = itemSlotJsonElement.getAsJsonObject();
                final int itemSlotId = itemSlotJson.get("id").getAsInt();
                final int itemSlotCount = itemSlotJson.get("count").getAsInt();
                itemSlots.add(new ItemSlot(itemSlotId, itemSlotCount));
            }
        }
        return new Inventory(itemSlots);
    }

    public boolean isOffline() {
        try {
            URL url = new URL(GOOGLE_SITE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.getResponseCode();
            return false;
        } catch (IOException e) {
            Log.info("No internet connection!", e);
            return true;
        }
    }

    public List<ItemSlot> getBankItemSlots(String apiKey) {
        try {
            String bankJson = readJsonFromUrl(BANK_API_URL, apiKey);
            JsonArray jsonArray = GsonHelper.jsonParser.parse(bankJson).getAsJsonArray();
            List<ItemSlot> bankItemSlots = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    int itemId = jsonObject.get("id").getAsInt();
                    int count = jsonObject.get("count").getAsInt();
                    bankItemSlots.add(new ItemSlot(itemId, count));
                }
            }
            return bankItemSlots;
        } catch (IOException e) {
            Log.error("Exception while get bank data", e);
            return new ArrayList<>();
        }
    }

    public List<ItemSlot> getMaterialItemSlots(String apiKey) {
        try {
            String materialJson = readJsonFromUrl(MATERIAL_API_URL, apiKey);
            JsonArray jsonArray = GsonHelper.jsonParser.parse(materialJson).getAsJsonArray();
            List<ItemSlot> materialItemSlots = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    int itemId = jsonObject.get("id").getAsInt();
                    int count = jsonObject.get("count").getAsInt();
                    materialItemSlots.add(new ItemSlot(itemId, count));
                }
            }
            return materialItemSlots;
        } catch (IOException e) {
            Log.error("Exception while get material data", e);
            return new ArrayList<>();
        }
    }

    public List<Wallet> getWallets(String apiKey) {
        try {
            String walletsJson = readJsonFromUrl(WALLET_API_URL, apiKey);
            JsonArray jsonArray = GsonHelper.jsonParser.parse(walletsJson).getAsJsonArray();
            List<Wallet> wallets = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    int currencyId = jsonObject.get("id").getAsInt();
                    int value = jsonObject.get("value").getAsInt();
                    wallets.add(new Wallet(currencyId, value));
                }
            }
            return wallets;
        } catch (IOException e) {
            Log.error("Exception while get wallet data", e);
            return new ArrayList<>();
        }
    }

    public List<Transaction> getCurrentBuyTransactions(String apiKey) {
        try {
            String currentBuyJson = readJsonFromUrl(CURRENT_BUY_TRANSACTION_API_URL, apiKey);
            JsonArray jsonArray = GsonHelper.jsonParser.parse(currentBuyJson).getAsJsonArray();
            List<Transaction> currentBuys = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    currentBuys.add(jsonToTransaction(jsonObject));
                }
            }
            return currentBuys;
        } catch (IOException e) {
            Log.error("Exception while get current sell transactions", e);
            return new ArrayList<>();
        }
    }

    public List<Transaction> getCurrentSellTransactions(String apiKey) {
        try {
            String currentSellJson = readJsonFromUrl(CURRENT_SELL_TRANSACTION_API_URL, apiKey);
            JsonArray jsonArray = GsonHelper.jsonParser.parse(currentSellJson).getAsJsonArray();
            List<Transaction> currentSells = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    currentSells.add(jsonToTransaction(jsonObject));
                }
            }
            return currentSells;
        } catch (IOException e) {
            Log.error("Exception while get current buy transactions", e);
            return new ArrayList<>();
        }
    }

    private Transaction jsonToTransaction(JsonObject jsonObject) {
        final int id = jsonObject.get("id").getAsInt();
        final int itemId = jsonObject.get("item_id").getAsInt();
        final int price = jsonObject.get("price").getAsInt();
        final int quantity = jsonObject.get("quantity").getAsInt();
        final LocalDateTime dateCreated = LocalDateTime.parse(jsonObject.get("created").getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        final JsonElement purchasedElement = jsonObject.get("purchased");
        if (purchasedElement == null) {
            return new Transaction(id, itemId, price, quantity, dateCreated);
        } else {
            final LocalDateTime datePurchased = LocalDateTime.parse(purchasedElement.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return new Transaction(id, itemId, price, quantity, dateCreated, datePurchased);
        }
    }

    public int getItemSellPrice(int itemId) {
        try {
            String pricesJson = readJsonFromUrl(PRICES_API_URL + "/" + itemId);
            JsonObject pricesInJson = GsonHelper.jsonParser.parse(pricesJson).getAsJsonObject();
            return pricesInJson.get("sells").getAsJsonObject().get("unit_price").getAsInt();
        } catch (IOException e) {
            Log.warn("Exception while get item sell price", e);
            return 0;
        }
    }

    public int getItemBuyPrice(int itemId) {
        try {
            String pricesJson = readJsonFromUrl(PRICES_API_URL + "/" + itemId);
            JsonObject pricesInJson = GsonHelper.jsonParser.parse(pricesJson).getAsJsonObject();
            return pricesInJson.get("buys").getAsJsonObject().get("unit_price").getAsInt();
        } catch (IOException e) {
            Log.error("Exception while get item buy price", e);
            return 0;
        }
    }

    public ObservableSet<Integer> getCurrencyIdSet() {
        return currencyIdSet;
    }
}
