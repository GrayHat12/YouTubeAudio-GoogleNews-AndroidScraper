package com.grayhat.graybot;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class Youtube extends Fragment {

    SearchView searchView;
    private View fragmentView;
    ListView listView;
    private AsyncHttpClient client;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> videoList ;
    String TITLE,AUTHOR,IMAGE,AUDIO,VIDEO,DURATION,DURATIONSECS;
    ImageView imageThumb,imagePlay;
    TextView textTitle,textArtist;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    HomePage object;

    public void setObject(HomePage object) {
        this.object = object;
    }

    public Youtube(HomePage ob) {
        object = ob;
    }

    public Youtube() { }

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
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        if(fragmentView!=null)
            return fragmentView;
        fragmentView = inflater.inflate(R.layout.fragment_youtube, container, false);
        searchView = fragmentView.findViewById(R.id.searchView);
        listView = fragmentView.findViewById(R.id.listView);
        imageThumb = fragmentView.findViewById(R.id.current_image);
        imagePlay = fragmentView.findViewById(R.id.playPause);
        textTitle = fragmentView.findViewById(R.id.curtitle);
        textArtist = fragmentView.findViewById(R.id.Curartist);
        if(Player.mediaPlayer!=null)
        {
            if(Player.mediaPlayer.isPlaying())
            {
                imagePlay.setImageResource(R.drawable.pause);
            }
        }
        client = new AsyncHttpClient();
        client.setTimeout(60*1000);
        client.setConnectTimeout(60*1000);
        client.setResponseTimeout(60*1000);
        client.setEnableRedirects(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    makeSearch(query,10);
                }
                catch (Exception ex)
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
        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Player.mediaPlayer==null)
                {
                    return;
                }
                if(Player.mediaPlayer.isPlaying())
                {
                    Player.mediaPlayer.pause();
                    imagePlay.setImageResource(R.drawable.play);
                }
                else
                {
                    Player.mediaPlayer.start();
                    imagePlay.setImageResource(R.drawable.pause);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                System.out.println(videoList.get(position).get("TITLE"));
                if(position>-1)
                    play(videoList.get(position));
            }
        });
        try {
            //makeSearch("Ed Sheeran Songs",10);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        imageThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioActivity();
            }
        });
        textTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioActivity();
            }
        });
        textArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioActivity();
            }
        });
        return fragmentView;
    }

    HashMap<String, String> CMapData;

    private void play(HashMap<String, String> mapdata) {
        CMapData = mapdata;
        TITLE = mapdata.get("TITLE");
        AUTHOR = mapdata.get("ARTIST");
        IMAGE = mapdata.get("URL");
        AUDIO = mapdata.get("AUDIOURL");
        VIDEO = mapdata.get("VIDEOURL");
        DURATION = mapdata.get("DURATION");
        DURATIONSECS = mapdata.get("DURATIONSEC");
        imagePlay.setImageResource(R.drawable.play);
        Picasso.get().load(IMAGE).fit().into(imageThumb, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        textTitle.setText(TITLE);
        textArtist.setText(AUTHOR);
        if(Player.mediaPlayer == null)
        { }
        else
        {
            if(Player.mediaPlayer.isPlaying())
                Player.mediaPlayer.stop();
            Player.mediaPlayer.release();
            Player.mediaPlayer=null;
        }
        try {
            Player.mediaPlayer = new MediaPlayer();
            Player.mediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK);
            //Player.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Player.mediaPlayer.setAudioAttributes(new AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            Player.mediaPlayer.setDataSource(AUDIO);
            try{
                Player.mediaPlayer.prepareAsync();
                System.out.println("MEDIA PLAYER PREPARING");
                Player.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Player.mediaPlayer.start();
                        System.out.println("MEDIA PLAYER PREPARED");
                        System.out.println("MEDIA PLAYER STARTED");
                    }
                });
                Player.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Player.mediaPlayer.release();
                        Player.mediaPlayer=null;
                    }
                });
                //Player.mediaPlayer.setLooping(true);
                imagePlay.setImageResource(R.drawable.pause);
            }
            catch (Exception err)
            {
                err.printStackTrace();
                Toast.makeText(getContext(),"Unable to load media.\nTry again Later",Toast.LENGTH_LONG).show();
            }
            Player.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getContext(),"Media Player encountered some error "+what,Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            Player.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    imagePlay.setImageResource(R.drawable.play);
                }
            });
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void makeSearch(String search, int i) throws Exception {
        videoList = new ArrayList<HashMap<String, String>>();
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("search", search);
        jsonParams.put("items", "10");
        StringEntity entity = new StringEntity(jsonParams.toString());
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(getContext(), "https://gray-server.herokuapp.com/youtube", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String data=new String(responseBody);
                Log.d("recievedData",data);
                try {
                    //JSONArray array = new JSONArray(data);
                    //JSONObject dataObject = array.getJSONObject(0).getJSONObject("data");
                    JSONObject dataObject = (new JSONObject(data)).getJSONObject("data");
                    Iterator<String> keys=dataObject.keys();
                    while(keys.hasNext())
                    {
                        String key = keys.next();
                        JSONObject link = dataObject.getJSONObject(key);
                        String author = link.getString("author").toString();
                        String category = link.getString("category").toString();
                        String description = link.getString("description").toString();
                        long dislikes = link.getLong("dislikes");
                        long length = link.getLong("length");
                        long likes = link.getLong("likes");
                        String published = link.getString("published").toString();
                        String rating = link.getString("rating").toString();
                        String title = link.getString("title").toString();
                        String thumb = link.getString("thumb").toString();
                        long viewcount = link.getLong("viewcount");
                        JSONObject audio = link.getJSONObject("best_audio");
                        JSONObject video = link.getJSONObject("best_audio_video");
                        String abitrate = audio.getString("bitrate").toString();
                        String aextnsion = audio.getString("extension").toString();
                        String asize = audio.getString("size").toString();
                        String aurl = audio.getString("url").toString();
                        String vbitrate = video.getString("bitrate").toString();
                        String vextnsion = video.getString("extension").toString();
                        String vsize = video.getString("size").toString();
                        String vurl = video.getString("url").toString();
                        HashMap<String,String> map = new HashMap<>();
                        map.put("TITLE",title);
                        map.put("YOUTUBE",key);
                        map.put("ARTIST",author);
                        map.put("DURATION",secConvert(length));
                        map.put("DURATIONSEC",""+length);
                        map.put("URL",thumb);
                        map.put("CATEGORY",category);
                        map.put("DESCRIPTION",description);
                        map.put("DISLIKES",""+dislikes);
                        map.put("LIKES",""+likes);
                        map.put("PUBLISHED",published);
                        map.put("RATING",""+rating);
                        map.put("VIEWS",""+viewcount);
                        map.put("AUDIOURL",aurl);
                        map.put("VIDEOURL",vurl);
                        videoList.add(map);
                    }
                    adapter = new LazyAdapter(Youtube.this,videoList);
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

    public String secConvert(long seconds)
    {
        long min = seconds/60;
        long sec = seconds%60;
        String minn = min>9l ? ""+min : "0"+min;
        String secc = sec>9l ? ""+sec : "0"+sec;
        return minn+":"+secc;
    }

    private void audioActivity() {
        if(CMapData==null)
            return;
        Intent i = new Intent(getContext(), AudioPlayer.class);
        i.putExtra("TITLE", TITLE);
        i.putExtra("AUTHOR",AUTHOR);
        i.putExtra("THUMB",IMAGE);
        i.putExtra("YOUTUBE",CMapData.get("YOUTUBE"));
        startActivityForResult(i, 12345);
    }

}
