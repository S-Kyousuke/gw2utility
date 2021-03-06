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

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    private static final Path currentAbsolutePath = Paths.get("").toAbsolutePath();

    private FileHelper() {
    }

    public static boolean exists(String filePath) {
        return filePath != null && Paths.get(filePath).toFile().exists();
    }

    public static String getRelativePath(String path) {
        return currentAbsolutePath.relativize(Paths.get(path)).toString();
    }

    public static String getAbsolutePath(String relativePath) {
        return Paths.get(relativePath).toAbsolutePath().toString();
    }
}
