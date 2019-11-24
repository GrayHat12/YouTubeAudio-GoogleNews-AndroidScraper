package com.grayhat.graybot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;


public class GoogleNews extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GoogleNews() {
        // Required empty public constructor
    }

    SearchView searchView;
    private View fragmentView;
    ListView listView;
    private AsyncHttpClient client;
    GoogleAdapter adapter;
    ArrayList<HashMap<String, String>> newsList ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(fragmentView!=null)
            return fragmentView;
        fragmentView = inflater.inflate(R.layout.fragment_google_news, container, false);
        searchView = fragmentView.findViewById(R.id.searchViewG);
        listView = fragmentView.findViewById(R.id.listViewG);
        client = new AsyncHttpClient();
        client.setTimeout(60*1000);
        client.setConnectTimeout(60*1000);
        client.setResponseTimeout(60*1000);
        client.setEnableRedirects(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try{
                    System.out.println("SEARCH CLICKED "+query);
                    makeSearch(query);
                    Log.d("searchQuery",query);
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                openBrowser(getContext(),newsList.get(position).get("LINK"));
            }
        });
        return fragmentView;
    }
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";

    public static void openBrowser(final Context context, String url) {

        if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
            url = HTTP + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(Intent.createChooser(intent, "Choose browser"));// Choose browser is arbitrary :)

    }
    private void makeSearch(String search) throws Exception {
        newsList = new ArrayList<HashMap<String, String>>();
        Log.d("Search",search);
        System.out.println(search);
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("search", search);
        StringEntity entity = new StringEntity(jsonParams.toString());
        entity.setContentType(new BasicHeader(cz.msebera.android.httpclient.protocol.HTTP.CONTENT_TYPE, "application/json"));
        client.post(getContext(), "https://gray-server.herokuapp.com/news", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String data=new String(responseBody);
                Log.d("recievedData",data);
                try {
                    JSONObject object = new JSONObject(data);
                    JSONArray jsonArray = object.getJSONArray("articles");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String author = jsonObject.getString("author").toString();
                        String class1 = jsonObject.getString("class1").toString();
                        String class2 = jsonObject.getString("class2").toString();
                        String desc = jsonObject.getString("desc").toString();
                        String image = jsonObject.getString("image").toString();
                        String publishedAt = jsonObject.getString("publishedAt").toString();
                        String title = jsonObject.getString("title").toString();
                        String url = jsonObject.getString("url").toString();
                        HashMap<String,String> map = new HashMap<>();
                        map.put("TITLE",title);
                        map.put("DESCRIPTION",desc);
                        try {
                            map.put("DATE", publishedAt.substring(1));
                        }
                        catch (Exception e)
                        {
                            map.put("DATE", publishedAt);
                        }
                        map.put("URL",image);
                        map.put("LINK",url);
                        newsList.add(map);
                    }
                    adapter = new GoogleAdapter(GoogleNews.this,newsList);
                    listView.setAdapter(adapter);
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getContext(),"Could not connect to server",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
