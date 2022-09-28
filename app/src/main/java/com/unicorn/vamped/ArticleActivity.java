package com.unicorn.vamped;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.squareup.picasso.Downloader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class ArticleActivity extends AppCompatActivity {
    private TextView articleTitle;
    //private ImageView articleImage;
    private WebView artWebView;
    private LinearProgressIndicator linearProgressIndicator;
    private RequestQueue mQueue;
    private int selectedPosition;
    private WebSettings webSettings;
    //private String artImageUrl, artTitle, artContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initViews();
        linearProgressIndicator.setVisibility(View.VISIBLE);

        SharedPreferences sh = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        int isDarkModeOn = sh.getInt("isDMOn", AppCompatDelegate.MODE_NIGHT_NO);

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            if(isDarkModeOn == AppCompatDelegate.MODE_NIGHT_NO){
                WebSettingsCompat.setForceDark(webSettings, WebSettingsCompat.FORCE_DARK_OFF);
            } else if(isDarkModeOn == AppCompatDelegate.MODE_NIGHT_YES){
                WebSettingsCompat.setForceDark(webSettings, WebSettingsCompat.FORCE_DARK_ON);
            }
        }
        mQueue = Volley.newRequestQueue(this);
    }

    private void initViews() {
        //articleImage = findViewById(R.id.articleImage);
        artWebView = findViewById(R.id.simpleWebView);
        articleTitle = findViewById(R.id.arttitle);
        linearProgressIndicator = findViewById(R.id.linearProgress2);
        webSettings = artWebView.getSettings();
        selectedPosition = getIntent().getIntExtra("selectedArticle", 514);
        //artImageUrl = getIntent().getStringExtra("articleImage");
        //artTitle = getIntent().getStringExtra("articleTitle");
        //artContent = getIntent().getStringExtra("articleContent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        jsonParse();
    }

    private void jsonParse() {
        //https://health.gov/myhealthfinder/api/v3/itemlist.json?Type=topic
        String url = "https://health.gov/myhealthfinder/api/v3/topicsearch.json?TopicId=" + selectedPosition;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject jsonObjectResource = object.getJSONObject("Result")
                            .getJSONObject("Resources")
                            .getJSONArray("Resource")
                            .getJSONObject(0);
                    String title = jsonObjectResource.getString("Title");
                    String url = jsonObjectResource.getString("ImageUrl");
                    JSONArray jsonArrayContent = jsonObjectResource.getJSONObject("Sections").getJSONArray("section");
                    String htmlContent = null, content = null;
                    for (int i = 0; i < jsonArrayContent.length(); i++) {
                        JSONObject jsonContent = jsonArrayContent.getJSONObject(i);
                        if (htmlContent == null) {
                            htmlContent = jsonContent.getString("Content");
                        } else {
                            //htmlContent = htmlContent + "\n\t" + jsonContent.getString("Content");
                            htmlContent = htmlContent + jsonContent.getString("Content");
                        }
                        content =  "<img src=" + "'" + url + "'" + " width='100%' /><br><br>" + htmlContent;
                        //content =  "<img src=" + "'" + artImageUrl + "'" + " width='100%' /><br><br>" + htmlContent;
                    }
                    articleTitle.setText(title);
                    //articleTitle.setText(artTitle);
                    artWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                linearProgressIndicator.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });
        mQueue.add(request);
    }

}