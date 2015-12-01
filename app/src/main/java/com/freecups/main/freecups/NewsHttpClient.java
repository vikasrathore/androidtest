package com.freecups.main.freecups;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by vikasrathour on 30/09/15.
 */
public class NewsHttpClient {
    static NewsHttpClient newsHttpClient;
    String TAG = "NewsHttpClient";


    public NewsHttpClient() {
    }

    public static NewsHttpClient getInstance() {
        if (newsHttpClient == null)
            newsHttpClient = new NewsHttpClient();
        return newsHttpClient;
    }

    private String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    void sendNewsRequest(String httpURL,JSONObject jsonData) {

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        URL url = null;

        try {
            url = new URL(httpURL);
        } catch (MalformedURLException e) {
            Log.i(TAG + "ERROR IN URL Creation", e.getMessage());
        }
        Log.i(TAG + "URL :::: ", httpURL);
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            Log.i(TAG + "ERROR IN Open Con", e.getMessage());
        }

        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(false);
        httpURLConnection.setUseCaches(false);


        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(httpURLConnection.getOutputStream ());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data =jsonData.toString();

        try {
            wr.writeBytes(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            httpURLConnection.connect();
        } catch (IOException e) {
            Log.i(TAG + "ERROR IN CONNECT", e.getMessage());
        }



        int contentLength = httpURLConnection.getContentLength();
        int responseCode= 0;
        try {
            responseCode = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseCode==200) {
            Log.i("Login  :: ", "Success responseCode== " + responseCode);
        }else
        {
            Log.i("Login  :: ", "Failed responseCode== " +responseCode);
        }

            try {
            inputStream = httpURLConnection.getInputStream();
            data = convertInputStreamToString(inputStream);
          //process data

        } catch (IOException e) {
            Log.i(TAG + "ERROR IN Get Stream", e.getMessage());
        } finally {
            closeConnection(httpURLConnection, inputStream, null);
            data = null;
        }

    }

    private void closeConnection(HttpURLConnection con, InputStream is,
                                 OutputStream os) {
        if (con != null) {
            try {
                con.disconnect();
            } catch (Exception e) {
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        if (os != null) {
            try {
                os.close();
            } catch (Exception e) {
            }
        }
    }


}
