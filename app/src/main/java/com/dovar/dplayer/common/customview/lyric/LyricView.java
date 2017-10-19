package com.dovar.dplayer.common.customview.lyric;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricView extends View{

  /*  private static TreeMap<Integer, LyricObject> lrc_map;
    private float mX; // 屏幕X轴的中点，此值固定，保持歌词在X中间显示
    private float offsetY; // 歌词在Y轴上的偏移量，此值会根据歌词的滚动变小
    private static boolean blLrc = false;
    private float touchY; // 当触摸歌词View时，保存为当前触点的Y轴坐标
    private float touchX;
    private boolean blScrollView = false;
    private int lrcIndex = 0; // 保存歌词TreeMap的下标
    private int SIZEWORD = 0;// 显示歌词文字的大小值
    private int INTERVAL = 45;// 歌词每行的间隔
    Paint paint = new Paint();// 画笔，用于画不是高亮的歌词
    Paint paintHL = new Paint(); // 画笔，用于画高亮的歌词，即当前唱到这句歌词

    public LyricView(Context context){
        super(context);
        init();
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(blLrc){
            paintHL.setTextSize(SIZEWORD);
            paint.setTextSize(SIZEWORD);
            if(lrc_map.size()==0){
                return;
            }
            LyricObject temp=lrc_map.get(lrcIndex);
            canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*lrcIndex, paintHL);
            // 画当前歌词之前的歌词
            for(int i=lrcIndex-1;i>=0;i--){
                temp=lrc_map.get(i);
                if(offsetY+(SIZEWORD+INTERVAL)*i<0){
                    break;
                }
                canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*i, paint);
            }
            // 画当前歌词之后的歌词
            for(int i=lrcIndex+1;i<lrc_map.size();i++){
                temp=lrc_map.get(i);
                if(offsetY+(SIZEWORD+INTERVAL)*i>600){
                    break;
                }
                canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*i, paint);
            }
        }
        else{
            paint.setTextSize(50);
            canvas.drawText("找不到歌词", mX, 310, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("bllll==="+blScrollView);
        float tt=event.getY();
        if(!blLrc){
            return super.onTouchEvent(event);
        }
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                touchY=tt-touchY;
                offsetY=offsetY+touchY;
                break;
            case MotionEvent.ACTION_UP:
                blScrollView=false;
                break;
        }
        touchY=tt;
        return true;
    }

    public void init(){
        lrc_map = new TreeMap<Integer, LyricObject>();
        offsetY=320;

        paint=new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(180);


        paintHL=new Paint();
        paintHL.setTextAlign(Paint.Align.CENTER);

        paintHL.setColor(Color.RED);
        paintHL.setAntiAlias(true);
        paintHL.setAlpha(255);
    }

    *//**
     * 根据歌词里面最长的那句来确定歌词字体的大小
     *//*

    public void SetTextSize(){
        if(!blLrc){
            return;
        }
        if(lrc_map.size()==0){
            return;
        } else{
            int max=lrc_map.get(0).lrc.length();
            for(int i=1;i<lrc_map.size();i++){
                LyricObject lrcStrLength=lrc_map.get(i);
                if(max<lrcStrLength.lrc.length()){
                    max=lrcStrLength.lrc.length();
                }
            }
            SIZEWORD=1280/max;
        }



    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mX = w * 0.5f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    *//**
     *  歌词滚动的速度
     *
     * @return 返回歌词滚动的速度
     *//*
    public Float SpeedLrc(){
        float speed=0;
        if(offsetY+(SIZEWORD+INTERVAL)*lrcIndex>220){
            speed=((offsetY+(SIZEWORD+INTERVAL)*lrcIndex-220)/20);

        } else if(offsetY+(SIZEWORD+INTERVAL)*lrcIndex < 120){
            Log.i("speed", "speed is too fast!!!");
            speed = 0;
        }
//      if(speed<0.2){
//          speed=0.2f;
//      }
        return speed;
    }

    *//**
     * 按当前的歌曲的播放时间，从歌词里面获得那一句
     * @param time 当前歌曲的播放时间
     * @return 返回当前歌词的索引值
     *//*
    public int SelectIndex(int time){
        if(!blLrc){
            return 0;
        }
        if(lrc_map.size()==0){
            return 0;
        }
        else{
            int index=0;
            for(int i=0;i<lrc_map.size();i++){
                LyricObject temp=lrc_map.get(i);
                if(temp.begintime<time){
                    ++index;
                }
            }
            lrcIndex=index-1;
            if(lrcIndex<0){
                lrcIndex=0;
            }

            return lrcIndex;
        }
    }

    *//**
     * 读取歌词文件
     * @param file 歌词的路径
     *
     *//*
    public static void read(String file) {
        TreeMap<Integer, LyricObject> lrc_read =new TreeMap<Integer, LyricObject>();
        String data = "";
        try {
            File saveFile=new File(file);
            // System.out.println("是否有歌词文件"+saveFile.isFile());
            if(!saveFile.isFile()){
                blLrc=false;
                return;
            }
            blLrc=true;

            FileInputStream stream = new FileInputStream(saveFile);//  context.openFileInput(file);


            BufferedReader br = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
            int i = 0;
            Pattern pattern = Pattern.compile("\\d{2}");
            while ((data = br.readLine()) != null) {
                // System.out.println("++++++++++++>>"+data);
                data = data.replace("[","");//将前面的替换成后面的
                data = data.replace("]","@");
                String splitdata[] =data.split("@");//分隔
                if(data.endsWith("@")){
                    for(int k=0;k<splitdata.length;k++){
                        String str=splitdata[k];

                        str = str.replace(":",".");
                        str = str.replace(".","@");
                        String timedata[] =str.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if(timedata.length==3 && matcher.matches()){
                            int m = Integer.parseInt(timedata[0]);  //分
                            int s = Integer.parseInt(timedata[1]);  //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m*60+s)*1000+ms*10;
                            LyricObject item1= new LyricObject();
                            item1.begintime = currTime;
                            item1.lrc       = "";
                            lrc_read.put(currTime,item1);
                        }
                    }

                }
                else{
                    String lrcContenet = splitdata[splitdata.length-1];

                    for (int j=0;j<splitdata.length-1;j++)
                    {
                        String tmpstr = splitdata[j];

                        tmpstr = tmpstr.replace(":",".");
                        tmpstr = tmpstr.replace(".","@");
                        String timedata[] =tmpstr.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if(timedata.length==3 && matcher.matches()){
                            int m = Integer.parseInt(timedata[0]);  //分
                            int s = Integer.parseInt(timedata[1]);  //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m*60+s)*1000+ms*10;
                            LyricObject item1= new LyricObject();
                            item1.begintime = currTime;
                            item1.lrc       = lrcContenet;
                            lrc_read.put(currTime,item1);// 将currTime当标签  item1当数据 插入TreeMap里
                            i++;
                        }
                    }
                }

            }
            stream.close();
        }
        catch (FileNotFoundException e) {
        }
        catch (IOException e) {
        }

        *//*
         * 遍历hashmap 计算每句歌词所需要的时间
        *//*
        lrc_map.clear();
        data ="";
        Iterator<Integer> iterator = lrc_read.keySet().iterator();
        LyricObject oldval  = null;
        int i =0;
        while(iterator.hasNext()) {
            Object ob =iterator.next();

            LyricObject val = (LyricObject)lrc_read.get(ob);

            if (oldval==null)
                oldval = val;
            else
            {
                LyricObject item1= new LyricObject();
                item1  = oldval;
                item1.timeline = val.begintime-oldval.begintime;
                lrc_map.put(new Integer(i), item1);
                i++;
                oldval = val;
            }
            if (!iterator.hasNext()) {
                lrc_map.put(new Integer(i), val);
            }

        }

    }

    *//**
     * @return the blLrc
     *//*
    public static boolean isBlLrc() {
        return blLrc;
    }

    *//**
     * @return the offsetY
     *//*
    public float getOffsetY() {
        return offsetY;
    }

    *//**
     * @param offsetY the offsetY to set
     *//*
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    *//**
     * @return 返回歌词文字的大小
     *//*
    public int getSIZEWORD() {
        return SIZEWORD;
    }

    *//**
     * 设置歌词文字的大小
     * @param sIZEWORD the sIZEWORD to set 
     *//*
    public void setSIZEWORD(int sIZEWORD) {
        SIZEWORD = sIZEWORD;
    }


//    private static boolean blLrc = false;
//    private static TreeMap<Integer, LyricObject> lrc_map;
//    private static int lrcIndex = 0; //保存歌词TreeMap的下标
//    private float mX;       //屏幕X轴的中点，此值固定，保持歌词在X中间显示
//    private float offsetY;      //歌词在Y轴上的偏移量，此值会根据歌词的滚动变小
//    private float touchY;   //当触摸歌词View时，保存为当前触点的Y轴坐标
//    private float touchX;
//    private boolean blScrollView = false;
//    private int SIZEWORD = 0;//显示歌词文字的大小值
//    private int INTERVAL = 45;//歌词每行的间隔
//    Paint paint = new Paint();//画笔，用于画不是高亮的歌词
//    Paint paintHL = new Paint();  //画笔，用于画高亮的歌词，即当前唱到这句歌词
//
//    private int WIDTH;//控件宽度
//    private int HEIGHT;//控件高度
//    private int lrcHeight;//高亮歌词的Y轴偏移量
//
//    public LyricView(Context context) {
//        super(context);
//        init();
//    }
//
//    public LyricView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        Log.d("dov", "onDraw: ");
//        if (blLrc) {
//            paintHL.setTextSize(SIZEWORD);
//            paint.setTextSize(SIZEWORD);
//            Log.d("dov", "onDraw: "+lrc_map+"lrcindex:"+lrcIndex);
//            LyricObject temp = lrc_map.get(lrcIndex);
//            canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * lrcIndex, paintHL);
//            if (canvas.getHeight() > 900) {
//                // 画当前歌词之前的歌词
//                for (int i = lrcIndex - 1; i >= 0; i--) {
//                    temp = lrc_map.get(i);
//                    if (offsetY + (SIZEWORD + INTERVAL) * i < 0) {
//                        break;
//                    }
//                    canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * i, paint);
//                }
//                // 画当前歌词之后的歌词
//                for (int i = lrcIndex + 1; i < lrc_map.size(); i++) {
//                    temp = lrc_map.get(i);
//                    if (offsetY + (SIZEWORD + INTERVAL) * i > HEIGHT) {
//                        break;
//                    }
//                    canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * i, paint);
//                }
//            }
//        }
//        super.onDraw(canvas);
//    }
//
//
////    @Override
////    public boolean onTouchEvent(MotionEvent event) {
////        System.out.println("bllll===" + blScrollView);
////        float tt = event.getY();
////        if (!blLrc) {
////            return super.onTouchEvent(event);
////        }
////        switch (event.getAction()) {
////            case MotionEvent.ACTION_DOWN:
////                touchX = event.getX();
////                break;
////            case MotionEvent.ACTION_MOVE:
////                touchY = tt - touchY;
////                offsetY = offsetY + touchY;
////                break;
////            case MotionEvent.ACTION_UP:
////                blScrollView = false;
////                break;
////        }
////        touchY = tt;
////        return true;
////    }
//
//    public void init() {
//        Log.d("dov", "init: ");
//        paint = new Paint();
//        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setColor(Color.GREEN);
//        paint.setAntiAlias(true);
//        paint.setDither(true);
//        paint.setAlpha(180);
//
//        paintHL = new Paint();
//        paintHL.setTextAlign(Paint.Align.CENTER);
//        paintHL.setColor(Color.RED);
//        paintHL.setAntiAlias(true);
//        paintHL.setAlpha(255);
//    }
//
//
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        Log.d("dov", "onSizeChanged: width:" + w + "height:" + h);
//        mX = w * 0.5f;
//        HEIGHT = h;
//        WIDTH = w;
//        lrcHeight =HEIGHT/2;
//        offsetY = lrcHeight;
//        super.onSizeChanged(w, h, oldw, oldh);
//    }
//
//    *//**
//     * 歌词滚动的速度
//     *
//     * @return 返回歌词滚动的速度
//     *//*
//    public Float SpeedLrc() {
//        float speed = 0;
//        if (offsetY + (SIZEWORD + INTERVAL) * lrcIndex > lrcHeight) {
//            speed = ((offsetY + (SIZEWORD + INTERVAL) * lrcIndex - lrcHeight) / 20);
//
//        } else if (offsetY + (SIZEWORD + INTERVAL) * lrcIndex < lrcHeight) {
//            Log.i("speed", "speed is too fast!!!");
//            speed = 0;
//        }
//        return speed;
//    }
//
//    *//**
//     * 按当前的歌曲的播放时间，从歌词里面获得那一句
//     *
//     * @param time 当前歌曲的播放时间
//     * @return 返回当前歌词的索引值
//     *//*
//    public int SelectIndex(int time) {
//        if (!blLrc) {
//            return 0;
//        }
//        int index = 0;
//        for (int i = 0; i < lrc_map.size(); i++) {
//            LyricObject temp = lrc_map.get(i);
//            if (temp.begintime < time) {
//                ++index;
//            }
//        }
//        lrcIndex = index - 1;
//        if (lrcIndex < 0) {
//            lrcIndex = 0;
//        }
//        return lrcIndex;
//
////        int index = 0;
////        for (int i = 0; i < lrc_map.size(); i++) {
////            LyricObject temp = lrc_map.get(i);
////            if (temp.begintime < time) {
////                index=i;
////            }else {
////                break;
////            }
////        }
////        return index;
//    }
//
//    *//**
//     * 读取歌词文件
//     *
//     * @param file 歌词的路径
//     *//*
//    public void read(File file) {
//        lrc_map = new TreeMap<Integer, LyricObject>();
//        TreeMap<Integer, LyricObject> lrc_read = new TreeMap<Integer, LyricObject>();
//        String data = "";
//        try {
//            blLrc = true;
//            FileInputStream stream = new FileInputStream(file);
//            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
//            int i = 0;
//            Pattern pattern = Pattern.compile("\\d{2}");
//            while ((data = br.readLine()) != null) {
//                data = data.replace("[", "");//将前面的替换成后面的
//                data = data.replace("]", "@");
//                String splitdata[] = data.split("@");//分隔
//                String lrcContenet = splitdata[splitdata.length - 1];
//
//                for (int j = 0; j < splitdata.length - 1; j++) {
//                    String tmpstr = splitdata[j];
//
//                    tmpstr = tmpstr.replace(":", ".");
//                    tmpstr = tmpstr.replace(".", "@");
//                    String timedata[] = tmpstr.split("@");
//                    Matcher matcher = pattern.matcher(timedata[0]);
//                    if (timedata.length == 3 && matcher.matches()) {
//                        int m = Integer.parseInt(timedata[0]);  //分
//                        int s = Integer.parseInt(timedata[1]);  //秒
//                        int ms = Integer.parseInt(timedata[2]); //毫秒
//                        int currTime = (m * 60 + s) * 1000 + ms * 10;
//                        LyricObject item1 = new LyricObject();
//                        item1.begintime = currTime;
//                        item1.lrc = lrcContenet;
//                        lrc_read.put(currTime, item1);// 将currTime当标签  item1当数据 插入TreeMap里
//                        i++;
//                    }
//                }
//            }
//            stream.close();
//        } catch (FileNotFoundException e) {
//        } catch (IOException e) {
//        }
//        *//*
//         * 遍历hashmap 计算每句歌词所需要的时间
//        *//*
////        lrc_map.clear();
//        Iterator<Integer> iterator = lrc_read.keySet().iterator();
//        int i = 0;
//        while (iterator.hasNext()) {
//            Object ob = iterator.next();
//            LyricObject val = lrc_read.get(ob);
//            lrc_map.put(new Integer(i), val);
//            i++;
//        }
//    }
//
//    *//**
//     * @return the offsetY
//     *//*
//    public float getOffsetY() {
//        return offsetY;
//    }
//
//    *//**
//     * @param offsetY the offsetY to set
//     *//*
//    public void setOffsetY(float offsetY) {
//        this.offsetY = offsetY;
//    }
//
//    *//**
//     * @return 返回歌词文字的大小
//     *//*
//    public int getSIZEWORD() {
//        return SIZEWORD;
//    }
//
//    *//**
//     * 设置歌词文字的大小
//     *
//     * @param sIZEWORD the sIZEWORD to set
//     *//*
//    public void setSIZEWORD(int sIZEWORD) {
//        SIZEWORD = sIZEWORD;
//    }
//
//    *//**
//     * 根据歌词里面最长的那句来确定歌词字体的大小
//     *//*
//    public void SetTextSize() {
//        if (!blLrc) {
//            return;
//        }
//        int max = lrc_map.get(0).lrc.length();
//        for (int i = 1; i < lrc_map.size(); i++) {
//            LyricObject lrcStrLength = lrc_map.get(i);
//            if (max < lrcStrLength.lrc.length()) {
//                max = lrcStrLength.lrc.length();
//            }
//        }
//        SIZEWORD = WIDTH / max;
//    }*/

    private TreeMap<Integer, LyricObject> lrc_map;
    private float mX; // 屏幕X轴的中点，此值固定，保持歌词在X中间显示
    private float offsetY; // 歌词在Y轴上的偏移量，此值会根据歌词的滚动变小
    private boolean blLrc = false;//是否已加载歌词
    private float touchY; // 当触摸歌词View时，保存为当前触点的Y轴坐标
    private float touchX;
    private int lrcIndex = 0; // 保存歌词TreeMap的下标
    private int SIZEWORD = 45;// 显示歌词文字的大小值
    private int INTERVAL = 45;// 歌词每行的间隔
    private Paint paint = new Paint();// 画笔，用于画不是高亮的歌词
    private Paint paintHL = new Paint(); // 画笔，用于画高亮的歌词，即当前唱到这句歌词

    private int WIDTH;//控件宽度
    private int HEIGHT;//控件高度
    private int lrcHeight;//高亮歌词的Y轴偏移量


    public LyricView(Context context) {
        super(context);
        init();
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //重置
    public void reset() {
        offsetY = HEIGHT / 2;
        lrcIndex = 0;
        if (lrc_map != null) {
            lrc_map = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (blLrc && lrc_map != null) {
            paintHL.setTextSize(SIZEWORD);
            Log.d("test", "onDraw: " + paintHL.getTextSize());
            paint.setTextSize(SIZEWORD);
            if (lrc_map.size() == 0) {
                return;
            }
            LyricObject temp = lrc_map.get(lrcIndex);
            canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * lrcIndex, paintHL);
            // 画当前歌词之前的歌词
            paint.setAlpha(180);
            for (int i = lrcIndex - 1; i >= 0; i--) {
                temp = lrc_map.get(i);
                if (offsetY + (SIZEWORD + INTERVAL) * i < 0) {
                    break;
                }
                if (lrcIndex > 4 && i < lrcIndex - 4) {
                    //前后各绘制四句歌词
                    paint.setAlpha(20);
                }
                canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * i, paint);
                paint.setAlpha(paint.getAlpha() - 40);
            }
            // 画当前歌词之后的歌词
            paint.setAlpha(180);
            for (int i = lrcIndex + 1; i < lrc_map.size(); i++) {
                temp = lrc_map.get(i);
                if (offsetY + (SIZEWORD + INTERVAL) * i > HEIGHT) {
                    break;
                }
                if (i > lrcIndex + 4) {
                    //前后各绘制四句歌词
                    paint.setAlpha(60);
                }
                canvas.drawText(temp.lrc, mX, offsetY + (SIZEWORD + INTERVAL) * i, paint);
                paint.setAlpha(paint.getAlpha() - 40);
            }
        } else {
            paint.setAlpha(180);
            paint.setTextSize(50);
            canvas.drawText("找不到歌词", mX, HEIGHT / 2, paint);
        }
        super.onDraw(canvas);
    }

    boolean isLyrMoved = false;//用手滑动歌词2秒内歌词不允许自动跳转
    float touchYY;
    final ThreadLocal<Integer> touchCount = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    int count = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float tt = event.getY();
        if (!blLrc) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchYY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                touchY = tt - touchY;
                offsetY = offsetY + touchY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(touchYY - event.getY()) > 50) {
                    count++;
                    isLyrMoved = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            touchCount.set(count);
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (touchCount.get() == count) {
                                isLyrMoved = false;
                                offsetY = lrcHeight - (SIZEWORD + INTERVAL) * lrcIndex;
                            }
                        }
                    }).start();
                }
                break;
        }
        touchY = tt;
        return true;
    }

    public void init() {
        lrc_map = new TreeMap<>();

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(180);


        paintHL = new Paint();
        paintHL.setTextAlign(Paint.Align.CENTER);
        paintHL.setColor(Color.RED);
        paintHL.setAntiAlias(true);
        paintHL.setAlpha(255);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mX = w * 0.5f;
        HEIGHT = h;
        WIDTH = w;
        lrcHeight = HEIGHT / 2;
        Log.d("test", "onSizeChanged: " + HEIGHT + "\n" + WIDTH);
        super.onSizeChanged(w, h, oldw, oldh);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        WIDTH=getWidth();
//        Log.d("test", "onMeasure: "+WIDTH);
//        HEIGHT=getHeight();
//        mX=WIDTH*0.5f;
//        lrcHeight=HEIGHT/2;
//    }

    /**
     * 歌词滚动的速度
     *
     * @return 返回歌词滚动的速度
     */
    public Float SpeedLrc() {
        float speed = 0;
        if (offsetY + (SIZEWORD + INTERVAL) * lrcIndex > lrcHeight) {
            speed = ((offsetY + (SIZEWORD + INTERVAL) * lrcIndex - lrcHeight) / 20);

        } else if (offsetY + (SIZEWORD + INTERVAL) * lrcIndex < lrcHeight) {
            Log.i("speed", "speed is too fast!!!");
//            speed = 0;
            speed = ((offsetY + (SIZEWORD + INTERVAL) * lrcIndex - lrcHeight) / 20);
        }
//      if(speed<0.2){
//          speed=0.2f;
//      }
        return speed;
    }

    /**
     * 按当前的歌曲的播放时间，从歌词里面获得那一句
     *
     * @param time 当前歌曲的播放时间
     * @return 返回当前歌词的索引值
     */
    public int SelectIndex(int time) {
        if (!blLrc) {
            return 0;
        }
        if (lrc_map == null || lrc_map.size() == 0) {
            return 0;
        } else {
            int index = 0;
            for (int i = 0; i < lrc_map.size(); i++) {
                LyricObject temp = lrc_map.get(i);
                if (temp.begintime < time) {
                    ++index;
                }
            }
            lrcIndex = index - 1;
            if (lrcIndex < 0) {
                lrcIndex = 0;
            }
            return lrcIndex;
        }
    }

    /**
     * 读取歌词文件
     *
     * @param file 歌词的路径
     */
    public void read(String file) {

        TreeMap<Integer, LyricObject> lrc_read = new TreeMap<>();
        String data = "";
        try {
            File saveFile = new File(file);
            // System.out.println("是否有歌词文件"+saveFile.isFile());
            if (!saveFile.isFile()) {
                blLrc = false;
                return;
            }
            blLrc = true;

            FileInputStream stream = new FileInputStream(saveFile);//  context.openFileInput(file);


            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            int i = 0;
            Pattern pattern = Pattern.compile("\\d{2}");
            while ((data = br.readLine()) != null) {
                // System.out.println("++++++++++++>>"+data);
                data = data.replace("[", "");//将前面的替换成后面的
                data = data.replace("]", "@");
                String splitdata[] = data.split("@");//分隔
                if (data.endsWith("@")) {
                    for (int k = 0; k < splitdata.length; k++) {
                        String str = splitdata[k];

                        str = str.replace(":", ".");
                        str = str.replace(".", "@");
                        String timedata[] = str.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if (timedata.length == 3 && matcher.matches()) {
                            int m = Integer.parseInt(timedata[0]);  //分
                            int s = Integer.parseInt(timedata[1]);  //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m * 60 + s) * 1000 + ms * 10;
                            LyricObject item1 = new LyricObject();
                            item1.begintime = currTime;
                            item1.lrc = "";
                            lrc_read.put(currTime, item1);
                        }
                    }

                } else {
                    String lrcContenet = splitdata[splitdata.length - 1];

                    for (int j = 0; j < splitdata.length - 1; j++) {
                        String tmpstr = splitdata[j];

                        tmpstr = tmpstr.replace(":", ".");
                        tmpstr = tmpstr.replace(".", "@");
                        String timedata[] = tmpstr.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if (timedata.length == 3 && matcher.matches()) {
                            int m = Integer.parseInt(timedata[0]);  //分
                            int s = Integer.parseInt(timedata[1]);  //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m * 60 + s) * 1000 + ms * 10;
                            LyricObject item1 = new LyricObject();
                            item1.begintime = currTime;
                            item1.lrc = lrcContenet;
                            lrc_read.put(currTime, item1);// 将currTime当标签  item1当数据 插入TreeMap里
                            i++;
                        }
                    }
                }

            }
            stream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        /*
         * 遍历hashmap 计算每句歌词所需要的时间
        */
        lrc_map.clear();
        data = "";
        Iterator<Integer> iterator = lrc_read.keySet().iterator();
        LyricObject oldval = null;
        int i = 0;
        while (iterator.hasNext()) {
            Object ob = iterator.next();

            LyricObject val = (LyricObject) lrc_read.get(ob);

            if (oldval == null)
                oldval = val;
            else {
                LyricObject item1 = new LyricObject();
                item1 = oldval;
                item1.timeline = val.begintime - oldval.begintime;
                lrc_map.put(new Integer(i), item1);
                i++;
                oldval = val;
            }
            if (!iterator.hasNext()) {
                lrc_map.put(new Integer(i), val);
            }
        }

    }

    /**
     * @return the blLrc
     */
    public boolean isBlLrc() {
        return blLrc;
    }

    /**
     * @return the offsetY
     */
    public float getOffsetY() {
        return offsetY;
    }

    /**
     * @param offsetY the offsetY to set
     */
    public void setOffsetY(float offsetY) {
        if (!isLyrMoved) {
            this.offsetY = offsetY;
        }
    }

    /**
     * @return 返回歌词文字的大小
     */
    public int getSIZEWORD() {
        return SIZEWORD;
    }

    /**
     * 设置歌词文字的大小
     *
     * @param sIZEWORD the sIZEWORD to set
     */
    public void setSIZEWORD(int sIZEWORD) {
        SIZEWORD = sIZEWORD;
    }

    /**
     * 读取歌词文件
     *
     * @param file 歌词的路径
     */
    public void read(File file) {
        if (lrc_map != null) {
            lrc_map = null;
        }
        lrc_map = new TreeMap<Integer, LyricObject>();
        TreeMap<Integer, LyricObject> lrc_read = new TreeMap<Integer, LyricObject>();
        String data = "";
        try {
            blLrc = true;
            FileInputStream stream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            int i = 0;
            Pattern pattern = Pattern.compile("\\d{2}");
            while ((data = br.readLine()) != null) {
                data = data.replace("[", "");//将前面的替换成后面的
                data = data.replace("]", "@");
                String splitdata[] = data.split("@");//分隔
                String lrcContenet = splitdata[splitdata.length - 1];

                for (int j = 0; j < splitdata.length - 1; j++) {
                    String tmpstr = splitdata[j];

                    tmpstr = tmpstr.replace(":", ".");
                    tmpstr = tmpstr.replace(".", "@");
                    String timedata[] = tmpstr.split("@");
                    Matcher matcher = pattern.matcher(timedata[0]);
                    if (timedata.length == 3 && matcher.matches()) {
                        int m = Integer.parseInt(timedata[0]);  //分
                        int s = Integer.parseInt(timedata[1]);  //秒
                        int ms = Integer.parseInt(timedata[2]); //毫秒
                        int currTime = (m * 60 + s) * 1000 + ms * 10;
                        LyricObject item1 = new LyricObject();
                        item1.begintime = currTime;
                        item1.lrc = lrcContenet;
                        lrc_read.put(currTime, item1);// 将currTime当标签  item1当数据 插入TreeMap里
                        i++;
                    }
                }
            }
            stream.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        /*
         * 遍历hashmap 计算每句歌词所需要的时间
        */
//        lrc_map.clear();
        Iterator<Integer> iterator = lrc_read.keySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Object ob = iterator.next();
            LyricObject val = lrc_read.get(ob);
            lrc_map.put(new Integer(i), val);
            i++;
        }
    }

    /**
     * 根据歌词里面最长的那句来确定歌词字体的大小
     */
    public void SetTextSize(int width) {
//        if (!blLrc) {
//            return;
//        }
//        if (lrc_map == null || lrc_map.size() == 0) {
//            return;
//        }
//        int max = lrc_map.get(0).lrc.length();
//        for (int i = 1; i < lrc_map.size(); i++) {
//            LyricObject lrcStrLength = lrc_map.get(i);
//            if (lrcStrLength.lrc != null) {
//                if (max < lrcStrLength.lrc.length()) {
//                    max = lrcStrLength.lrc.length();
//                }
//            }
//        }
//        if (max != 0) {
//            SIZEWORD = width / max;
//        }
        SIZEWORD = width / 15;//按每行最多显示15个字确定字体大小
    }
}
