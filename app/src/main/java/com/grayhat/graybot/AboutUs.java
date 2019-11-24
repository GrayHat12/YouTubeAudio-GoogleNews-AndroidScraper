package com.grayhat.graybot;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class AboutUs extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View fragmentView;
    ImageView website,instagram,facebook,twitter,github,linkedIn;

    public AboutUs() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(fragmentView!=null)
            return fragmentView;
        fragmentView = inflater.inflate(R.layout.fragment_about_us, container, false);
        website = fragmentView.findViewById(R.id.website);
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(getContext(),"https://grayhat12.github.io/old/index.html");
            }
        });
        Picasso.get()
                .load("https://www.pinclipart.com/picdir/middle/211-2116571_website-website-logo-png-transparent-background-clipart.png")
                .fit().into(website, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        instagram = fragmentView.findViewById(R.id.instagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(getContext(),"https://www.instagram.com/gray_._hat/");
            }
        });
        Picasso.get()
                .load("https://imageog.flaticon.com/icons/png/512/174/174855.png")
                .fit().into(instagram, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        facebook = fragmentView.findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(getContext(),"https://www.facebook.com/grayhathacks/");
            }
        });
        Picasso.get()
                .load("https://en.facebookbrand.com/wp-content/uploads/2019/04/f_logo_RGB-Hex-Blue_512.png")
                .fit().into(facebook, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        linkedIn = fragmentView.findViewById(R.id.linkedIn);
        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(getContext(),"https://www.linkedin.com/in/grayhat");
            }
        });
        Picasso.get()
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/LinkedIn_logo_initials.png/600px-LinkedIn_logo_initials.png")
                .fit().into(linkedIn, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        github = fragmentView.findViewById(R.id.github);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(getContext(),"https://github.com/GrayHat12");
            }
        });
        Picasso.get()
                .load("https://mpng.pngfly.com/20180628/wpy/kisspng-github-social-media-computer-icons-logo-android-5b34849064c384.6953108415301684644127.jpg")
                .fit().into(github, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        twitter = fragmentView.findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser(getContext(),"https://twitter.com/gray_rahul");
            }
        });
        Picasso.get()
                .load("https://cdn2.iconfinder.com/data/icons/popular-social-media-flat/48/Popular_Social_Media-11-512.png")
                .fit().into(twitter, new Callback() {
            @Override
            public void onSuccess() {
                System.out.println("Loaded");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        return fragmentView;
    }
}
