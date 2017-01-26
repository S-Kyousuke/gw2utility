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

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Character {
    private SimpleStringProperty name;
    private SimpleObjectProperty<Equipment> equipment;
    private ObservableList<Bag> bags;

    public Character(String name, Equipment equipment, ObservableList<Bag> bags) {
        this.name = new SimpleStringProperty(name);
        this.equipment = new SimpleObjectProperty<>(equipment);
        this.bags = bags;
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

    public Equipment getEquipment() {
        return equipment.get();
    }

    public SimpleObjectProperty<Equipment> equipmentProperty() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment.set(equipment);
    }

    public ObservableList<Bag> getBags() {
        return bags;
    }

    public void setBags(ObservableList<Bag> bags) {
        this.bags = bags;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name=" + name +
                ", equipment=" + equipment +
                ", bags=" + bags +
                '}';
    }
}
