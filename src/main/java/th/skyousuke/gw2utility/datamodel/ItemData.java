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

/**
 * Item database class
 */
public class ItemData {
    private static final int MAX_DOWNLOAD_TRY_COUNT = 3;

    private static final String IMAGE_DIR = "items";
    private static final String DATA_FILE = "items.json";

    private static final ItemData instance = new ItemData();
    private static final String LOADING_ITEM_NAME = "Loading...";

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
        }
        else {
            Item item = new Item(id, LOADING_ITEM_NAME, ItemRarity.BASIC, "");
            downloadItemInfo(item);
            items.put(id, item);
            return item;
        }
    }

    private boolean isCorrectItem(Item item) {
        return item != null
                && !item.getName().equals(LOADING_ITEM_NAME)
                && FileHelper.exists(item.getIconPath());
    }

    private void downloadItemInfo(Item item) {
        new Thread(() -> {
            Item downloadedItem = tryToDownloadItemInfo(item.getId());
            item.setName(downloadedItem.getName());
            item.setRarity(downloadedItem.getRarity());

            final String iconURL = downloadedItem.getIconPath();
            final String iconPath = tryToDownloadItemIcon(iconURL);
            item.setIconPath(iconPath);
            saveData();
        }).start();
    }

    private Item tryToDownloadItemInfo(int itemId) {
        Item item = null;
        int tryCount = 0;
        while (item == null && tryCount != MAX_DOWNLOAD_TRY_COUNT) {
            item = Gw2Api.getInstance().getItem(itemId);
            tryCount++;
        }
        return item;
    }

    private String tryToDownloadItemIcon(String iconUrl) {
        String iconPath = null;
        int tryCount = 0;
        while (iconPath == null && tryCount != MAX_DOWNLOAD_TRY_COUNT) {
            try {
                iconPath = Downloader.download(iconUrl, IMAGE_DIR, null);
            } catch (IOException e) {
                Log.warn("Exception while try to downloading item icon!", e);
                break;
            }
            tryCount++;
        }
        return iconPath;
    }

    private void saveData() {
        if (!GsonHelper.writeItemDataToFile(items, DATA_FILE)) {
            Log.error("Couldn't save item data!");
        }
    }

    public void loadData() {
        Map<Integer, Item> loadedItems = GsonHelper.readItemDataFromFile(DATA_FILE);
        if (loadedItems != null) {
            items.clear();
            items.putAll(loadedItems);
        }
    }
}
