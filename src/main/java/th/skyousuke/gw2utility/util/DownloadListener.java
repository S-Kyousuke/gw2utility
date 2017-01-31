package th.skyousuke.gw2utility.util;

public interface DownloadListener {

    void onProgressUpdate(int percentComplete);

    void finishDownloading(String downloadedFilePath);

}
