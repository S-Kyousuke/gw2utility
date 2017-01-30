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

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.Transaction;
import th.skyousuke.gw2utility.datamodel.TransactionListing;
import th.skyousuke.gw2utility.util.JavaFXControlUtils;
import th.skyousuke.gw2utility.view.ItemCountTableCell;
import th.skyousuke.gw2utility.view.ItemNameTableCell;
import th.skyousuke.gw2utility.view.ListingAgeTableCell;
import th.skyousuke.gw2utility.view.PriceInCoinTableCell;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class TradingPostPageController {

    @FXML
    private TableView<TransactionListing> sellListTableView;
    @FXML
    private TableView<TransactionListing> buyListTableView;

    private ObservableList<TransactionListing> sellListings;

    private ObservableList<TransactionListing> buyListings;

    public void initialize() {
        initListing();
        bindListingsToTransactions();

        initSellListTableView();
        initBuyListTableView();
    }

    private void initListing() {
        sellListings = FXCollections.observableArrayList();
        buyListings = FXCollections.observableArrayList();

        for (Transaction transaction : AccountData.getInstance().getSellTransactions()) {
            TransactionListing sellList = new TransactionListing(transaction);
            sellList.updateSellPrice();
            sellListings.add(sellList);
        }
        for (Transaction transaction : AccountData.getInstance().getBuyTransactions()) {
            TransactionListing buyList = new TransactionListing(transaction);
            buyList.updateBuyPrice();
            buyListings.add(buyList);
        }
    }

    private void bindListingsToTransactions() {
        AccountData.getInstance().getSellTransactions().addListener((ListChangeListener<Transaction>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Transaction transaction : c.getAddedSubList()) {
                        TransactionListing listing = new TransactionListing(transaction);
                        listing.updateSellPrice();
                        sellListings.add(listing);
                    }
                } else if (c.wasRemoved()) {
                    removeTransactionFromListings(c.getRemoved(), sellListings);
                }
            }
            JavaFXControlUtils.fixTableViewColumnWidth(sellListTableView);
        });

        AccountData.getInstance().getBuyTransactions().addListener((ListChangeListener<Transaction>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Transaction transaction : c.getAddedSubList()) {
                        TransactionListing listing = new TransactionListing(transaction);
                        listing.updateBuyPrice();
                        buyListings.add(listing);
                    }
                } else if (c.wasRemoved()) {
                    removeTransactionFromListings(c.getRemoved(), buyListings);
                }
            }
            JavaFXControlUtils.fixTableViewColumnWidth(buyListTableView);
        });
    }

    @SuppressWarnings("unchecked")
    private void initSellListTableView() {
        sellListTableView.setItems(sellListings);

        TableColumn<TransactionListing, Integer> countColumn = (TableColumn<TransactionListing, Integer>) sellListTableView.getColumns().get(0);
        countColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTransaction().getQuantity()));
        countColumn.setCellFactory(param -> new ItemCountTableCell<>());

        TableColumn<TransactionListing, Item> nameColumn = (TableColumn<TransactionListing, Item>) sellListTableView.getColumns().get(1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Item::getName));

        TableColumn<TransactionListing, Integer> listingPriceColumn = (TableColumn<TransactionListing, Integer>) sellListTableView.getColumns().get(2);
        listingPriceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTransaction().getPrice()));
        listingPriceColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<TransactionListing, Integer> sellPriceColumn = (TableColumn<TransactionListing, Integer>) sellListTableView.getColumns().get(3);
        sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        sellPriceColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<TransactionListing, Long> ageColumn = (TableColumn<TransactionListing, Long>) sellListTableView.getColumns().get(4);
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("ageInSeconds"));
        ageColumn.setCellFactory(param -> new ListingAgeTableCell<>());
    }

    @SuppressWarnings("unchecked")
    private void initBuyListTableView() {
        buyListTableView.setItems(buyListings);

        TableColumn<TransactionListing, Integer> countColumn = (TableColumn<TransactionListing, Integer>) buyListTableView.getColumns().get(0);
        countColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTransaction().getQuantity()));
        countColumn.setCellFactory(param -> new ItemCountTableCell<>());

        TableColumn<TransactionListing, Item> nameColumn = (TableColumn<TransactionListing, Item>) buyListTableView.getColumns().get(1);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        nameColumn.setCellFactory(param -> new ItemNameTableCell<>());
        nameColumn.setComparator(Comparator.comparing(Item::getName));

        TableColumn<TransactionListing, Integer> listingPriceColumn = (TableColumn<TransactionListing, Integer>) buyListTableView.getColumns().get(2);
        listingPriceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTransaction().getPrice()));
        listingPriceColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<TransactionListing, Integer> sellPriceColumn = (TableColumn<TransactionListing, Integer>) buyListTableView.getColumns().get(3);
        sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("buyPrice"));
        sellPriceColumn.setCellFactory(param -> new PriceInCoinTableCell<>());

        TableColumn<TransactionListing, Long> ageColumn = (TableColumn<TransactionListing, Long>) buyListTableView.getColumns().get(4);
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("ageInSeconds"));
        ageColumn.setCellFactory(param -> new ListingAgeTableCell<>());
    }

    private void removeTransactionFromListings(List<? extends Transaction> transactions,
                                               List<TransactionListing> listings) {
        for (Transaction transaction : transactions) {
            Iterator<TransactionListing> it = listings.iterator();
            while (it.hasNext()) {
                TransactionListing listing = it.next();
                if (listing.getTransaction() == transaction) {
                    listing.stopAgeCalculation();
                    it.remove();
                }
            }
        }
    }

    public void dispose() {
        for (TransactionListing buyListing : buyListings) {
            buyListing.stopAgeCalculation();
        }
        for (TransactionListing sellListing : sellListings) {
            sellListing.stopAgeCalculation();
        }
    }

}
