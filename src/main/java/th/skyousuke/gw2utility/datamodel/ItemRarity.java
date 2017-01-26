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

import javafx.scene.paint.Color;

public enum ItemRarity {

    JUNK,
    BASIC,
    FINE,
    MASTERWORK,
    RARE,
    EXOTIC,
    ASCENDED,
    LEGENDARY;

    private static final ItemRarity[] values = ItemRarity.values();

    public Color getNameColor() {
        switch (this) {
            case JUNK:
                return Color.rgb(170, 170, 170);
            case BASIC:
                return Color.rgb(255, 255, 255);
            case FINE:
                return Color.rgb(98, 164, 218);
            case MASTERWORK:
                return Color.rgb(26, 147, 6);
            case RARE:
                return Color.rgb(252, 208, 11);
            case EXOTIC:
                return Color.rgb(255, 164, 5);
            case ASCENDED:
                return Color.rgb(251, 62, 141);
            case LEGENDARY:
                return Color.rgb(76, 19, 157);
        }
        return null;
    }

    public static ItemRarity fromString(String text) {
        if (text != null) {
            for (ItemRarity itemRarity : values) {
                if (text.equalsIgnoreCase(itemRarity.name())) {
                    return itemRarity;
                }
            }
        }
        throw new IllegalArgumentException("No ItemRarity found: " + text);
    }


    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
