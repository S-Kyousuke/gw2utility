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

package th.skyousuke.gw2utility.controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Bag;
import th.skyousuke.gw2utility.datamodel.Character;
import th.skyousuke.gw2utility.datamodel.EquipmentSlot;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.util.JavaFXControlUtils;
import th.skyousuke.gw2utility.view.ItemCountTableCell;
import th.skyousuke.gw2utility.view.ItemNameTableCell;
import th.skyousuke.gw2utility.view.ItemValueFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CharacterPageController {
    @FXML
    private ComboBox<String> characterNameComboBox;
    @FXML
    private TableView<ItemSlot> inventoryTableView;
    @FXML
    private TableView<EquipmentSlot> equipmentTableView;

    private ObservableList<ItemSlot> characterInventoryList;
    private ObservableList<EquipmentSlot> characterEquipmentList;

    private String lastSelectedCharacterName = "";

    public void initialize() {
        AccountData.getInstance().getCharacterNames().addListener((ListChangeListener<String>) c -> Platform.runLater(this::updateCharacterComboBox));
        AccountData.getInstance().getCharacters().addListener((MapChangeListener<String, Character>) change -> Platform.runLater(this::refreshSelectedCharacter));

        initCharacterNameComboBox();
        initInventoryTableView();
        initEquipmentTableView();
    }

    private void initCharacterNameComboBox() {
        characterNameComboBox.setItems(AccountData.getInstance().getCharacterNames());
        characterNameComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lastSelectedCharacterName = newValue;
                refreshSelectedCharacter();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initInventoryTableView() {
        characterInventoryList = FXCollections.observableArrayList();
        inventoryTableView.setItems(characterInventoryList);

        TableColumn<ItemSlot, Integer> countColumn = (TableColumn<ItemSlot, Integer>) inventoryTableView.getColumns().get(0);
        countColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));
        countColumn.setCellFactory(param -> new ItemCountTableCell<>());

        TableColumn<ItemSlot, Item> nameColumn = (TableColumn<ItemSlot, Item>) inventoryTableView.getColumns().get(1);
        nameColumn.setCellValueFactory(new ItemValueFactory<>());
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Item::getName));
    }

    @SuppressWarnings("unchecked")
    private void initEquipmentTableView() {
        characterEquipmentList = FXCollections.observableArrayList();
        equipmentTableView.setItems(characterEquipmentList);

        TableColumn<EquipmentSlot, Item> nameColumn = (TableColumn<EquipmentSlot, Item>) equipmentTableView.getColumns().get(0);
        nameColumn.setCellValueFactory(new ItemValueFactory<>());
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Item::getName));

        TableColumn<EquipmentSlot, String> typeColumn = (TableColumn<EquipmentSlot, String>) equipmentTableView.getColumns().get(1);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("slotType"));
    }

    public void refreshSelectedCharacter() {
        final Map<String, Character> characters = AccountData.getInstance().getCharacters();
        final Character character = characters.get(lastSelectedCharacterName);

        if (character != null) {
            final List<ItemSlot> itemSlots = new ArrayList<>();
            final List<EquipmentSlot> equipmentSlots = character.getEquipment().getEquipmentSlots();

            final List<Bag> bags = character.getBags();
            for (Bag bag : bags) {
                itemSlots.addAll(bag.getInventory().getItemSlots());
            }

            characterEquipmentList.clear();
            characterInventoryList.clear();
            characterEquipmentList.addAll(equipmentSlots);
            characterInventoryList.addAll(itemSlots);

            JavaFXControlUtils.fixTableViewColumnWidth(inventoryTableView);
            JavaFXControlUtils.fixTableViewColumnWidth(equipmentTableView);

            JavaFXControlUtils.performColumnSort(inventoryTableView, 0, TableColumn.SortType.DESCENDING);
        }
    }

    public void updateCharacterComboBox() {
        if (AccountData.getInstance().getCharacterNames().contains(lastSelectedCharacterName)) {
            characterNameComboBox.getSelectionModel().select(lastSelectedCharacterName);
        } else {
            characterNameComboBox.getSelectionModel().selectFirst();
        }
    }
}
