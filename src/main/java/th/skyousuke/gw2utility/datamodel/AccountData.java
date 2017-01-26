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

package th.skyousuke.gw2utility.datamodel;

import com.esotericsoftware.minlog.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Label;
import th.skyousuke.gw2utility.util.DateTimeHelper;
import th.skyousuke.gw2utility.util.gson.GsonHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountData {

    private static final AccountData instance = new AccountData();

    private SimpleStringProperty apiKey = new SimpleStringProperty();

    private SimpleObjectProperty<Account> account = new SimpleObjectProperty<>();

    private final ObservableList<String> characterNames = FXCollections.observableArrayList();
    private final ObservableMap<String, Character> characters = FXCollections.observableHashMap();
    private final ObservableList<ItemSlot> bank = FXCollections.observableArrayList();
    private final ObservableList<ItemSlot> material = FXCollections.observableArrayList();
    private final ObservableList<Wallet> wallets = FXCollections.observableArrayList();
    private final ObservableList<Transaction> sellTransactions = FXCollections.observableArrayList();
    private final ObservableList<Transaction> buyTransactions = FXCollections.observableArrayList();

    private final ObservableList<String> referenceCharacterNames = FXCollections.observableArrayList();
    private final ObservableMap<String, Character> referenceCharacters = FXCollections.observableHashMap();
    private final ObservableList<ItemSlot> referenceBank = FXCollections.observableArrayList();
    private final ObservableList<ItemSlot> referenceMaterial = FXCollections.observableArrayList();
    private final ObservableList<Wallet> referenceWallets = FXCollections.observableArrayList();
    private final ObservableList<Transaction> referenceSellTransactions = FXCollections.observableArrayList();
    private final ObservableList<Transaction> referenceBuyTransactions = FXCollections.observableArrayList();
    private LocalDateTime referenceTime;

    private ObservableList<ItemSlot> itemChange = FXCollections.observableArrayList();
    private ObservableList<Wallet> walletChange = FXCollections.observableArrayList();

    private Label referenceDataLabel;

    private AccountData() {
    }

    public static AccountData getInstance() {
        return instance;
    }

    public ObservableList<String> getCharacterNames() {
        return characterNames;
    }

    public ObservableMap<String, Character> getCharacters() {
        return characters;
    }

    public ObservableList<ItemSlot> getBank() {
        return bank;
    }

    public ObservableList<ItemSlot> getMaterial() {
        return material;
    }

    public ObservableList<Wallet> getWallets() {
        return wallets;
    }

    public ObservableList<Transaction> getSellTransactions() {
        return sellTransactions;
    }

    public ObservableList<Transaction> getBuyTransactions() {
        return buyTransactions;
    }

    public String getApiKey() {
        return apiKey.get();
    }

    public SimpleStringProperty apiKeyProperty() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey.set(apiKey);
    }

    public Account getAccount() {
        return account.get();
    }

    public SimpleObjectProperty<Account> accountProperty() {
        return account;
    }

    public void setAccount(Account account) {
        this.account.set(account);
    }

    public ObservableList<String> getReferenceCharacterNames() {
        return referenceCharacterNames;
    }

    public ObservableList<ItemSlot> getReferenceBank() {
        return referenceBank;
    }

    public ObservableList<ItemSlot> getReferenceMaterial() {
        return referenceMaterial;
    }

    public ObservableList<Wallet> getReferenceWallets() {
        return referenceWallets;
    }

    public ObservableMap<String, Character> getReferenceCharacters() {
        return referenceCharacters;
    }

    public ObservableList<Transaction> getReferenceSellTransactions() {
        return referenceSellTransactions;
    }

    public ObservableList<Transaction> getReferenceBuyTransactions() {
        return referenceBuyTransactions;
    }

    public LocalDateTime getReferenceTime() {
        return referenceTime;
    }

    public ObservableList<ItemSlot> getItemChange() {
        return itemChange;
    }

    public ObservableList<Wallet> getWalletChange() {
        return walletChange;
    }

    public void setReferenceData() {
        referenceCharacterNames.clear();
        referenceCharacters.clear();
        referenceBank.clear();
        referenceMaterial.clear();
        referenceWallets.clear();
        referenceSellTransactions.clear();
        referenceBuyTransactions.clear();

        referenceCharacterNames.addAll(characterNames);
        referenceCharacters.putAll(characters);
        referenceBank.addAll(bank);
        referenceMaterial.addAll(material);
        referenceWallets.addAll(wallets);
        referenceSellTransactions.addAll(sellTransactions);
        referenceBuyTransactions.addAll(buyTransactions);

        setReferenceTime(LocalDateTime.now());
    }

    private void setReferenceTime(LocalDateTime localDateTime) {
        referenceTime = localDateTime;
        Platform.runLater(() -> referenceDataLabel.setText("Reference Data: " + DateTimeHelper.dateTimeFormatter.format(localDateTime)));
    }

    public boolean isReferenceDataSet() {
        return referenceTime != null;
    }

    public void setReferenceDataLabel(Label referenceDataLabel) {
        this.referenceDataLabel = referenceDataLabel;
    }

    public boolean exportCurrentData(String filePath) {
        return GsonHelper.writeJsonToFile(this, AccountData.class, filePath);
    }

    public boolean setReferenceData(String filePath) {
        JsonObject data = GsonHelper.readJsonFromFile(filePath);
        if (data == null) {
            return false;
        }
        try {
            final JsonArray characterNamesInJson = data.get("characterNames").getAsJsonArray();
            final JsonObject charactersInJson = data.get("characters").getAsJsonObject();
            final JsonArray bankInJson = data.get("bank").getAsJsonArray();
            final JsonArray materialInJson = data.get("material").getAsJsonArray();
            final JsonArray walletsInJson = data.get("wallets").getAsJsonArray();
            final JsonArray sellTransactionsInJson = data.get("sellTransactions").getAsJsonArray();
            final JsonArray buyTransactionsInJson = data.get("buyTransactions").getAsJsonArray();

            final List<String> fileCharacterNames = new ArrayList<>();
            final Map<String, Character> fileCharacters = new HashMap<>();
            final List<ItemSlot> fileBank = new ArrayList<>();
            final List<ItemSlot> fileMaterial = new ArrayList<>();
            final List<Wallet> fileWallets = new ArrayList<>();
            final List<Transaction> fileSellTransactions = new ArrayList<>();
            final List<Transaction> fileBuyTransactions = new ArrayList<>();

            for (int i = 0; i < characterNamesInJson.size(); i++) {
                fileCharacterNames.add(characterNamesInJson.get(i).getAsString());
            }
            for (Map.Entry<String, JsonElement> charactersEntry : charactersInJson.entrySet()) {
                final String characterName = charactersEntry.getKey();
                final JsonObject characterInJson = charactersInJson.get(characterName).getAsJsonObject();
                final Equipment equipment = getEquipmentFromCharacter(characterInJson);
                final ObservableList<Bag> bags = getBagsFromCharacter(characterInJson);
                fileCharacters.put(characterName, new Character(characterName, equipment, bags));
            }
            for (int i = 0; i < bankInJson.size(); i++) {
                JsonObject itemSlot = bankInJson.get(i).getAsJsonObject();
                final int id = itemSlot.get("id").getAsInt();
                final int count = itemSlot.get("count").getAsInt();
                fileBank.add(new ItemSlot(id, count));
            }
            for (int i = 0; i < materialInJson.size(); i++) {
                JsonObject itemSlot = materialInJson.get(i).getAsJsonObject();
                final int id = itemSlot.get("id").getAsInt();
                final int count = itemSlot.get("count").getAsInt();
                fileMaterial.add(new ItemSlot(id, count));
            }
            for (int i = 0; i < walletsInJson.size(); i++) {
                JsonObject walletInJson = walletsInJson.get(i).getAsJsonObject();
                final int id = walletInJson.get("id").getAsInt();
                final int value = walletInJson.get("value").getAsInt();
                fileWallets.add(new Wallet(id, value));
            }
            addTransactionsInJsonToList(sellTransactionsInJson, fileSellTransactions);
            addTransactionsInJsonToList(buyTransactionsInJson, fileBuyTransactions);

            referenceCharacterNames.clear();
            referenceCharacters.clear();
            referenceBank.clear();
            referenceMaterial.clear();
            referenceWallets.clear();
            referenceSellTransactions.clear();
            referenceBuyTransactions.clear();

            referenceCharacterNames.addAll(fileCharacterNames);
            referenceCharacters.putAll(fileCharacters);
            referenceBank.addAll(fileBank);
            referenceMaterial.addAll(fileMaterial);
            referenceWallets.addAll(fileWallets);
            referenceSellTransactions.addAll(fileSellTransactions);
            referenceBuyTransactions.addAll(fileBuyTransactions);

            setReferenceTime(DateTimeHelper.getLocalDateTime(data.getAsJsonPrimitive("time").getAsString()));
            return true;
        } catch (Exception e) {
            Log.warn("Exception while set reference data from file", e);
            return false;
        }
    }

    private ObservableList<Bag> getBagsFromCharacter(JsonObject characterInJson) {
        final JsonArray bagsInJson = characterInJson.get("bags").getAsJsonArray();
        final ObservableList<Bag> bags = FXCollections.observableArrayList();
        for (int i = 0; i < bagsInJson.size(); i++) {
            final JsonObject bagInJson = bagsInJson.get(i).getAsJsonObject();
            final int id = bagInJson.get("id").getAsInt();
            final int size = bagInJson.get("size").getAsInt();
            final JsonArray inventoryInJson = bagInJson.get("inventory").getAsJsonArray();
            final ObservableList<ItemSlot> inventoryItemSlots = FXCollections.observableArrayList();
            for (int j = 0; j < inventoryInJson.size(); j++) {
                JsonObject itemSlotInJson = inventoryInJson.get(j).getAsJsonObject();
                final int itemId = itemSlotInJson.get("id").getAsInt();
                final int count = itemSlotInJson.get("count").getAsInt();
                inventoryItemSlots.add(new ItemSlot(itemId, count));
            }
            final Inventory inventory = new Inventory(inventoryItemSlots);
            bags.add(new Bag(id, size, inventory));
        }
        return bags;
    }

    private Equipment getEquipmentFromCharacter(JsonObject characterInJson) {
        final JsonArray equipmentInJson = characterInJson.get("equipment").getAsJsonArray();
        final ObservableList<EquipmentSlot> equipmentSlots = FXCollections.observableArrayList();

        for (int i = 0; i < equipmentInJson.size(); i++) {
            JsonObject equipmentSlotInJson = equipmentInJson.get(i).getAsJsonObject();
            final int id = equipmentSlotInJson.get("id").getAsInt();
            final String slot = equipmentSlotInJson.get("slot").getAsString();
            equipmentSlots.add(new EquipmentSlot(id, slot));
        }
        return new Equipment(equipmentSlots);
    }

    private void addTransactionsInJsonToList(JsonArray transactionsInJson, List<Transaction> transactions) {
        for (int i = 0; i < transactionsInJson.size(); i++) {
            final JsonObject sellTransactionInJson = transactionsInJson.get(i).getAsJsonObject();
            final int id = sellTransactionInJson.get("id").getAsInt();
            final int itemId = sellTransactionInJson.get("itemId").getAsInt();
            final int price = sellTransactionInJson.get("price").getAsInt();
            final int quantity = sellTransactionInJson.get("quantity").getAsInt();
            final LocalDateTime dateCreated = DateTimeHelper.getLocalDateTime(sellTransactionInJson.get("dateCreated").getAsString());

            LocalDateTime datePurchased = null;
            final JsonElement datePurchasedInJson = sellTransactionInJson.get("datePurchased");
            if (datePurchasedInJson != null) {
                datePurchased = DateTimeHelper.getLocalDateTime(datePurchasedInJson.getAsString());
            }
            transactions.add(new Transaction(id, itemId, price, quantity, dateCreated, datePurchased));
        }
    }
}
