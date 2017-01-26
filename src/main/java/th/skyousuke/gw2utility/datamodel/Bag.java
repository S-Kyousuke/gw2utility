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

public class Bag {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty size;
    private SimpleObjectProperty<Inventory> inventory;

    public Bag(int id, int size, Inventory inventory) {
        this.id = new SimpleIntegerProperty(id);
        this.size = new SimpleIntegerProperty(size);
        this.inventory = new SimpleObjectProperty<>(inventory);
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

    public int getSize() {
        return size.get();
    }

    public SimpleIntegerProperty sizeProperty() {
        return size;
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public Inventory getInventory() {
        return inventory.get();
    }

    public SimpleObjectProperty<Inventory> inventoryProperty() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory.set(inventory);
    }

    @Override
    public String toString() {
        return "Bag{" +
                "id=" + id +
                ", size=" + size +
                ", inventory=" + inventory +
                '}';
    }
}
