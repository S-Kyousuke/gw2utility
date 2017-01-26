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

import java.util.HashMap;
import java.util.Map;

public class CacheService {

    private static final Map<String, Image> imageCache = new HashMap<>();

    private CacheService() {
    }

    public static synchronized Image getImage(String imagePath) {
        if (imageCache.containsKey(imagePath)) {
            return imageCache.get(imagePath);
        }
        Image image = new Image(imagePath);
        imageCache.put(imagePath, image);
        return image;
    }

    public static synchronized void createImageCached(String imagePath) {
        imageCache.put(imagePath, new Image(imagePath));
    }


}
