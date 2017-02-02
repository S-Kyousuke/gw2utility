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

package th.skyousuke.gw2utility.util.gson;


import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import th.skyousuke.gw2utility.datamodel.AccountData;
import th.skyousuke.gw2utility.datamodel.Currency;
import th.skyousuke.gw2utility.datamodel.Item;
import th.skyousuke.gw2utility.util.FileHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Map;

public class GsonHelper {

    // @formatter:off
    public static final Type itemDataType = new TypeToken<Map<Integer, Item>>() {}.getType();
    public static final Type currencyDataType = new TypeToken<Map<Integer, Currency>>() {}.getType();
    // @formatter:on

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(itemDataType, new ItemDataSerializer())
            .registerTypeAdapter(itemDataType, new ItemDataDeserializer())
            .registerTypeAdapter(currencyDataType, new CurrencyDataSerializer())
            .registerTypeAdapter(currencyDataType, new CurrencyDataDeserializer())
            .registerTypeAdapter(AccountData.class, new AccountDataSerializer())
            .setPrettyPrinting().create();

    public static final JsonParser jsonParser = new JsonParser();

    private static final String READING_WARNING_MESSAGE = "Exception while reading json file: ";

    private GsonHelper() {
    }

    /**
     * Serializes the specified object into the json file.
     *
     * @param object   the json object to write
     * @param filePath the file to write
     * @return true    if the file has been successfully written and false otherwise.
     * @see Gson#toJson(Object, Appendable)
     */
    public static boolean writeJsonToFile(Object object, String filePath) {
        return writeJsonToFile(object, null, filePath);
    }

    /**
     * Serializes the specified object,including those of generic types, into the json file.
     *
     * @param object   the json object to write
     * @param typeOfT  The specific genericized type of object.
     * @param filePath the file to write
     * @return true    if the file has been successfully written and false otherwise.
     * @see Gson#toJson(Object, Appendable)
     */
    public static boolean writeJsonToFile(Object object, Type typeOfT, String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            if (typeOfT != null)
                gson.toJson(object, typeOfT, writer);
            else
                gson.toJson(object, writer);
            return true;
        } catch (Exception e) {
            Log.warn("Exception while writing json data: " + filePath + System.lineSeparator() + object, e);
            return false;
        }
    }


    public static JsonObject readJsonFromFile(String filePath) {
        if (!FileHelper.exists(filePath)) {
            return null;
        }
        try (Reader reader = new FileReader(filePath)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return GsonHelper.jsonParser.parse(bufferedReader).getAsJsonObject();
        } catch (Exception e) {
            Log.warn(READING_WARNING_MESSAGE + filePath, e);
            return null;
        }
    }

    /**
     * Deserializes the json read from the file into an object of the specified class.
     *
     * @param <T>      the type of the desired object
     * @param filePath the path of the json file
     * @param classOfT the class of T
     * @return an object of type T. Return null if there was a problem or json is at EOF.
     * @see Gson#fromJson(Reader, Class)
     */
    public static <T> T readJsonFromFile(String filePath, Class<T> classOfT) {
        if (!FileHelper.exists(filePath)) {
            return null;
        }
        try (Reader reader = new FileReader(filePath)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return GsonHelper.gson.fromJson(bufferedReader, classOfT);
        } catch (Exception e) {
            Log.warn(READING_WARNING_MESSAGE + filePath, e);
            return null;
        }
    }

    /**
     * Deserializes the json read from the file into an object of the specified type.
     *
     * @param <T>      the type of the desired object
     * @param filePath the path of the json file
     * @param typeOfT  the specific genericized type
     * @return an object of type T. Return null if there was a problem or json is at EOF.
     * @see Gson#fromJson(String, Type)
     */
    public static <T> T readJsonFromFile(String filePath, Type typeOfT) {
        if (!FileHelper.exists(filePath)) {
            return null;
        }
        try (Reader reader = new FileReader(filePath)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return GsonHelper.gson.fromJson(bufferedReader, typeOfT);
        } catch (Exception e) {
            Log.warn(READING_WARNING_MESSAGE + filePath, e);
            return null;
        }
    }

    public static Map<Integer, Item> readItemDataFromFile(String filePath) {
        return readJsonFromFile(filePath, itemDataType);
    }

    public static Map<Integer, Currency> readCurrencyDataFromFile(String filePath) {
        return readJsonFromFile(filePath, currencyDataType);
    }

    public static boolean writeItemDataToFile(Map<Integer, Item> items, String filePath) {
        return writeJsonToFile(items, itemDataType, filePath);
    }

    public static boolean writeCurrencyDataToFile(Map<Integer, Currency> currencies, String filePath) {
        return writeJsonToFile(currencies, currencyDataType, filePath);
    }
}
