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

package th.skyousuke.gw2utility.util;

import javafx.scene.image.Image;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheService {

    private static final ConcurrentMap<String, Image> imageCache = new ConcurrentHashMap<>();

    private static final ConcurrentMap<Integer, Integer> itemSellPrice = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, LocalDateTime> itemSellPriceLastUpdate = new ConcurrentHashMap<>();

    private static final ConcurrentMap<Integer, Integer> itemBuyPrice = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Integer, LocalDateTime> itemBuyPriceLastUpdate = new ConcurrentHashMap<>();

    private CacheService() {
    }

    public static Image getImage(String imagePath) {
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }
        Image image = new Image(imagePath);
        imageCache.put(imagePath, image);
        return image;
    }

    public static int getItemSellPrice(int itemId) {
        if (itemSellPrice.containsKey(itemId)) {
            final LocalDateTime lastUpdateTime = itemSellPriceLastUpdate.get(itemId);
            if (lastUpdateTime.until(LocalDateTime.now(), ChronoUnit.MINUTES) < 6) {
                return itemSellPrice.get(itemId);
            }
        }
        final Integer sellPrice = Gw2Api.getInstance().getItemSellPrice(itemId);
        final LocalDateTime currentTime = LocalDateTime.now();
        itemSellPrice.put(itemId, sellPrice);
        itemSellPriceLastUpdate.put(itemId, currentTime);
        return sellPrice;
    }

    public static int getItemBuyPrice(int itemId) {
        if (itemBuyPrice.containsKey(itemId)) {
            final LocalDateTime lastUpdateTime = itemBuyPriceLastUpdate.get(itemId);
            if (lastUpdateTime.until(LocalDateTime.now(), ChronoUnit.MINUTES) < 6) {
                return itemBuyPrice.get(itemId);
            }
        }
        final Integer buyPrice = Gw2Api.getInstance().getItemBuyPrice(itemId);
        final LocalDateTime currentTime = LocalDateTime.now();
        itemBuyPrice.put(itemId, buyPrice);
        itemBuyPriceLastUpdate.put(itemId, currentTime);
        return buyPrice;
    }

}
