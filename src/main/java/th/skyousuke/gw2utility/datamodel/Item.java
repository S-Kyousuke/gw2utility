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
import javafx.beans.property.SimpleStringProperty;
import th.skyousuke.gw2utility.util.CacheService;

import java.util.Objects;
import java.util.Observable;

public class Item extends Observable {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleObjectProperty<ItemRarity> rarity;
    private SimpleStringProperty iconPath;

    public Item(int id, String name, ItemRarity rarity, String iconPath) {
        this.id = new SimpleIntegerProperty(id);
        this.name =  new SimpleStringProperty(name);
        this.rarity = new SimpleObjectProperty<>(rarity);
        this.iconPath = new SimpleStringProperty();
        this.iconPath.addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                CacheService.createImageCached("file:" + newValue);
        });
        this.iconPath.set(iconPath);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
        setChanged();
        notifyObservers();
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
        setChanged();
        notifyObservers();
    }

    public ItemRarity getRarity() {
        return rarity.get();
    }

    public SimpleObjectProperty<ItemRarity> rarityProperty() {
        return rarity;
    }

    public void setRarity(ItemRarity rarity) {
        this.rarity.set(rarity);
        setChanged();
        notifyObservers();
    }

    public String getIconPath() {
        return iconPath.get();
    }

    public SimpleStringProperty iconPathProperty() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath.set(iconPath);
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Item item = (Item) o;
        return Objects.equals(id.get(), item.id.get()) &&
                Objects.equals(name.get(), item.name.get()) &&
                Objects.equals(rarity.get(), item.rarity.get()) &&
                Objects.equals(iconPath.get(), item.iconPath.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.get(), name.get(), rarity.get(), iconPath.get());
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", rarity=" + rarity.get() +
                ", iconPath='" + iconPath.get() + '\'' +
                '}';
    }


}
