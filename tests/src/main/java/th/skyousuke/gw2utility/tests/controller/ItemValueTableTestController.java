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

package th.skyousuke.gw2utility.tests.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemContainerValue;
import th.skyousuke.gw2utility.datamodel.ItemSlot;
import th.skyousuke.gw2utility.datamodel.ItemValue;
import th.skyousuke.gw2utility.util.CustomColor;
import th.skyousuke.gw2utility.util.ItemValueCalculator;
import th.skyousuke.gw2utility.view.ItemCountTableCell;
import th.skyousuke.gw2utility.view.ItemNameTableCell;
import th.skyousuke.gw2utility.view.PriceInCoinTableCell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class ItemValueTableTestController {

    @FXML
    private TableView<ItemContainerValue> itemValueTable;

    private ObservableList<ItemContainerValue> itemValues = FXCollections.observableArrayList();

    private ItemValueCalculator itemValueCalculator = new ItemValueCalculator();

    private ItemSlot itemSlotSample = new ItemSlot(1, 0);
    private ItemValue itemValueSample = itemValueCalculator.getItemValue(itemSlotSample.getItem());

    public void initialize() {
        initItemValueTable();

        itemValues.add(new ItemContainerValue(itemValueSample, itemSlotSample.itemCountProperty()));
        generateData();
    }

    @SuppressWarnings("unchecked")
    private void initItemValueTable() {
        itemValueTable.setItems(itemValues);

        TableColumn<ItemContainerValue, Integer> quantityColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(0);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(param -> {
            final ItemCountTableCell<ItemContainerValue> tableCell = new ItemCountTableCell<>();
            tableCell.setShowPlusSign(true);
            tableCell.setTextColor(Color.WHITE, CustomColor.positiveValueColor, CustomColor.negativeValueColor);
            return tableCell;
        });

        TableColumn<ItemContainerValue, Item> nameColumn = (TableColumn<ItemContainerValue, Item>) itemValueTable.getColumns().get(1);
        nameColumn.setCellValueFactory(param -> param.getValue().getItemValue().itemProperty());
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());

        TableColumn<ItemContainerValue, Integer> valueColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(2);
        valueColumn.setCellValueFactory(param -> param.getValue().getItemValue().valueProperty().asObject());
        valueColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<ItemContainerValue, Integer> tradeValueColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(3);
        tradeValueColumn.setCellValueFactory(param -> param.getValue().getItemValue().incomeValueProperty().asObject());
        tradeValueColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<ItemContainerValue, Integer> totalIncomeColumn = (TableColumn<ItemContainerValue, Integer>) itemValueTable.getColumns().get(4);
        totalIncomeColumn.setCellValueFactory(new PropertyValueFactory<>("totalIncomeValue"));
        totalIncomeColumn.setCellFactory(param -> {
            final PriceInCoinTableCell<ItemContainerValue> tableCell = new PriceInCoinTableCell<>();
            tableCell.setShowPlusSign(true);
            tableCell.setTextColor(Color.WHITE, CustomColor.positiveValueColor, CustomColor.negativeValueColor);
            return tableCell;
        });
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case F5:
                itemValueSample.setValue(itemValueSample.getValue() - 1);
                break;
            case F6:
                itemValueSample.setValue(itemValueSample.getValue() + 1);
                break;
            case F7:
                itemSlotSample.setItemCount(itemSlotSample.getItemCount() - 1);
                break;
            case F8:
                itemSlotSample.setItemCount(itemSlotSample.getItemCount() + 1);
                break;
            default:
        }
    }

    private void generateData() {
        List<ItemSlot> itemSlots = new ArrayList<>();
        itemSlots.add(new ItemSlot(31039, ThreadLocalRandom.current().nextInt(0, 251)));
        itemSlots.add(new ItemSlot(31040, ThreadLocalRandom.current().nextInt(0, 251)));
        itemSlots.add(new ItemSlot(31041, ThreadLocalRandom.current().nextInt(0, 251)));
        itemSlots.add(new ItemSlot(31042, ThreadLocalRandom.current().nextInt(0, 251)));

        for (ItemSlot itemSlot : itemSlots) {
            itemValues.add(new ItemContainerValue(itemValueCalculator.getItemValue(itemSlot.getItem()), itemSlot.itemCountProperty()));
        }
    }
}
