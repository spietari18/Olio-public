package net.kirte;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

// Https connection to server with asynchronous task. Called from DBConnection
public class AsyncConnection extends AsyncTask<String, String, String> {
    private ProgressDialog dialog;

    // Show loading dialog
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = MainActivity.getInstance().showLoading();
    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length == 2) {
            return getJSON(strings[0], strings[1]);
        } else if (strings.length == 5) {
            return getJSON(strings[0], strings[1], strings[2], strings[3], strings[4]);
        }
        else {
            return null;
        }
    }


    // Hide loading dialog
    @Override
    protected void onPostExecute(final String ret) {
        MainActivity.getInstance().hideLoading(dialog);
    }

    // Get JSON for login only
    public String getJSON(String string_url, String inputString) {
        String response = null;
        try {
            URL url = new URL(string_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (inputString.isEmpty()) {
                connection.setRequestMethod("GET");
            } else {
                connection.setRequestMethod("POST");
            }
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            OutputStream os = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            if (!inputString.isEmpty()) {
                System.out.println("Sending data...");
                bw.write(inputString);
            }
            bw.close();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            System.out.println("Starting to read data...");
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                System.out.println(line);
            }
            in.close();
            os.close();
            response = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    // Get JSON when logged in, sets session and userId to header.
    public String getJSON(String string_url, String inputString, String session, String userId, String method) {
        String response = null;
        try {
            URL url = new URL(string_url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("X-Booked-SessionToken", session);
            connection.setRequestProperty("X-Booked-UserId", userId);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            if (inputString != null) {
                connection.setDoOutput(true);
                OutputStream os = new BufferedOutputStream(connection.getOutputStream());
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                System.out.println("Sending data...");
                bw.write(inputString);
                bw.close();
            }
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            System.out.println("Starting to read data...");
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line).append("\n");
                System.out.println(line);
            }
            in.close();
            response = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
