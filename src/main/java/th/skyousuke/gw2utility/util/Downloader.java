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

import com.esotericsoftware.minlog.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Downloader {

    private static final int CONNECTION_TIMEOUT_SECOND = 10;
    private static final int READ_TIMEOUT_SECOND = 10;

    private static final int SECOND_TO_MILLISECOND = 1000;

    private static final int BUFFER_SIZE = 4096;

    private Downloader() {
    }

    /**
     * Quietly download file from an HTTP URL in the background. Cancel download task if timeout.
     *
     * @return download task
     * @see Downloader#download(String, String, DownloadListener)
     */
    public static Future<Void> startDownloadTask(String fileURL, String destinationDirectory, DownloadListener downloadListener) {
        final ExecutorService executor = Executors.newFixedThreadPool(1);
        final Future<Void> future = executor.submit(() -> {
            String filePath = null;
            try {
                filePath = download(fileURL, destinationDirectory, downloadListener);
            } catch (Exception e) {
                Log.warn("Exception while download file " +
                        fileURL + " to " + destinationDirectory, e);
            }
            if (downloadListener != null) {
                downloadListener.finishDownloading(filePath);
            }
            return null;
        });
        executor.shutdown();
        return future;
    }

    /**
     * Download file from an HTTP URL. If the file exists, overwrite an existing file.
     *
     * @param fileURL              file URL to download
     * @param destinationDirectory destination directory
     * @param downloadListener     download listener
     * @return downloaded          file path if the file has been downloaded successfully and null otherwise.
     * @throws IOException if an I/O exception occurs.
     */
    public static String download(String fileURL, String destinationDirectory, DownloadListener downloadListener)
            throws IOException {
        URL url = new URL(fileURL);

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT_SECOND * SECOND_TO_MILLISECOND);
        httpURLConnection.setReadTimeout(READ_TIMEOUT_SECOND * SECOND_TO_MILLISECOND);

        int responseCode = httpURLConnection.getResponseCode();

        // if the server isn't response OK status, stop downloading.
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return null;
        }

        //setup output file
        String fileName = FilenameUtils.getName(fileURL);
        String saveFilePath = Paths.get(destinationDirectory).toAbsolutePath().toString() + File.separator + fileName;
        File outputFile = new File(saveFilePath);

        // Setup streams
        InputStream inputStream = httpURLConnection.getInputStream();
        FileOutputStream outputStream = FileUtils.openOutputStream(outputFile);

        // Start download file. If thread is interrupted while downloading, stop download.
        final long completeFileSize = httpURLConnection.getContentLength();
        long downloadedFileSize = 0;
        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1 && !Thread.interrupted()) {
            outputStream.write(buffer, 0, bytesRead);
            downloadedFileSize += bytesRead;

            if (downloadListener != null) {
                final int percentComplete = (int) (100 * ((float) downloadedFileSize / completeFileSize));
                downloadListener.onProgressUpdate(percentComplete);
            }
        }

        // Close streams after download complete
        inputStream.close();
        outputStream.close();

        // Return file path if the downloaded the file size is correct, otherwise delete the corrupted file and return null
        if (downloadedFileSize == completeFileSize) {
            return outputFile.getAbsolutePath();
        } else {
            FileUtils.deleteQuietly(outputFile);
            return null;
        }
    }

}