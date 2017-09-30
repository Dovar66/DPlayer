package com.dovar.dplayer.commen.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dovar.dplayer.R;
import com.dovar.dplayer.commen.MyApplication;
import com.dovar.dplayer.commen.glide.CircleTransform;
import com.dovar.dplayer.commen.glide.GlideRoundTransform;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 2016/10/11.
 */

public class PhotoUtil {

    private static String qiniuUrl_1 = "bkt.clouddn.com";
    private static String qiniuUrl_2 = "img1.cloud.itouchtv.cn";

    private static String aliUrl_1 = "image-touchtv.oss-cn-shenzhen.aliyuncs.com";
    private static String aliUrl_2 = "img2-cloud.itouchtv.cn";

    public interface BitmapCallBack {
        void onResourceReady(Bitmap resource);

        void onFail();
    }

    public static String smallPhoto(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        } else {
            if (url.contains(qiniuUrl_1)) {
                return url + "?imageMogr2/thumbnail/!205x291";
            } else if (url.contains(qiniuUrl_2)) {
                return url + "?imageMogr2/thumbnail/!205x291";
            } else if (url.contains(aliUrl_1)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_205,w_291";
            } else if (url.contains(aliUrl_2)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_205,w_291";
            } else {
                return url;
            }
        }

    }

    public static String bigPhoto(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        } else {
            if (url.contains(qiniuUrl_1)) {
                return url + "?imageMogr2/thumbnail/!410x750";
            } else if (url.contains(qiniuUrl_2)) {
                return url + "?imageMogr2/thumbnail/!410x750";
            } else if (url.contains(aliUrl_1)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_410,w_750";
            } else if (url.contains(aliUrl_2)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_410,w_750";
            } else {
                return url;
            }
        }
    }

    public static String setPhotoSize(String url, int size) {
        if (TextUtils.isEmpty(url)) {
            return "";
        } else {
            if (url.contains(qiniuUrl_1)) {
                return url + "?imageMogr2/size-limit/" + size + "k";
            } else if (url.contains(qiniuUrl_2)) {
                return url + "?imageMogr2/size-limit/" + size + "k";
            } else if (url.contains(aliUrl_1)) {
                return url;
            } else if (url.contains(aliUrl_2)) {
                return url;
            } else {
                return url;
            }
        }
    }

    public static String photoWithSize(String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            return "";
        } else {
            if (url.contains(qiniuUrl_1)) {
                return url + "?imageMogr2/thumbnail/!" + width + "x" + height;
            } else if (url.contains(qiniuUrl_2)) {
                return url + "?imageMogr2/thumbnail/!" + width + "x" + height;
            } else if (url.contains(aliUrl_1)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_" + width + ",w_" + height;
            } else if (url.contains(aliUrl_2)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_" + width + ",w_" + height;
            } else {
                return url;
            }
        }
    }


    public static String channelPhoto(String url) {
        /*if(url==null)
            return "";
        else{
            if(url.equals(""))
                return "";
            else
                return url+"?imageMogr2/size-limit/50k";
        }*/
        if (TextUtils.isEmpty(url)) {
            return "";
        } else {
            if (url.contains(qiniuUrl_1)) {
                return url + "?imageMogr2/thumbnail/!527x1275";
            } else if (url.contains(qiniuUrl_2)) {
                return url + "?imageMogr2/thumbnail/!527x1275";
            } else if (url.contains(aliUrl_1)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_527,w_1275";
            } else if (url.contains(aliUrl_2)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_527,w_1275";
            } else {
                return url;
            }
        }
    }

    public static String headPhoto(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        } else {
            if (url.contains(qiniuUrl_1)) {
                return url + "?imageMogr2/thumbnail/!130x130";
            } else if (url.contains(qiniuUrl_2)) {
                return url + "?imageMogr2/thumbnail/!130x130";
            } else if (url.contains(aliUrl_1)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_130,w_130";
            } else if (url.contains(aliUrl_2)) {
                return url + "?x-oss-process=image/resize,m_lfit,h_130,w_130";
            } else {
                return url;
            }
        }

    }

    /**
     * 清除缓存
     */
    public static void clearMemory() {
        Glide.get(MyApplication.getInstance()).clearMemory();
    }

    /**
     * 清除磁盘缓存
     */
    public static void clearDiskCache() {
        //Glide.get(MyApplication.getInstance()).clearDiskCache();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Glide.get(MyApplication.getInstance()).clearDiskCache();
            }
        }.start();
    }

    /**
     * 暂停全部图片请求
     */
    public static void resumeRequests() {
        Glide.with(MyApplication.getInstance()).resumeRequests();
    }

    /**
     * 恢复全部图片请求
     */
    public static void pauseRequests() {
        Glide.with(MyApplication.getInstance()).pauseRequests();
    }

    public static void loadGif(ImageView imageView, String photoUrl) {
        Glide.with(MyApplication.getInstance()).load(photoUrl).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    public static void loadGif(ImageView imageView, String photoUrl, int error, int placeholder) {
        Glide.with(MyApplication.getInstance()).load(photoUrl).asGif().placeholder(placeholder).error(error).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    /**
     * 加载本地drawable
     *
     * @param imageView
     * @param drawable
     */
    public static void loadLocalPhoto(ImageView imageView, int drawable) {
        try {
            Glide.with(MyApplication.getInstance())
                    .load(drawable)
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载圆形bitmap
     *
     * @param imageView
     * @param bitmap
     */
    public static void loadCircleBitmap(ImageView imageView, Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(MyApplication.getInstance())
                    .load(bytes)
                    .transform(new CircleTransform(MyApplication.getInstance()))
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载bitmap
     *
     * @param imageView
     * @param bitmap
     */
    public static void loadBitmap(ImageView imageView, Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            Glide.with(MyApplication.getInstance())
                    .load(bytes)
                    .dontAnimate()
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载图片，回调一个bitmap对象
     *
     * @param photoUrl
     * @param callBack
     */
    public static void loadBitmap(String photoUrl, final BitmapCallBack callBack) {
        try {
            Glide.with(MyApplication.getInstance())
                    .load(photoUrl)
                    .asBitmap()
                    .priority(Priority.HIGH)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            callBack.onResourceReady(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            callBack.onFail();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载本地圆形drawable
     *
     * @param imageView
     * @param drawable
     */
    public static void loadLocalCirclePhoto(ImageView imageView, int drawable) {
        try {
            Glide.with(MyApplication.getInstance())
                    .load(drawable)
                    .crossFade()
                    .transform(new CircleTransform(MyApplication.getInstance()))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载本地圆角drawable
     *
     * @param imageView
     * @param drawable
     */
    public static void loadLocalRoundPhoto(ImageView imageView, int drawable, int dp) {
        Glide.with(MyApplication.getInstance())
                .load(drawable)
                .crossFade()
                .transform(new GlideRoundTransform(MyApplication.getInstance(), dp))
                .into(imageView);
    }

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param photoUrl  图片url
     */
    public static void loadPhoto(ImageView imageView, String photoUrl) {
        loadPhoto(imageView, photoUrl, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载的图片
     * @param errorPhoto   加载失败的图片
     */
    public static void loadPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto) {
        loadPhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, true);
    }

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param cache        是否加入到本地缓存
     */
    public static void loadPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, boolean cache) {
        loadPhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, cache, Priority.NORMAL);
    }

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param priority     加载优先级
     */
    public static void loadPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, Priority priority) {
        loadPhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, true, priority);
    }

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param cache        是否加入本地缓存
     * @param priority     加载优先级别
     */
    public static void loadPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, boolean cache, Priority priority) {
        if (photoUrl.endsWith(".gif")) {
            loadGif(imageView, photoUrl, preLoadPhoto, errorPhoto);
        } else {
            try {
                Glide.with(MyApplication.getInstance())
                        .load(photoUrl)
                        .error(errorPhoto)
                        .placeholder(preLoadPhoto)
                        .priority(priority)
                        .dontAnimate()
                        .diskCacheStrategy(cache == true ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                        .into(imageView);

            } catch (Exception e) {
                PhotoUtil.clearMemory();
            }
        }

    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param photoUrl  图片url
     */
    public static void loadCirclePhoto(ImageView imageView, String photoUrl) {
        loadCirclePhoto(imageView, photoUrl, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载的图片
     * @param errorPhoto   加载失败的图片
     */
    public static void loadCirclePhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto) {
        loadCirclePhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, true);
    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param cache        是否加入到本地缓存
     */
    public static void loadCirclePhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, boolean cache) {
        loadCirclePhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, cache, Priority.NORMAL);
    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param priority     加载优先级
     */
    public static void loadCirclePhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, Priority priority) {
        loadCirclePhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, true, priority);
    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param cache        是否加入本地缓存
     * @param priority     加载优先级别
     */
    public static void loadCirclePhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, boolean cache, Priority priority) {
        try {
            Glide.with(MyApplication.getInstance())
                    .load(photoUrl)
                    .error(errorPhoto)
                    .placeholder(preLoadPhoto)
                    .priority(priority)
                    .transform(new CircleTransform(MyApplication.getInstance()))
                    .dontAnimate()
                    .diskCacheStrategy(cache == true ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                    .into(imageView);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            PhotoUtil.clearMemory();
        }
    }


    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param photoUrl  图片url
     */
    public static void loadRoundPhoto(ImageView imageView, String photoUrl, int dp) {
        loadRoundPhoto(imageView, photoUrl, R.mipmap.ic_launcher, R.mipmap.ic_launcher, dp);
    }

    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载的图片
     * @param errorPhoto   加载失败的图片
     */
    public static void loadRoundPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, int dp) {
        loadRoundPhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, true, dp);
    }

    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param cache        是否加入到本地缓存
     */
    public static void loadRoundPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, boolean cache, int dp) {
        loadRoundPhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, cache, Priority.NORMAL, dp);
    }

    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param priority     加载优先级
     */
    public static void loadRoundPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, Priority priority, int dp) {
        loadRoundPhoto(imageView, photoUrl, preLoadPhoto, errorPhoto, true, priority, dp);
    }

    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param photoUrl     图片url
     * @param preLoadPhoto 预加载图片
     * @param errorPhoto   加载失败图片
     * @param cache        是否加入本地缓存
     * @param priority     加载优先级别
     */
    public static void loadRoundPhoto(final ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, boolean cache, Priority priority, final int dp) {
        /*Glide.with(MyApplication.getInstance())
                .load(photoUrl)
                .error(errorPhoto)
                .placeholder(preLoadPhoto)
                .priority(priority)
                .transform(new GlideRoundTransform(MyApplication.getInstance(),dp))
                .dontAnimate()
                .diskCacheStrategy(cache==true? DiskCacheStrategy.ALL:DiskCacheStrategy.NONE)
                .fitCenter()
                .into(imageView);*/

        try {
            Glide.with(MyApplication.getInstance())
                    .load(photoUrl)
                    .asBitmap()
                    .priority(priority)
                    .diskCacheStrategy(cache == true ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                    .placeholder(preLoadPhoto)
                    .error(errorPhoto)
                    .centerCrop().into(new BitmapImageViewTarget(imageView) {

                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MyApplication.getInstance().getResources(), resource);
                    circularBitmapDrawable.setCornerRadius(dp);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }

            });
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            PhotoUtil.clearMemory();
        }
    }


    public static void loadPhoto(ImageView imageView, String photoUrl, int preLoadPhoto, int errorPhoto, DiskCacheStrategy cacheStrategy) {
        try {
            Glide.with(MyApplication.getInstance())
                    .load(photoUrl)
                    .error(errorPhoto)
                    .placeholder(preLoadPhoto)
                    .priority(Priority.NORMAL)
                    .dontAnimate()
                    .diskCacheStrategy(cacheStrategy)
                    .into(imageView);
        } catch (Exception e) {
            int i;
        }

    }

    public static final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dplayer/image/";

    /**
     * 保存方法
     */
    public static void saveBitmap(Bitmap bm) {
        Context context = MyApplication.getInstance();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            File parent = new File(path);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File f = new File(path, fileName);
            if (f.exists()) {
                f.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(f);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

                // 其次把文件插入到系统图库
                try {
                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            f.getAbsolutePath(), fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // 最后通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }


    public static boolean copyFile(String oldPath, String newPath) {
        try {
            File imageDir = new File(PhotoUtil.path);
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            File f = new File(newPath);
            if (f.exists()) {
                f.delete();
            }

            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return false;
        }

    }

    public static void sendMediaScannerScanFile(String filePath) {
        MyApplication.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));

    }


    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取图片真实类型
     *
     * @param filePath
     * @return
     */
    public static String getFileType(String filePath) {

        String type = "jpg";
        FileInputStream is = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[4];
            try {
                is.read(b, 0, b.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            type = bytesToHexString(b).toUpperCase();
            if (type.contains("FFD8FF")) {
                return "jpg";
            } else if (type.contains("89504E47")) {
                return "png";
            } else if (type.contains("47494638")) {
                return "gif";
            } else if (type.contains("49492A00")) {
                return "tif";
            } else if (type.contains("424D")) {
                return "bmp";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return type;
    }
}
