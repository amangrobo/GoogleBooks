package com.grobo.googlebooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    public static List<Book> doSearch(String constructedUrl) {

        URL url = createUrl(constructedUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> bookList = extractDataFromJson(jsonResponse);
        return bookList;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;

        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readJsonFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving json result", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readJsonFromStream(InputStream inputStream) throws IOException {
        StringBuilder outputJson = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null) {
                outputJson.append(line);
                line = bufferedReader.readLine();
            }
        }
        return outputJson.toString();
    }

    private static List<Book> extractDataFromJson(String jsonResponse){
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        List<Book> bookList = new ArrayList<Book>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray books = root.getJSONArray("items");
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                JSONObject info = book.getJSONObject("volumeInfo");
                String title = info.getString("title");
                String description = info.getString("description");

                JSONArray authors = info.getJSONArray("authors");
                String author = authors.getString(0);

                Book bookObject = new Book(title,author,description);

                bookList.add(bookObject);
            }

        }catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return bookList;
    }
}
