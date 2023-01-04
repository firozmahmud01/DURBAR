package com.firoz.mahmud.projectsme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class CameraThread extends Thread{
    Socket sock;
    InputStream is;
    ImgSetter bg;
    String host;
    byte twofif=(byte)255;
    byte start=(byte)216;
    byte end=(byte)217;
    byte data[],temp[];
    int point=0;
    int port ;
    boolean dest=true;
    public void destroys(){
        dest=false;
        try {
            is.close();
            sock.close();
        }catch (Exception e){}

    }
    public CameraThread(ImgSetter bg,String host,int port){
        is=null;
        this.host=host;
        data=new byte[1000000];
        temp=new byte[1000000];
        this.port =port;
        this.bg=bg;
        sock=null;
    }
    @Override
    public void run() {
        while(dest){

            if(sock==null||!sock.isConnected()){
                try{
                    sock=new Socket(host,port);
                    is=sock.getInputStream();

                }catch (Exception e){
                    sock=null;
                    is=null;
                }
            }


            try{
                getcamera();
            }catch (Exception e){
                e.printStackTrace();
            }


            try {
                sleep(50);
            } catch (InterruptedException e) {

            }
        }
    }

    boolean flag=false;
    private void getcamera() throws Exception {
        while(true){

            if(is.available()>0){
                int size=is.read(temp,0,temp.length);
                for(int i=0;i<size;i++){
                    if(flag){
                        flag=false;
                        if(temp[i]==start){
                            point=0;
                            data[point++]=(byte)255;

                        }else if(temp[i]==end){
                            data[point++]=temp[i];
                            finaligeimage();
                            continue;
                        }
                    }
                    if(temp[i]==twofif){
                        flag=true;
                    }
                        data[point++]=temp[i];
                }
            }else{
                Thread.sleep(50);
            }
        }
    }
    private void finaligeimage(){
        Bitmap bit = BitmapFactory.decodeByteArray(data,0,point);
        bg.setBitmap(bit);
    }
}
