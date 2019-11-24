package com.grayhat.graybot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class AudioPlayer extends AppCompatActivity {

    ImageView thumb,play;
    TextView title,author;
    String url;
    SeekBar seekBar;

    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        thumb = (ImageView)findViewById(R.id.thumbnaill);
        title = (TextView)findViewById(R.id.detail);
        author = (TextView)findViewById(R.id.authorr);
        play = (ImageView)findViewById(R.id.playButt);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        if(Player.mediaPlayer!=null)
        {
            if(Player.mediaPlayer.isPlaying())
            {
                play.setImageResource(R.drawable.pause);
            }
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title.setText(bundle.getString("TITLE"));
            author.setText(bundle.getString("AUTHOR"));
            url = bundle.getString("YOUTUBE");
            Picasso.get().load(bundle.getString("THUMB")).fit().into(thumb, new Callback() {
                @Override
                public void onSuccess() {
                    System.out.println("Loaded");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
        finalTime=Player.mediaPlayer.getDuration();
        seekBar.setProgress((int)startTime);
        myHandler.postDelayed(UpdateSongTime,100);
    }

    public void repeat(View view) {
        if(Player.mediaPlayer==null)
            return;
        Player.mediaPlayer.setLooping(!Player.mediaPlayer.isLooping());
    }

    public void runb(View view) {
        if(Player.mediaPlayer==null)
            return;
        if(Player.mediaPlayer.isPlaying()&&Player.mediaPlayer.getCurrentPosition()-5000>=0)
        {
            Player.mediaPlayer.seekTo(Player.mediaPlayer.getCurrentPosition()-5000);
        }
    }

    public void play(View view) {
        if(Player.mediaPlayer==null)
            return;
        try {
            if(Player.mediaPlayer.isPlaying())
            {
                Player.mediaPlayer.pause();
                play.setImageResource(R.drawable.play);
            }
            else
            {
                Player.mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Something Wrong",Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    public void runn(View view) {
        if(Player.mediaPlayer==null)
            return;
        if(Player.mediaPlayer.isPlaying()&&Player.mediaPlayer.getCurrentPosition()+5000<Player.mediaPlayer.getDuration())
        {
            Player.mediaPlayer.seekTo(Player.mediaPlayer.getCurrentPosition()+5000);
        }
    }

    public void share(View view) {
        if(Player.mediaPlayer==null)
            return;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "SHARED FROM GrayBot\nLink : "+url;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Song Shared from GrayBot");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivityForResult(Intent.createChooser(sharingIntent, "Share To"),23456);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            if(Player.mediaPlayer==null)
                return;
            if(!Player.mediaPlayer.isPlaying())
                return;
            startTime = Player.mediaPlayer.getCurrentPosition();
            seekBar.setMax((int)finalTime);
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this, 500);
        }
    };

    public void back(View view) {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
