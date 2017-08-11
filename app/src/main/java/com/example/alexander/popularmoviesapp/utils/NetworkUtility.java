package com.example.alexander.popularmoviesapp.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by awest on 8/11/2017.
 */

public class NetworkUtility {
    private static final String LOG_TAG = NetworkUtility.class.getSimpleName();

    private NetworkUtility() {
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public static InputStream getInputStreamFromURL(URL createdUrl) throws IOException {
        URL url = createdUrl;
        InputStream is;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10_000 /* milliseconds */);
        conn.setConnectTimeout(15_000 /* milliseconds */);
        conn.setRequestMethod("GET");
        if (conn.getDoInput() != true) {
            conn.setDoInput(true);
        }
        // Starts the query
        conn.connect();
        is = conn.getInputStream();
        if (conn != null) {
            conn.disconnect();
        }
        return is;
    }


    // Reads an InputStream and converts it to a String.
    public static String readInputStream(InputStream stream) throws IOException {

        BufferedReader reader;
        StringBuffer buffer = new StringBuffer();
        if (stream == null) {
            Log.d(LOG_TAG, "Nothing to Download?");
        }
        reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            // Stream was empty, no point in parsing
            Log.v(LOG_TAG, "BUFFER WAS EMPTY!!");
            return null;
        }
        reader.close();
        return buffer.toString();
    }
}