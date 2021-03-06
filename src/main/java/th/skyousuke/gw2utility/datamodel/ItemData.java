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
import org.apache.commons.io.FilenameUtils;
import th.skyousuke.gw2utility.util.Downloader;
import th.skyousuke.gw2utility.util.FileHelper;
import th.skyousuke.gw2utility.util.Gw2Api;
import th.skyousuke.gw2utility.util.gson.GsonHelper;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Item database class
 */
public class ItemData {

    private static final String IMAGE_DIR = "items";
    private static final String DATA_FILE = "items.json";

    private static final ItemData instance = new ItemData();

    private boolean autoSave = true;

    private ExecutorService executor = Executors.newCachedThreadPool();
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);

    private ObservableMap<Integer, Item> items = FXCollections.observableHashMap();

    private ItemData() {
    }

    public static ItemData getInstance() {
        return instance;
    }

    public ObservableMap<Integer, Item> getItems() {
        return items;
    }

    public Item getItem(int id) {
        Item itemInDatabase = items.get(id);
        if (isCorrectItem(itemInDatabase)) {
            return itemInDatabase;
        } else {
            Item item = new Item(id, "Loading...", ItemRarity.BASIC, "", false, false, 0);
            item.resetWaitData();
            downloadItemInfo(item);
            items.put(id, item);
            return item;
        }
    }

    private boolean isCorrectItem(Item item) {
        return item != null
                && FileHelper.exists(item.getIconPath());
    }

    private void downloadItemInfo(Item item) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                Item downloadedItem = Gw2Api.getInstance().getItem(item.getId());
                if (downloadedItem == null || !setItemInfo(downloadedItem, item)) {
                    scheduledExecutor.schedule(this, 5, TimeUnit.SECONDS);
                }
            }
        });
    }

    private boolean setItemInfo(Item downloadedItem, Item item) {
        item.setName(downloadedItem.getName());
        item.setRarity(downloadedItem.getRarity());
        item.setVendorPrice(downloadedItem.getVendorPrice());
        item.setBoundOnAcquire(downloadedItem.isBoundOnAcquire());
        item.setNoSell(downloadedItem.isNoSell());

        String iconURL = downloadedItem.getIconPath();
        String iconName = FilenameUtils.getName(iconURL);
        String iconPath = IMAGE_DIR + File.separator + iconName;
        if (!FileHelper.exists(iconPath)) {
            iconPath = downloadItemIcon(iconURL);
        }

        if (iconPath != null) {
            item.setIconPath(iconPath);
            item.onDataComplete();
            if (autoSave)
                saveData();
            return true;
        } else {
            return false;
        }
    }

    private String downloadItemIcon(String url) {
        try {
            return Downloader.download(url, IMAGE_DIR, null, false);
        } catch (Exception e) {
            Log.warn("Exception while downloading item icon", e);
            return null;
        }
    }

    public void saveData() {
        synchronized (this) {
            if (!GsonHelper.writeItemDataToFile(items, DATA_FILE)) {
                Log.error("Couldn't save item data!");
            }
        }
    }

    public void loadData() {
        Map<Integer, Item> loadedItems = GsonHelper.readItemDataFromFile(DATA_FILE);
        if (loadedItems != null) {
            items.clear();
            items.putAll(loadedItems);
        }
    }

    public void stopUpdateService() {
        executor.shutdown();
        scheduledExecutor.shutdown();
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void setScheduledExecutor(ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
    }
}
