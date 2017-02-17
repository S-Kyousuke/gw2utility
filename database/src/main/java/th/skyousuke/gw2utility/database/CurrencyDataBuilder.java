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

package th.skyousuke.gw2utility.database;

import com.esotericsoftware.minlog.Log;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.CurrencyData;
import th.skyousuke.gw2utility.util.Gw2Api;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CurrencyDataBuilder {

    private CurrencyDataBuilder() {
    }

    public static void main(String[] args) throws InterruptedException {
        // load data and disable auto saving
        CurrencyData.getInstance().loadData();
        CurrencyData.getInstance().setAutoSave(false);

        // increasing download task speed by create custom large executor
        ExecutorService executor = Executors.newFixedThreadPool(32);
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(4);
        CurrencyData.getInstance().setExecutor(executor);
        CurrencyData.getInstance().setScheduledExecutor(scheduledExecutor);

        // fetch all currency id
        Set<Integer> currencyIdSet = Gw2Api.getInstance().getCurrencyIdSet();
        Integer[] currencyIdArray = currencyIdSet.toArray(new Integer[currencyIdSet.size()]);
        int currencyIdArrayLength = currencyIdArray.length;

        // get all currency by id
        Currency[] currencies = new Currency[currencyIdArrayLength];
        for (int i = 0; i < currencyIdArrayLength; i++) {
            int currencyId = currencyIdArray[i];
            currencies[i] = CurrencyData.getInstance().getCurrency(currencyId);
        }
        // wait until all item data download completed
        for (int i = 0; i < currencyIdArrayLength; i++) {
            currencies[i].waitData();
            Log.info("currency " + currencies[i].getId() + " download completed");
        }
        // save data
        CurrencyData.getInstance().saveData();

        // shutdown custom executor
        executor.shutdown();
        scheduledExecutor.shutdown();
    }

}
