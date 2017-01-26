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

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

public class Transaction {

    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty itemId;
    private final SimpleIntegerProperty price;
    private final SimpleIntegerProperty quantity;
    private final SimpleObjectProperty<LocalDateTime> dateCreated;
    private final SimpleObjectProperty<LocalDateTime> datePurchased;

    public Transaction(int id, int itemId, int price, int quantity, LocalDateTime dateCreated) {
        this(id, itemId, price, quantity, dateCreated, null);
    }

    public Transaction(int id, int itemId, int price, int quantity, LocalDateTime dateCreated, LocalDateTime datePurchased) {
        this.id = new SimpleIntegerProperty(id);
        this.itemId = new SimpleIntegerProperty(itemId);
        this.price = new SimpleIntegerProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.dateCreated = new SimpleObjectProperty<>(dateCreated);
        this.datePurchased = new SimpleObjectProperty<>(datePurchased);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getItemId() {
        return itemId.get();
    }

    public SimpleIntegerProperty itemIdProperty() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId.set(itemId);
    }

    public int getPrice() {
        return price.get();
    }

    public SimpleIntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public LocalDateTime getDateCreated() {
        return dateCreated.get();
    }

    public SimpleObjectProperty<LocalDateTime> dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public LocalDateTime getDatePurchased() {
        return datePurchased.get();
    }

    public SimpleObjectProperty<LocalDateTime> datePurchasedProperty() {
        return datePurchased;
    }

    public void setDatePurchased(LocalDateTime datePurchased) {
        this.datePurchased.set(datePurchased);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", dateCreated=" + dateCreated +
                ", datePurchased=" + datePurchased +
                '}';
    }
}
