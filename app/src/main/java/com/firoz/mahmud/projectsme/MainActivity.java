package com.firoz.mahmud.projectsme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    Background bg=null;
    int speed=1;
    String link1,link2;
    WebView cam1;
    TextView speedview;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = (int) (displayMetrics.heightPixels/2.2);
        int width = (int) (displayMetrics.widthPixels/2.2);
        if(bg==null){
            bg=new Background();
        }
        cam1=findViewById(R.id.mainview);
        link1="http://192.168.0.102:8000" +"/?size="+width+"X"+height;
        link2="http://192.168.0.102:8001" +"/?size="+width+"X"+height;
        Toast.makeText(this, link1, Toast.LENGTH_SHORT).show();
        cam1.setWebViewClient(new WebViewClient());
        cam1.setWebChromeClient(new WebChromeClient());
        cam1.getSettings().setJavaScriptEnabled(true);
        cam1.loadUrl(link1);
    }
    TextView but;
    Handler hand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
//        if(bg==null){
//            bg=new Background(this);
//        }
//        setContentView(bg);
        setContentView(R.layout.activity_main);
        hand=new Handler();
        speedview=findViewById(R.id.speedofview);
        but=findViewById(R.id.buttonviewer);


//        vv=findViewById(R.id.main_video_view);
//        vv.setVideoURI(Uri.parse("http://192.168.0.100:8000"));
//        vv.start();









    }

    @Override
    protected void onPause() {
        super.onPause();
//        bg.destroythis();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your system is running.Don't press on back", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(bg.con!=null) {
//            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//                speed--;
//                if (speed <= 0) speed = 1;
//                bg.con.sendcmd(20 + speed);
//                Toast.makeText(this, "Speed is " + speed, Toast.LENGTH_SHORT).show();
//            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//                speed++;
//                if (speed >= 11) speed = 10;
//                bg.con.sendcmd(20 + speed);
//                Toast.makeText(this, "Speed is " + speed, Toast.LENGTH_SHORT).show();
//            }
//
//        }
        return true;
    }
//    joystick
//  right up=37
//    left up=19
//  right down=39
//    left down=20
//  right left=38
//    left left=21
//  right right=40
//    left right=22
//  y=11
//    b=10
//    x=8
//    a=9
//    volium low =25
//    volium up=24
//    plus right=32
//    plus left=29
//    plus up=51
//    plus down=47
//    clow open=12
//    clow close=13
    int joint=0;
    Thread working=null;
    boolean activated=true;
    public void checker(){
        if(working==null||!working.isAlive()){
            try{
                working.destroy();
            }catch (Exception e){

            }
            working=new Thread(){
                @Override
                public void run() {
                    while(true) {
                        try {
                            sleep(80);
                        } catch (Exception e) {
                        }
                        if(!activated){
                            bg.joystickstop();
                            hand.post(new Runnable() {
                                @Override
                                public void run() {
                                    but.setText("Deactivated");
                                    but.setTextColor(Color.RED);
                                }
                            });
                        }else{
                            activated=false;
                        }
                    }
                }
            };
            working.start();

        }
        activated=true;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
            checker();
        if (event.getAction()==KeyEvent.ACTION_DOWN){
            but.setTextColor(Color.GREEN);
            but.setText("Activated "+event.getKeyCode());
            switch (event.getKeyCode()){
                case 19:
                    bg.joystickforward();
                    break;
                case 20:
                    bg.joystickbackward();
                    break;
                case 21:
                    bg.joystickleft();
                    break;
                case 22:
                    bg.joystickright();
                    break;
                case 37:
//                    move up
                    switch (joint){
                        case 0:
                            bg.lowerarmright(true);
                            break;
                        case 1:
                            bg.upperarmleft(true);
                            break;
                        case 2:
                            bg.clowup(true);
                            break;
                    }
                    break;
                case 39:
//                    move down
                    switch (joint){
                        case 0:
                            bg.lowerarmleft(true);
                            break;
                        case 1:
                            bg.upperarmright(true);
                            break;
                        case 2:
                            bg.clowdown(true);
                            break;
                    }
                    break;
                case 38:
//                    move left
                    switch (joint){
                        case 0:
                        case 1:
                            bg.baseleft(true);
                            break;
                        case 2:
                            bg.clowleft(true);
                            break;
                    }
                    break;
                case 40:
//                    move right
                    switch (joint){
                        case 0:
                        case 1:
                            bg.baseright(true);
                            break;
                        case 2:
                            bg.clowright(true);
                            break;
                    }
                break;
                case 9:
                    joint=0;
                    break;
                case 8:
                    joint=1;
                    break;
                case 11:
                    joint=2;
                    break;
                case 12:
                    bg.clawopen();
                    break;
                case 13:
                    bg.clawclose();
                    break;
                case 25:
                speed--;
                if (speed <= 0) speed = 1;
                bg.con.sendcmd(20 + speed);
                    speedview.setText("Speed:"+speed);
                    break;
                case 24:
                                    speed++;
                if (speed >= 10) speed = 9;
                bg.con.sendcmd(20 + speed);
                    speedview.setText("Speed:"+speed);
                    break;
                case 14:
                    cam1.loadUrl(link1);
                    break;
                case 15:
                    cam1.loadUrl(link2);
                    break;
                default:
                    Toast.makeText(this, ""+event.getKeyCode(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
//        bg.joystickstop();
        return true;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        Toast.makeText(this, "motion activated", Toast.LENGTH_SHORT).show();
        return true;
    }
}