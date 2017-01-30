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
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Currency {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty iconPath;

    public Currency(int id, String name, String iconPath) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.iconPath = new SimpleStringProperty(iconPath);
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

    public String getIconPath() {
        return iconPath.get();
    }

    public SimpleStringProperty iconPathProperty() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath.set(iconPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Currency currency = (Currency) o;
        return Objects.equals(id.get(), currency.id.get()) &&
                Objects.equals(name.get(), currency.name.get()) &&
                Objects.equals(iconPath.get(), currency.iconPath.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.get(), name.get(), iconPath.get());
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id.get() +
                ", name=" + name.get() +
                ", iconPath=" + iconPath.get() +
                '}';
    }
}
