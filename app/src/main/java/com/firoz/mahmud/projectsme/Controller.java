package com.firoz.mahmud.projectsme;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class Controller{

    Background bg;
    Socket sock;
    InputStream is;
    OutputStream os;
    int cmd=-1;
    boolean dest=true;
    String remoteip="192.168.0.102";
    public Controller(Background bg){
        this.sock=null;
        this.bg=bg;


    }
    public void destroys(){
        dest=false;
        try{
            is.close();
            os.close();
            sock.close();
        }catch (Exception e){}
    }

    private void scanNet() throws Exception {
        try {
            this.sock = new Socket("192.168.0.102", 4000);
//            this.is = this.sock.getInputStream();
            this.os = this.sock.getOutputStream();
        }catch (Exception e ){
            try {
                this.sock.close();
            }catch (Exception ee){}
            this.sock=null;
            this.is=null;
            this.os=null;
        }
    }

    private void sendData(int data) throws Exception {
//        this.sock = new Socket("192.168.0.103", 4000);
//        this.is = this.sock.getInputStream();
//        this.os = this.sock.getOutputStream();
        byte b[]=new byte[1];
        b[0]=(byte)data;
        this.os.write(b,0,b.length);
        this.os.flush();
    }
    boolean isworking=false;
    public void sendcmd(int cmd){
        if(isworking) {
//            if(cmd==17){
////                while(isworking){}
//            }else {
                return;
//            }
        }
//        this.cmd=cmd;
        Thread th=new Thread(){
            @Override
            public void run() {
                isworking=true;
                try {
                    if(Controller.this.sock==null) {
                        while (Controller.this.sock == null) {
                            scanNet();
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                            }
                        }
                    }else {
                        sendData(cmd);
                    }
                }catch (Exception e){
                    try {
                        Controller.this.sock.close();
                    }catch (Exception ee){}
                    Controller.this.sock=null;
                    Controller.this.is=null;
                    Controller.this.os=null;
                }
                isworking=false;
            }
        };
        th.start();

    }


}
