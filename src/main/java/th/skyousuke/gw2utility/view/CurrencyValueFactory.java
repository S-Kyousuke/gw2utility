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

package th.skyousuke.gw2utility.view;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.CurrencyContainer;

public class CurrencyValueFactory<T extends CurrencyContainer> implements Callback<TableColumn.CellDataFeatures<T, Currency>, ObservableValue<Currency>> {

    @Override
    public ObservableValue<Currency> call(TableColumn.CellDataFeatures<T, Currency> param) {
        final Currency currency = param.getValue().getCurrency();
        return Bindings.createObjectBinding(() -> currency, currency.nameProperty(), currency.iconPathProperty());
    }
}
