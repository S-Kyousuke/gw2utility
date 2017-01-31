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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemData;
import th.skyousuke.gw2utility.tests.datamodel.ItemTest;
import th.skyousuke.gw2utility.view.ItemNameTableCell;
import th.skyousuke.gw2utility.view.ItemValueFactory;


public class ItemTableNameTestController {

    @FXML
    private TableView<ItemTest> itemTable;
    @FXML
    private TableColumn<ItemTest, Item> nameColumn;

    private ObservableList<ItemTest> itemTests = FXCollections.observableArrayList();

    private Item itemSample = ItemData.getInstance().getItem(1);
    private ItemTest itemTest = new ItemTest(itemSample);

    public void initialize() {

        itemTests.add(itemTest);
        itemTable.setItems(itemTests);

        nameColumn.setCellValueFactory(new ItemValueFactory<>());
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F5) {
            itemSample.setName("F5 Item");
        } else if (keyEvent.getCode() == KeyCode.F6) {
            itemSample.setName("F6 Item");
        }
    }
}
