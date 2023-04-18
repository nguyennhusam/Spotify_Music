package hcmute.edu.vn.musicmediaplayer.Activity;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... urls) {
        try {
            String url = urls[0];
            // Get the filename from the url
            String fileName = url.substring(url.lastIndexOf('/') + 1);

            // Create a new folder for the mp3 file
            File musicDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "Test");
            if (!musicDirectory.exists()) {
                musicDirectory.mkdirs();
            }

            // Create a new file for the mp3 file
            File outputFile = new File(musicDirectory, fileName);

            // Set up the input and output streams
            InputStream inputStream = new URL(url).openStream();
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            // Copy the input stream to the output stream
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Display a message indicating that the mp3 file was successfully downloaded
    }
}
