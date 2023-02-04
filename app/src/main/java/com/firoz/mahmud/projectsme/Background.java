package com.firoz.mahmud.projectsme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Background extends View {
    Thread th;
    int speed;
    Controller con;
    Point cam1s=new Point(),cam2s=new Point();

    boolean canjoyrun=false;
    boolean isjoystick=false;
    private String bigmsg=null;

    float joystickx=-1,joysticky=-1;
    ArrayList<Double> metal,lngd;
    Handler hand;
    WebView cam1,cam2;
    public Background(Context context){
        super(context);
        this.con=null;
        this.cam1s.x=getWidth();
        this.cam1s.y=getHeight();
        hand=new Handler();
        this.speed=1;
        this.metal=new ArrayList<>();
        this.lngd=new ArrayList<>();
        this.cam1=new WebView(context);
        this.cam2=new WebView(context);
        this.cam1.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(context, "Failed to connect with camera 1", Toast.LENGTH_SHORT).show();
                Thread th=new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                        }catch (Exception e){}
                        cam1.loadUrl("http://192.168.0.102:8000/?size="+cam1s.x+"X"+cam1s.y);
                    }
                };
                th.start();
            }
        });
        this.cam2.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(context, "Failed to connect with camera 2", Toast.LENGTH_SHORT).show();
                Thread th=new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                        }catch (Exception e){}
                        cam2.loadUrl("http://192.168.0.102:8001/?size="+cam2s.x+"X"+cam2s.y);
                    }
                };
                th.start();
            }
        });
        this.cam2.loadUrl("http://192.168.0.102:8001/?size="+cam2s.x+"X"+cam2s.y);
        this.cam1.loadUrl("http://192.168.0.102:8000/?size="+cam1s.x+"X"+cam1s.y);
        th=null;
    }





    public void showBigMsg(String msg){
        this.bigmsg=msg;
        try {
            invalidate();
        }catch (Exception e){

        }
    }
    public void clearScreen(){
        bigmsg=null;
        invalidate();
    }


    public boolean isGamepad() {

        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {

                return true;

            }
        }
        return false;
    }


    @Override
    public void onDraw(Canvas c){

        c.drawRGB(0,0,0);
        Cam1(0,0,getHeight(),getWidth(),c);

        Paint p=new Paint();
        p.setColor(Color.WHITE);

        p.setTextSize(20);

        c.drawText("Bettary:",getWidth()-200,30,p);
        p.setColor(Color.GREEN);
        c.drawText("100%",getWidth()-100,30,p);


        c.drawText("No metal detected",getWidth()-200,70,p);

        c.drawText("No cuircit detected",getWidth()-200,110,p);
        p.setColor(Color.WHITE);
        c.drawText("Main Camera",(getWidth()/2)-55,getHeight()-40,p);


        if(!isGamepad()) {
            canjoyrun=false;
            c.drawCircle(230, 230, 30, p);
            Bitmap b = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.clawopen), 50, 50, false);
            c.drawBitmap(b, 205, 200, new Paint());
            c.drawCircle(290, 340, 30, p);
            b = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.clawclose), 50, 50, false);
            c.drawBitmap(b, 265, 315, new Paint());
            b = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.clawrotate), 100, 170, false);
            c.drawBitmap(b, 280, 200, new Paint());

            int circlex = getWidth() - 140;
            int circley = getHeight() - 140;
            c.drawCircle(circlex, circley, 100, p);
            c.drawCircle(circlex, circley, 97, new Paint());


//        touch joystick
            Paint joystick = new Paint();
            joystick.setColor(Color.GRAY);
            if (joystickx != -1) {
                c.drawCircle(this.joystickx, this.joysticky, 40, joystick);
            } else {
                this.joystickx = getWidth() - 140;
                this.joysticky = getHeight() - 140;
                c.drawCircle(this.joystickx, this.joysticky, 40, joystick);
            }

            Bitmap hand = BitmapFactory.decodeResource(getResources(), R.drawable.controlpic);
            hand = Bitmap.createScaledBitmap(hand, (int) (hand.getWidth() * (((getHeight() / 3.0) * 2) / hand.getHeight())), (int) ((getHeight() / 3.0) * 2), false);
            c.drawBitmap(hand, 30, (float) ((Double.valueOf(getHeight()) / 3.0) - 10.0), new Paint());

            Paint disruptur = new Paint();
            disruptur.setColor(Color.RED);
            c.drawCircle(getWidth() - 350, getHeight() - 70, 40, disruptur);


        }else{
            canjoyrun=true;
        }


            Cam2(20,20,150,200,c);
            c.drawText("Camera 1",30,200,p);
            Cam3(250,20,150,200,c);
            c.drawText("Camera 2",250,200,p);

            MetalDetector(500,20,150,200,c);
            c.drawText("Metal Detector",500,200,p);








