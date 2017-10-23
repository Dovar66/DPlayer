package com.dovar.dplayer.common.utils;

import com.dovar.dplayer.BuildConfig;

/**
 * Created by Administrator on 2017-09-25.
 */

public class Constants {
    //调试模式
    public static boolean isDebug = BuildConfig.DEBUG;

    //播放模式
    public static final int MODE_INORDER = 1;//顺序播放
    public static final int MODE_RANDOM = 2;//随机播放
    public static final int MODE_SINGLE = 3;//单曲循环

    public static final int REQUEST_CODE = 666;//startActivityForResult默认使用这个请求码，方便管理、节约内存。调用相机或相册除外
    public static final int REQUEST_PERMISSIONS = 1;//请求权限
    public static final int SELECT_BY_GALLEY = 1001;//从图库选择
    public static final int TAKE_PHOTO = 1002;//拍照

    //切歌
    public static final String ACTION_NEWPLAY = "play_new_music";
    // 继续播放
    public static final String ACTION_PLAY = "play_music";
    // 暂停播放
    public static final String ACTION_PAUSE = "pause_music";
    // 上一曲
    public static final String ACTION_PREVIOUS = "previous_music";
    // 下一曲
    public static final String ACTION_NEXT = "next_music";
    // 移动seekbar手动改变播放进度
    public static final String ACTION_CHANGE = "changeProgress_music";
    // 更新实时播放进度
    public static final String ACTION_UPDATEPROGRESS = "updateProgress";
    //传递信息
    public static final String ACTION_TRANSFERDATA = "transfer_data";
    //增加歌曲到歌单
    public static final String ACTION_ADD = "add_data";
    //删除歌单中一首歌曲
    public static final String ACTION_DELETE = "delete_data";
    //创建新歌单,并开始播放
    public static final String ACTION_NEWPLAYLIST = "new_playlist";
}
