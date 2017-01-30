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
import th.skyousuke.gw2utility.util.Downloader;
import th.skyousuke.gw2utility.util.FileHelper;
import th.skyousuke.gw2utility.util.Gw2Api;
import th.skyousuke.gw2utility.util.gson.GsonHelper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CurrencyData {

    private static final String LOADING_CURRENCY_NAME = "Loading...";

    private static final String IMAGE_DIR = "currencies";
    private static final String DATA_FILE = "currencies.json";

    private static final CurrencyData instance = new CurrencyData();

    private ExecutorService executor = Executors.newCachedThreadPool();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Currency downloadedCurrency = Gw2Api.getInstance().getCurrency(currency.getId());
                currency.setName(downloadedCurrency.getName());
                final String iconURL = downloadedCurrency.getIconPath();
                if (iconURL != null) {
                    try {
                        final String iconPath = Downloader.download(iconURL, IMAGE_DIR, null);
                        currency.setIconPath(iconPath);
                        saveData();
                        return;
                    } catch (IOException e) {
                        Log.warn("Exception while downloading currency icon", e);
                    }
                }
                scheduler.schedule(this, 5, TimeUnit.SECONDS);
            }
        });
    }

    private boolean isCorrectCurrency(Currency currency) {
        return currency != null
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

    public void stopUpdateService() {
        executor.shutdown();
        scheduler.shutdown();
    }
}