//        message section
//        if(bigmsg!=null) {
//            Paint paint=new Paint();
//            paint.setTextSize(50);
//            paint.setColor(Color.WHITE);
//            c.drawText(bigmsg, (getWidth() / 2)-((25*bigmsg.length())/2),getHeight()/2,paint );
//        }
    }




    private void clowleft(boolean pressed){
        if(pressed) {
            con.sendcmd(5);
        }else{
            con.sendcmd(17);
        }
    }
    private void clowright(boolean pressed){
        if(pressed) {
            con.sendcmd(6);
        }else{
            con.sendcmd(17);
        }
    }
    private void upperarmright(boolean pressed){
        if(pressed) {
            con.sendcmd(8);
        }else{
            con.sendcmd(17);
        }
    }
    private void upperarmleft(boolean pressed){
        if(pressed) {
            con.sendcmd(7);
        }else{
            con.sendcmd(17);
        }
    }
    private void lowerarmright(boolean pressed){

        if(pressed) {
            con.sendcmd(10);
        }else{
            con.sendcmd(17);
        }
    }
    private void lowerarmleft(boolean pressed){
        if(pressed) {
            con.sendcmd(9);
        }else{
            con.sendcmd(17);
        }
    }
    private void baseright(boolean pressed){

        if(pressed) {
            con.sendcmd(12);
        }else{
            con.sendcmd(17);
        }
    }
    private void baseleft(boolean pressed){
        if(pressed) {
            con.sendcmd(11);
        }else{
            con.sendcmd(17);
        }
    }



    public void pushLNGD(int value,int max){
        double ratio=(double)(value)/(double)(max);
        lngd.add(0, ratio);
        invalidate();
    }
    public void pushMetal(int value,int max){
        double ratio=(double)(value)/(double)(max);
        metal.add(0, ratio);

        invalidate();
    }
    public void LNGD(int x,int y,int hight,int weight,Canvas c){
        Paint p=new Paint();
        p.setStrokeWidth(3);
        p.setColor(Color.WHITE);
        c.drawLine(x,y,x+weight,y,p);
        c.drawLine(x,y,x,y+hight,p);
        c.drawLine(x+weight,y,x+weight,y+hight,p);
        c.drawLine(x,y+hight,x+weight,y+hight,p);


    }

    public void MetalDetector(int x,int y,int hight,int weight,Canvas c){
        Paint p=new Paint();
        p.setStrokeWidth(3);
        p.setColor(Color.WHITE);
        c.drawLine(x,y,x+weight,y,p);
        c.drawLine(x,y,x,y+hight,p);
        c.drawLine(x+weight,y,x+weight,y+hight,p);
        c.drawLine(x,y+hight,x+weight,y+hight,p);


    }
    public void Cam1(int x,int y,int hight,int weight,Canvas c){
        Paint p=new Paint();
        p.setStrokeWidth(3);
        p.setColor(Color.WHITE);
        c.drawLine(x,y,x+weight,y,p);
        c.drawLine(x,y,x,y+hight,p);
        c.drawLine(x+weight,y,x+weight,y+hight,p);
        c.drawLine(x,y+hight,x+weight,y+hight,p);
        if(cam1!=null) {
//            Bitmap bit=Bitmap.createScaledBitmap(cam1,weight,hight,false);
//            c.drawBitmap(bit,x,y,new Paint());
        }

    }
    public void Cam2(int x,int y,int hight,int weight,Canvas c){
        Paint p=new Paint();
        p.setStrokeWidth(3);
        p.setColor(Color.WHITE);
        c.drawLine(x,y,x+weight,y,p);
        c.drawLine(x,y,x,y+hight,p);
        c.drawLine(x+weight,y,x+weight,y+hight,p);
        c.drawLine(x,y+hight,x+weight,y+hight,p);
        if(cam2!=null) {
//            Bitmap bit=Bitmap.createScaledBitmap(cam2,weight,hight,false);
//            c.drawBitmap(bit,x,y,new Paint());
        }

    }
    public void Cam3(int x,int y,int hight,int weight,Canvas c){
        Paint p=new Paint();
        p.setStrokeWidth(3);
        p.setColor(Color.WHITE);
        c.drawLine(x,y,x+weight,y,p);
        c.drawLine(x,y,x,y+hight,p);
        c.drawLine(x+weight,y,x+weight,y+hight,p);
        c.drawLine(x,y+hight,x+weight,y+hight,p);


    }

    private boolean clickcheck(int x,int y,int weight,int hight,float clickx,float clicky){
        if(clickx>=x&&clickx<=(x+weight)&&clicky>=y&&clicky<=(y+hight)){
            return true;
        }
        return false;
    }
    private void touchup(float clickx,float clicky){
        joystickup(clickx,clicky);
        //        clowleft x=100 y=265 and weight 60,height 60
//        clowright x=190 y=350 weight 60 and hight 60
//        upperarmleft x=30 y=410 weight 60 and hight 60
//        upperarmright x=170 y=480 weight 60 and hight 60
//        lowerarmleft x=50 y=585 weight 60 and hight 60
//        lowerarmright x=190 y=560 weight 60 and hight 60
//        baseleft x=245 y=655 weight 60 and hight 60
//        baseright x=170 y=730 weight 60 and hight 60
        con.sendcmd(17);


        if(clickcheck((getWidth()/2)-300,(getHeight()/2)-180,600,480,clickx,clicky)){
            cameracheck(clickx,clicky,false,false);
        }

    }
    public void clawopen(){
        con.sendcmd(31);

    }
    public void clawclose(){
        con.sendcmd(32);
    }

    public void clawleft(){
        con.sendcmd(33);
    }
    public void clawright(){
        con.sendcmd(34);
    }
    public void firedis(){
        con.sendcmd(35);
    }
    private void touchdown(float clickx,float clicky){
        if(clickcheck((getWidth()/2)-300,(getHeight()/2)-180,600,480,clickx,clicky)){
            cameracheck(clickx,clicky,false,true);
        }
        if(canjoyrun)return;
        if(clickcheck(getWidth()-250,getHeight()-250,200,200,clickx,clicky)){
            joystickdown(clickx, clicky);
        }

//        clowleft x=100 y=265 and weight 60,height 60
//        clowright x=190 y=350 weight 60 and hight 60
//        upperarmleft x=30 y=410 weight 60 and hight 60
//        upperarmright x=170 y=480 weight 60 and hight 60
//        lowerarmleft x=50 y=585 weight 60 and hight 60
//        lowerarmright x=190 y=560 weight 60 and hight 60
//        baseleft x=245 y=655 weight 60 and hight 60
//        baseright x=170 y=730 weight 60 and hight 60
        else if (clickcheck(190,350,60,60,clickx,clicky)){
            clowright(true);
        }else if (clickcheck(100,265,60,60,clickx,clicky)){
            clowleft(true);
        }
        else if (clickcheck(200,200,60,60,clickx,clicky)){
            clawopen();
        }else if (clickcheck(260,310,60,60,clickx,clicky)){
            clawclose();
        }
        else if (clickcheck(285,200,60,60,clickx,clicky)){
            clawleft();
        }else if (clickcheck(335,325,60,60,clickx,clicky)){
            clawright();
        }
        else if (clickcheck(30,410,60,60,clickx,clicky)){
            upperarmleft(true);
        }else if (clickcheck(getWidth() - 370,getHeight()-110,80,80,clickx,clicky)){
            firedis();
        }
        else if (clickcheck(170,480,60,60,clickx,clicky)){
            upperarmright(true);
        }
        else if (clickcheck(50,585,60,60,clickx,clicky)){
            lowerarmleft(true);
        }
        else if (clickcheck(190,560,60,60,clickx,clicky)){
            lowerarmright(true);
        }
        else if (clickcheck(245,655,60,60,clickx,clicky)){
            baseleft(true);
        }
        else if (clickcheck(170,730,60,60,clickx,clicky)){
            baseright(true);
        }
        //      x=(getWidth()/2)-300
//      y=(getHeight()/2)-180
//      width 600
//      height 480


    }
    private void touchmove(float clickx,float clicky){
        if(clickcheck(getWidth()-250,getHeight()-250,200,200,clickx,clicky)){
            joystickmove(clickx,clicky);
        }

        else if(clickcheck((getWidth()/2)-300,(getHeight()/2)-180,600,480,clickx,clicky)){
            cameracheck(clickx,clicky,true,false);
        }



    }

    private void joystickmovecheck(float x,float y){
        if(canjoyrun)return;
        int centerx=getWidth() - 140;
        int centery=getHeight()-140;
        if(y<(centery-40)&&Math.abs(centerx-x)<41){
            joystickforward();
        }else if(x<(centerx-40)&&Math.abs(centery-y)<40){
            joystickleft();
        }else if(y>(centery+40)&&Math.abs(centerx-x)<41){
            joystickbackward();
        }else if(x>(centerx+40)&&Math.abs(centery-y)<40){
            joystickright();
        }
    }

    private void joystickup(float x,float y){
        if(!isjoystick)return;
        isjoystick=false;
        this.joystickx=getWidth() - 140;
        this.joysticky=getHeight() - 140;
        joystickstop();
        invalidate();
    }
    private void joystickdown(float x,float y){
        isjoystick=true;
        joystickx=x;
        joysticky=y;
        joystickmovecheck(x,y);
        invalidate();
    }

    private void joystickmove(float x,float y){
        joystickx=x;
        joysticky=y;
        joystickmovecheck(x,y);
        invalidate();
    }
    boolean iscamera=false;
    float pointx,pointy;
    private void cameracheck(float x,float y,boolean move,boolean start){
        if(start){
            iscamera=true;
            pointx=x;
            pointy=y;
        }else if (move){

        }else{
            iscamera=false;
            if(Math.abs(pointx-x)<20){

                if(Math.abs(pointy-y)>30){
                    if(pointy-y>0){
//                        up
                        cameraup();
                    }else if(pointy-y<0){
//                        down
                        cameradown();
                    }
                }
            }else if (Math.abs(pointy-y)<20){
//                left or right
                if(Math.abs(pointx-x)>30){
                    if(pointx-x>0){
//                        left
                        cameraleft();

                    }else if(pointx-x<0){
//                        right
                        cameraright();
                    }
                }
            }
        }
    }
    private void cameraup(){
        con.sendcmd(13);
    }
    private void cameradown(){
        con.sendcmd(14);
    }
    private void cameraleft(){
        con.sendcmd(15);
    }
    private void cameraright(){
        con.sendcmd(16);
    }


    private void joystickforward(){
        con.sendcmd(1);
    }
    private void joystickbackward(){
        con.sendcmd(2);
    }
    private void joystickleft(){
        con.sendcmd(4);
    }
    private void joystickright(){
        con.sendcmd(3);
    }

    private void joystickstop(){
        con.sendcmd(17);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(this.con==null){
            this.con=new Controller(this);

        }
        float x=event.getX();
        float y=event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            touchdown(x,y);
        }else if(event.getAction()==MotionEvent.ACTION_UP){
            touchup(x,y);

        }else if(event.getAction()==MotionEvent.ACTION_MOVE){
            touchmove(x,y);
        }
        return true;
    }

    public void sendsig(int button)  {
        this.con.sendcmd(button);

    }


    public Bitmap bytetobit(byte[] b){


        int intByteCount = b.length;
        int[] intColors = new int[intByteCount / 3];
        int intWidth = 320;
        int intHeight = 240;
        final int intAlpha = 255;
        if ((intByteCount / 3) != (intWidth * intHeight)) {
            throw new ArrayStoreException();
        }

        for (int intIndex = 0; intIndex < intByteCount - 2; intIndex = intIndex + 3) {
            int ba =(int)(0xFF &b[intIndex]);
            int g =(int)(0xFF &b[intIndex+1]);
            int r =(int)(0xFF & b[intIndex+2]);
            intColors[intIndex / 3] = Color.rgb(r,g,ba);
        }
        Bitmap bmpImage = Bitmap.createBitmap(intColors, intWidth, intHeight, Bitmap.Config.ARGB_8888);
        return bmpImage;
    }


}
