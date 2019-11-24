package com.grayhat.graybot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView title,subtitle;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=(TextView)(findViewById(R.id.head));
        subtitle=(TextView)findViewById(R.id.subtitle);
        logo=(ImageView)(findViewById(R.id.logoIm));
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.transition);
        final Animation slide = AnimationUtils.loadAnimation(this,R.anim.movein);
        final Intent home = new Intent(this,HomePage.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                title.startAnimation(slide);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(home,
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logo.startAnimation(myAnim);
    }
}