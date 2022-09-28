package com.unicorn.vamped;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment implements ArticleAdapter.ListItemClickListener{
    private List<Article> articleList = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private RequestQueue mQueue;
    private RecyclerView recyclerView;
    private LinearProgressIndicator linearProgressIndicator;
    private ArticleAdapter articleAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        recyclerView = view.findViewById(R.id.RVArticle);
        linearProgressIndicator = view.findViewById(R.id.linearProgress);
        linearProgressIndicator.setVisibility(View.VISIBLE);
        initView();
        getIdList();
        mQueue = Volley.newRequestQueue(getContext());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        jsonParse();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());//Parameter: context, number of columns
        recyclerView.setLayoutManager(layoutManager);
        articleAdapter = new ArticleAdapter(articleList, this);
        recyclerView.setAdapter(articleAdapter);
    }

    private void jsonParse() {
        //https://health.gov/myhealthfinder/api/v3/itemlist.json?Type=topic
        for (int i = 0; i < idList.size(); i++) {
            final int artUrl = idList.get(i);
            String url = "https://health.gov/myhealthfinder/api/v3/topicsearch.json?TopicId=" + artUrl;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    linearProgressIndicator.setVisibility(View.INVISIBLE);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject jsonObjectResource = object.getJSONObject("Result")
                                .getJSONObject("Resources")
                                .getJSONArray("Resource")
                                .getJSONObject(0);
                        String title = jsonObjectResource.getString("Title");
                        String url = jsonObjectResource.getString("ImageUrl");
                        JSONArray jsonArrayContent = jsonObjectResource.getJSONObject("Sections").getJSONArray("section");
                        String htmlContent = null, content = null, twoWordsName = null;
                        for (int i = 0; i < jsonArrayContent.length(); i++) {
                            JSONObject jsonContent = jsonArrayContent.getJSONObject(i);
                            if (htmlContent == null) {
                                htmlContent = jsonContent.getString("Content");
                            } else {
                                htmlContent = htmlContent + "\n" + jsonContent.getString("Content");
                            }
                            content = Html.fromHtml(Html.fromHtml(htmlContent).toString()).toString();
                            //twoWordsName = content.substring(0, content.indexOf('.', content.indexOf(' ') + 1)) + ".";
                        }
                        //initArticle(title, twoWordsName, url);
                        initArticle(title, content, url, artUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void initArticle(String name, String content, String url, int artUrl) {
        articleList.add(new Article(name, content, url, artUrl));
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(int position) {
        int myposition = articleList.get(position).getArtUrl();
        Intent artIntent = new Intent(getContext(), ArticleActivity.class);
        artIntent.putExtra("selectedArticle", myposition);
        startActivity(artIntent);
    }

    public void getIdList() {
        idList.clear();
        idList.add(30533);
        idList.add(30537);
        idList.add(30538);
        idList.add(30539);
        idList.add(30540);
        idList.add(30543);
        idList.add(30545);
        idList.add(30546);
        idList.add(30547);
        idList.add(30548);
        idList.add(30551);
        idList.add(30558);
        idList.add(30560);
        idList.add(30562);
        idList.add(30564);
        idList.add(30565);
        idList.add(350);
        idList.add(514);
        idList.add(531);
        idList.add(533);
        idList.add(536);
        idList.add(540);
        idList.add(547);
        idList.add(552);
        idList.add(30588);
        idList.add(30589);
        idList.add(30593);
        idList.add(30598);
        idList.add(30612);
        idList.add(30613);
        idList.add(30614);
        idList.add(33303);
        idList.add(33304);
    }
}
