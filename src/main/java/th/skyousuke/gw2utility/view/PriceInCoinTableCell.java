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
import javafx.scene.paint.Color;

public class PriceInCoinTableCell<T> extends TableCell<T, Integer> {

    private boolean showPlusSign;

    private final CoinView coinView = new CoinView();

    private Color positiveValueColor = Color.WHITE;
    private Color negativeValueColor = Color.WHITE;
    private Color zeroValueColor = Color.WHITE;

    public PriceInCoinTableCell() {
        setPadding(new Insets(0, 10, 0, 0));
    }

    @Override
    protected void updateItem(Integer price, boolean empty) {
        if (empty || price == null) {
            setGraphic(null);
        } else {
            coinView.setShowPlusSign(showPlusSign);
            coinView.setValue(price);
            if (price > 0) {
                coinView.setTextFill(positiveValueColor);
            } else if (price < 0) {
                coinView.setTextFill(negativeValueColor);
            } else {
                coinView.setTextFill(zeroValueColor);
            }
            setGraphic(coinView);
        }
    }

    public void setShowPlusSign(boolean showPlusSign) {
        this.showPlusSign = showPlusSign;
    }

    public void setTextColor(Color zeroValueColor, Color positiveValueColor, Color negativeValueColor) {
        this.zeroValueColor = zeroValueColor;
        this.positiveValueColor = positiveValueColor;
        this.negativeValueColor = negativeValueColor;
    }
}
