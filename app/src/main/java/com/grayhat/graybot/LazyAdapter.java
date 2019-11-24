package com.grayhat.graybot;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class LazyAdapter extends BaseAdapter {

    private Fragment activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;

    public LazyAdapter(Fragment a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        HashMap<String, String> song = data.get(position);

        // Setting all values in listview
        title.setText(song.get("TITLE"));
        artist.setText(song.get("ARTIST"));
        duration.setText(song.get("DURATION"));
        Picasso.get().load(song.get("URL")).fit().into(thumb_image, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}