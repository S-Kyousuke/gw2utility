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

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class DownloaderTest {

    private static final String DOWNLOAD_TEST_DIRECTORY = "download-test";

    @AfterClass
    public static void cleanUp() {
        FileUtils.deleteQuietly(new File(DOWNLOAD_TEST_DIRECTORY));
    }

    @Test
    public void startDownloadTask_100KbFile_NotNullDownloadedFilePath() throws Exception {
        final String DOWNLOAD_FILE_TEST_URL = "http://speedtest.ftp.otenet.gr/files/test100k.db";
        final DownloadListener downloadListener = new DownloadAdapter() {
            @Override
            public void finishDownloading(String downloadedFilePath) {
                Assert.assertNotNull(downloadedFilePath);
            }
        };
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, DOWNLOAD_TEST_DIRECTORY, downloadListener);
        future.get();
    }

    @Test
    public void startDownloadTask_GoogleLogo_NotNullDownloadedFilePath() throws Exception {
        final String DOWNLOAD_FILE_TEST_URL = "https://www.google.co.th/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
        final DownloadListener downloadListener = new DownloadAdapter() {
            @Override
            public void finishDownloading(String downloadedFilePath) {
                Assert.assertNotNull(downloadedFilePath);
            }
        };
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, DOWNLOAD_TEST_DIRECTORY, downloadListener);
        future.get();
    }

    @Test
    public void startDownloadTask_Save100KbFileToTempFolder_NotNullDownloadedFilePath() throws Exception {
        final String DOWNLOAD_FILE_TEST_URL = "http://speedtest.ftp.otenet.gr/files/test100k.db";
        final DownloadListener downloadListener = new DownloadAdapter() {
            @Override
            public void finishDownloading(String downloadedFilePath) {
                Assert.assertNotNull(downloadedFilePath);
            }
        };
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, System.getProperty("java.io.tmpdir"), downloadListener);
        future.get();
    }

    @Test
    public void startDownloadTask_MalformedURL_NullDownloadedFilePath() throws Exception {
        final String DOWNLOAD_FILE_TEST_URL = "Wrong URL";
        final DownloadListener downloadListener = new DownloadAdapter() {
            @Override
            public void finishDownloading(String downloadedFilePath) {
                Assert.assertNull(downloadedFilePath);
            }
        };
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, DOWNLOAD_TEST_DIRECTORY, downloadListener);
        future.get();
    }

    @Test
    public void startDownloadTask_DeadURL_NullDownloadedFilePath() throws Exception {
        final String DOWNLOAD_FILE_TEST_URL = "http://google.com/deadurl";
        final DownloadListener downloadListener = new DownloadAdapter() {
            @Override
            public void finishDownloading(String downloadedFilePath) {
                Assert.assertNull(downloadedFilePath);
            }
        };
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, DOWNLOAD_TEST_DIRECTORY, downloadListener);
        future.get();
    }

    @Test
    public void startDownloadTask_WrongDirectory_NullDownloadedFilePath() throws Exception {
        final String DOWNLOAD_FILE_TEST_URL = "http://speedtest.ftp.otenet.gr/files/test100k.db";
        final DownloadListener downloadListener = new DownloadAdapter() {
            @Override
            public void finishDownloading(String downloadedFilePath) {
                Assert.assertNull(downloadedFilePath);
            }
        };
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, "//Wrong Directory", downloadListener);
        future.get();
    }

    @Test
    public void startDownloadTask_InterruptTask_NullDownloadedFilePath() throws Exception {
        final CountDownLatch interruptSignal = new CountDownLatch(1);
        final CountDownLatch completeSignal = new CountDownLatch(1);
        final String DOWNLOAD_FILE_TEST_URL = "http://speedtest.ftp.otenet.gr/files/test1Mb.db";
        final DownloadListener downloadListener = new DownloadAdapter() {
            @Override
            public void finishDownloading(String downloadedFilePath) {
                Assert.assertNull(downloadedFilePath);
                completeSignal.countDown();
            }

            @Override
            public void onProgressUpdate(int percentComplete) {
                if (percentComplete >= 50 && interruptSignal.getCount() == 1) {
                    interruptSignal.countDown();
                }
            }
        };
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, DOWNLOAD_TEST_DIRECTORY, downloadListener);
        interruptSignal.await();
        future.cancel(true);
        completeSignal.await();
    }

    @Test
    public void startDownloadTask_EmptyDownloadListener_NullDownloadedFilePath() throws Exception {
        final String DOWNLOAD_FILE_TEST_URL = "http://speedtest.ftp.otenet.gr/files/test100k.db";
        Future<Void> future = Downloader.startDownloadTask(DOWNLOAD_FILE_TEST_URL, DOWNLOAD_TEST_DIRECTORY, new DownloadAdapter());
        future.get();
    }

    @Test
    public void testPrivateConstructors() throws Exception {
        final Constructor<Downloader> constructor = Downloader.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}