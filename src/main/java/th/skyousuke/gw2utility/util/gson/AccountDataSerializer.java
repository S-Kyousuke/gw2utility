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


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import javafx.collections.ObservableList;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Bag;
import th.skyousuke.gw2utility.datamodel.Character;
import th.skyousuke.gw2utility.datamodel.EquipmentSlot;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.datamodel.Transaction;
import th.skyousuke.gw2utility.datamodel.Wallet;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountDataSerializer implements JsonSerializer<AccountData> {

    @Override
    public JsonElement serialize(AccountData src, Type typeOfSrc, JsonSerializationContext context) {
        if (src != null) {
            final JsonObject accountDataInJson = new JsonObject();

            final JsonArray characterNamesInJson = new JsonArray();
            final JsonObject charactersInJson = new JsonObject();
            final JsonArray bankInJson = new JsonArray();
            final JsonArray materialInJson = new JsonArray();
            final JsonArray walletsInJson = new JsonArray();

            for (String characterName : src.getCharacterNames()) {
                characterNamesInJson.add(characterName);
            }
            for (Character character : src.getCharacters().values()) {
                final JsonObject characterInJson = new JsonObject();
                characterInJson.add("equipment", getEquipmentJson(character));
                characterInJson.add("bags", getBagsJson(character));
                charactersInJson.add(character.getName(), characterInJson);
            }
            for (ItemSlot itemSlotInBank : src.getBank()) {
                JsonObject itemSlotInJson = new JsonObject();
                itemSlotInJson.addProperty("id", itemSlotInBank.getItem().getId());
                itemSlotInJson.addProperty("count", itemSlotInBank.getItemCount());
                bankInJson.add(itemSlotInJson);
            }
            for (ItemSlot itemSlotInMaterial : src.getMaterial()) {
                JsonObject itemSlotInJson = new JsonObject();
                itemSlotInJson.addProperty("id", itemSlotInMaterial.getItem().getId());
                itemSlotInJson.addProperty("count", itemSlotInMaterial.getItemCount());
                materialInJson.add(itemSlotInJson);
            }
            for (Wallet wallet : src.getWallets()) {
                JsonObject walletInJson = new JsonObject();
                walletInJson.addProperty("id", wallet.getCurrency().getId());
                walletInJson.addProperty("value", wallet.getValue());
                walletsInJson.add(walletInJson);
            }

            accountDataInJson.add("characterNames", characterNamesInJson);
            accountDataInJson.add("characters", charactersInJson);
            accountDataInJson.add("bank", bankInJson);
            accountDataInJson.add("material", materialInJson);
            accountDataInJson.add("wallets", walletsInJson);
            accountDataInJson.add("buyTransactions", getTransactionsJson(src.getBuyTransactions()));
            accountDataInJson.add("sellTransactions", getTransactionsJson(src.getSellTransactions()));
            accountDataInJson.addProperty("time", DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now()));
            return accountDataInJson;
        }
        return null;
    }

    private JsonArray getEquipmentJson(Character character) {
        final JsonArray equipmentInJson = new JsonArray();
        for (EquipmentSlot equipmentSlot : character.getEquipment().getEquipmentSlots()) {
            JsonObject equipmentSlotInJson = new JsonObject();
            equipmentSlotInJson.addProperty("id", equipmentSlot.getItem().getId());
            equipmentSlotInJson.addProperty("slot", equipmentSlot.getSlotType());
            equipmentInJson.add(equipmentSlotInJson);
        }
        return equipmentInJson;
    }

    private JsonArray getBagsJson(Character character) {
        final JsonArray bagsInJson = new JsonArray();
        for (Bag bag : character.getBags()) {
            final JsonObject bagInJson = new JsonObject();
            final JsonArray inventoryInJson = new JsonArray();
            for (ItemSlot itemSlot : bag.getInventory().getItemSlots()) {
                JsonObject itemSlotInJson = new JsonObject();
                itemSlotInJson.addProperty("id", itemSlot.getItem().getId());
                itemSlotInJson.addProperty("count", itemSlot.getItemCount());
                inventoryInJson.add(itemSlotInJson);
            }
            bagInJson.addProperty("id", bag.getId());
            bagInJson.addProperty("size", bag.getSize());
            bagInJson.add("inventory", inventoryInJson);
            bagsInJson.add(bagInJson);
        }
        return bagsInJson;
    }

    private JsonArray getTransactionsJson(ObservableList<Transaction> transactions) {
        final JsonArray transactionsInJson = new JsonArray();
        for (Transaction transaction : transactions) {
            JsonObject transactionInJson = new JsonObject();
            transactionInJson.addProperty("id", transaction.getId());
            transactionInJson.addProperty("itemId", transaction.getItemId());
            transactionInJson.addProperty("price", transaction.getPrice());
            transactionInJson.addProperty("quantity", transaction.getQuantity());
            transactionInJson.addProperty("dateCreated", DateTimeFormatter.ISO_DATE_TIME.format(transaction.getDateCreated()));
            LocalDateTime datePurchased = transaction.getDatePurchased();
            if (datePurchased != null) {
                transactionInJson.addProperty("datePurchased", DateTimeFormatter.ISO_DATE_TIME.format(datePurchased));
            }
            transactionsInJson.add(transactionInJson);
        }
        return transactionsInJson;
    }
}
