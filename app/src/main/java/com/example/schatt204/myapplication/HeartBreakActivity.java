package com.example.schatt204.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by schatt204 on 7/17/17.
 */

public class HeartBreakActivity extends Activity {

    private class LongOperation extends AsyncTask<String, Void, String> {

        private String response = "";
        @Override
        protected String doInBackground(String... params) {

            String str = DownloadText("https://hindi.shayari4lovers.com/");

            Document doc = Jsoup.parse(str);
            Elements paragraphs = doc.select("p");
            String ans = "";
            for (Element p : paragraphs)
            {
                ans = ans + p.ownText() + "\n";
            }
            this.response = ans;
            return ans;
        }

        @Override
        protected void onPostExecute(String result) {
            final ScrollView scrollView = (ScrollView) findViewById(R.id.HeartBreakShayariscrollView);
            TextView txt = (TextView) findViewById(R.id.heartbreakShayariText);
            txt.setText(this.response);

            txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartbreak);

        new LongOperation().execute("");

        /*TextView txt = (TextView) findViewById(R.id.HappyShayaritextView3);
        txt.setText(str);*/
    }

    private String DownloadText(String URL)
    {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e1) {
// TODO Auto-generated catch block
            e1.printStackTrace();
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {
//---convert the chars to a String---
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        return str;
    }

    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            response = httpConn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
            else
            {
                System.out.println("Didnt work!!");
                in = null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new IOException("Error connecting");
        }
        return in;
    }
}