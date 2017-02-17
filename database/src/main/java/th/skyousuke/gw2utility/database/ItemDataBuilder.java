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
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.datamodel.ItemData;
import th.skyousuke.gw2utility.util.Gw2Api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ItemDataBuilder {

    private ItemDataBuilder() {
    }

    public static void main(String[] args) throws InterruptedException {
        // load data and disable auto saving
        ItemData.getInstance().loadData();
        ItemData.getInstance().setAutoSave(false);

        // increasing download task speed by create custom large executor
        ExecutorService executor = Executors.newFixedThreadPool(256);
        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(32);
        ItemData.getInstance().setExecutor(executor);
        ItemData.getInstance().setScheduledExecutor(scheduledExecutor);

        // fetch all item id
        int[] itemIdArray = Gw2Api.getInstance().getItemIdArray();
        int itemIdArrayLength = itemIdArray.length;

        // get all item by id
        Item[] items = new Item[itemIdArrayLength];
        for (int i = 0; i < itemIdArrayLength; i++) {
            int itemId = itemIdArray[i];
            items[i] = ItemData.getInstance().getItem(itemId);
        }
        // wait until all item data download completed
        for (int i = 0; i < itemIdArrayLength; i++) {
            items[i].waitData();
            Log.info("item " + items[i].getId() + " download completed");
        }
        // save data
        ItemData.getInstance().saveData();

        // shutdown custom executor
        executor.shutdown();
        scheduledExecutor.shutdown();
    }
}
