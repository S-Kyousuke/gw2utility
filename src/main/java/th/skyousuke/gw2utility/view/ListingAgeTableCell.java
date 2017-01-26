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

import javafx.geometry.Insets;
import javafx.scene.control.TableCell;

public class ListingAgeTableCell<T> extends TableCell<T, Long> {

    public ListingAgeTableCell() {
        setPadding(new Insets(0, 10, 0, 10));
    }

    @Override
    protected void updateItem(Long ageInSeconds, boolean empty) {
        if (empty || ageInSeconds == null) {
            setText(null);
        } else {
            int hours = (int) (ageInSeconds / 3600);
            long remaining = ageInSeconds % 3600;
            int minutes = (int) (remaining / 60);
            int seconds = (int) (remaining % 60);
            String hoursText = (hours < 10 ? "0" : "") + hours;
            String minutesText = (minutes < 10 ? "0" : "") + minutes;
            String secondsText = (seconds < 10 ? "0" : "") + seconds;
            setText(hoursText + ':' + minutesText + ':' + secondsText);
        }
    }
}
