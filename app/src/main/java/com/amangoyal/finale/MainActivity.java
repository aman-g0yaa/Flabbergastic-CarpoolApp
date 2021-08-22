package com.amangoyal.finale;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    //delay time of 5s stored in a variable

    private static int SPLASH_SCREEN = 3000 ; //1000 = 1s --  5000 = 5s , static so splash screen in capitals


    // Splash screen variables

    Animation top,bottom;
    ImageView logo;
    TextView logoname,tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //hiding status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //splash screen
        //hooks
        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo = findViewById(R.id.logo_id1);
        logoname = findViewById(R.id.logoname_id1);
        tag = findViewById(R.id.tag_1);

        logo.setAnimation(top);
        logoname.setAnimation(bottom);
        tag.setAnimation(bottom);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,login.class);

                //pair created for store a bundle which has the animation -  view , its transition name as elements of pair

                Pair[] pairs = new Pair[2];

                pairs[0] = new Pair<View,String>(logo,"logo_trans");
                pairs[1] = new Pair<View,String>(logoname,"logoname_trans");
S

                //"if" statement becoz this animation wont coz crash for devices below lollipop

//                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP) {
//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
//                    startActivity(intent, options.toBundle());  //intent is started with the animation bundle
//                    finish();
//                }
//                else {
                    startActivity(intent);
                    finish();
//                }



            }
        },SPLASH_SCREEN);

    }
}