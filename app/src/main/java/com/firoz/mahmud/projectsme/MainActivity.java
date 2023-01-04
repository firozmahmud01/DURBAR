package com.firoz.mahmud.projectsme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    Background bg=null;
    int speed=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        if(bg==null){
            bg=new Background(this);
        }
        setContentView(bg);





    }

    @Override
    protected void onPause() {
        super.onPause();
        bg.destroythis();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Your system is running.Don't press on back", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(bg.con!=null) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                speed--;
                if (speed <= 0) speed = 1;
                bg.con.sendcmd(20 + speed);
                Toast.makeText(this, "Speed is " + speed, Toast.LENGTH_SHORT).show();
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                speed++;
                if (speed >= 11) speed = 10;
                bg.con.sendcmd(20 + speed);
                Toast.makeText(this, "Speed is " + speed, Toast.LENGTH_SHORT).show();
            }

        }
        return true;
    }


}