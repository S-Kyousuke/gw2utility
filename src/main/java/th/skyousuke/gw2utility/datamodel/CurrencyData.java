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

import com.esotericsoftware.minlog.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import th.skyousuke.gw2utility.util.DownloadListener;
import th.skyousuke.gw2utility.util.Downloader;
import th.skyousuke.gw2utility.util.FileHelper;
import th.skyousuke.gw2utility.util.Gw2Api;
import th.skyousuke.gw2utility.util.gson.GsonHelper;

import java.util.Map;

public class CurrencyData {

    private static final String LOADING_CURRENCY_NAME = "Loading...";

    private static final String IMAGE_DIR = "currencies";
    private static final String DATA_FILE = "currencies.json";

    private static final CurrencyData instance = new CurrencyData();

    private final ObservableMap<Integer, Currency> currencies = FXCollections.observableHashMap();

    private CurrencyData() {
    }

    public static CurrencyData getInstance() {
        return instance;
    }

    public ObservableMap<Integer, Currency> getCurrencies() {
        return currencies;
    }

    public Currency getCurrency(int id) {
        Currency currencyInDatabase = currencies.get(id);
        if (isCorrectCurrency(currencyInDatabase)) {
            return currencyInDatabase;
        }
        else {
            Currency currency = new Currency(id, LOADING_CURRENCY_NAME, "");
            downloadCurrencyInfo(currency);
            currencies.put(id, currency);
            return currency;
        }
    }

    private void downloadCurrencyInfo(Currency currency) {
        new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Currency downloadedCurrency = Gw2Api.getInstance().getCurrency(currency.getId());
                currency.setName(downloadedCurrency.getName());
                final String fileURL = downloadedCurrency.getIconPath();
                Downloader.startDownloadTask(fileURL, IMAGE_DIR, new DownloadListener() {
                    @Override
                    public void onProgressUpdate(int percentComplete) {
                        // do nothing
                    }

                    @Override
                    public void finishDownloading(String filePath) {
                        if (filePath != null) {
                            currency.setIconPath(filePath);
                            saveData();
                        } else {
                            downloadCurrencyInfo(currency);
                        }
                    }
                });
                return null;
            }
        }).start();
    }

    private boolean isCorrectCurrency(Currency currency) {
        return currency != null
                && !currency.getName().equals(LOADING_CURRENCY_NAME)
                && FileHelper.exists(currency.getIconPath());
    }

    private void saveData() {
        if (!GsonHelper.writeCurrencyDataToFile(currencies, DATA_FILE)) {
            Log.error("Couldn't save currency data!");
        }
    }

    public void loadData() {
        Map<Integer, Currency> loadedCurrencies = GsonHelper.readCurrencyDataFromFile(DATA_FILE);
        if (loadedCurrencies != null) {
            currencies.clear();
            currencies.putAll(loadedCurrencies);
        }
    }
}
