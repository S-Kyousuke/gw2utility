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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class Item {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleObjectProperty<ItemRarity> rarity;
    private SimpleStringProperty iconPath;
    private SimpleBooleanProperty boundOnAcquire;
    private SimpleBooleanProperty noSell;
    private SimpleIntegerProperty vendorPrice;

    private CountDownLatch waitDataSignal;

    public Item(int id, String name, ItemRarity rarity, String iconPath, boolean boundOnAcquire, boolean noSell, int vendorPrice) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.rarity = new SimpleObjectProperty<>(rarity);
        this.iconPath = new SimpleStringProperty(iconPath);
        this.boundOnAcquire = new SimpleBooleanProperty(boundOnAcquire);
        this.noSell = new SimpleBooleanProperty(noSell);
        this.vendorPrice = new SimpleIntegerProperty(vendorPrice);
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

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ItemRarity getRarity() {
        return rarity.get();
    }

    public SimpleObjectProperty<ItemRarity> rarityProperty() {
        return rarity;
    }

    public void setRarity(ItemRarity rarity) {
        this.rarity.set(rarity);
    }

    public String getIconPath() {
        return iconPath.get();
    }

    public SimpleStringProperty iconPathProperty() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath.set(iconPath);
    }

    public boolean isBoundOnAcquire() {
        return boundOnAcquire.get();
    }

    public SimpleBooleanProperty boundOnAcquireProperty() {
        return boundOnAcquire;
    }

    public void setBoundOnAcquire(boolean boundOnAcquire) {
        this.boundOnAcquire.set(boundOnAcquire);
    }

    public boolean isNoSell() {
        return noSell.get();
    }

    public SimpleBooleanProperty noSellProperty() {
        return noSell;
    }

    public void setNoSell(boolean noSell) {
        this.noSell.set(noSell);
    }

    public int getVendorPrice() {
        return vendorPrice.get();
    }

    public SimpleIntegerProperty vendorPriceProperty() {
        return vendorPrice;
    }

    public void setVendorPrice(int vendorPrice) {
        this.vendorPrice.set(vendorPrice);
    }

    public void waitData() throws InterruptedException {
        if (waitDataSignal != null)
            waitDataSignal.await();
    }

    public void onDataComplete() {
        if (waitDataSignal != null)
            waitDataSignal.countDown();
    }

    public void resetWaitData() {
        if (waitDataSignal != null && waitDataSignal.getCount() == 1) {
            waitDataSignal.countDown();
        }
        waitDataSignal = new CountDownLatch(1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Item item = (Item) o;
        return Objects.equals(id.get(), item.id.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.get());
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name=" + name +
                ", rarity=" + rarity +
                ", iconPath=" + iconPath +
                ", boundOnAcquire=" + boundOnAcquire +
                ", noSell=" + noSell +
                ", vendorPrice=" + vendorPrice +
                '}';
    }
}
