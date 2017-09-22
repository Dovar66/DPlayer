package com.dovar.dplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by edit on 2016/8/29.
 */
public class SharedpreferencesUtil {
    /**
     * 频道列表
     **/
    private static final String REMIND_LIST_TABLE = "RemindListTable";
    private static final String REMIND_LIST_TITLE = "RemindListTitle";
    private static final String WIFI_SETTING_TABLE = "WIFI_SETTING_TABLE";
    private static final String WIFI_SETTING_TITLE = "WIFI_SETTING_TITLE";
    private static final String PUSH_SETTING_TABLE = "PUSH_SETTING_TABLE";
    private static final String PUSH_SETTING_TITLE = "PUSH_SETTING_TITLE";
    private static final String CHANNEL_LIST = "channel_list";
    private static final String SP_USER_CHANNEL_LIST = "SP_USER_CHANNEL_LIST";
    private static final String SP_OTHER_CHANNEL_LIST = "SP_OTHER_CHANNEL_LIST";
    private static final String SP_LOCATION = "SP_LOCATION";
    private static final String SP_LAST_LOCATION = "SP_Last_LOCATION";
    private static final String SP_DICOVER_TODAY = "SP_DICOVER_TODAY";//今日看点
    private static final String SP_DICOVER_HOT = "SP_DICOVER_HOT";//热门头条
    private static final String SP_DICOVER_live = "SP_DICOVER_live";//精彩直播
    private static final String SP_DISCOVER_ACTIVITY = "SP_DISCOVER_ACTIVITY";//活动
    private static final String NAME_DICOVER_CACHE = "NAME_DICOVER_CACHE";//
    private static final String NAME_HOTFIX = "NAME_HOTFIX";//
    private static final String SP_HOTFIX_VERSION = "SP_HOTFIX_VERSION";//
    private static final String NAME_WEATHER = "NAME_WEATHER";
    private static final String SP_WEATHER_REFLASH_TIME = "SP_WEATHER_REFLASH_TIME";
    private static final String SP_LIVE_CHANNEL_LIST = "SP_LIVE_CHANNEL_LIST";
    private static final String ADDRESSTABLE = "AddressTable";
    private static final String ADDRESS_TITLE = "AddressTitle";

    private static final String SP_IP_LOCATION = "SP_IP_LOCATION";


    private static final String NAME_AD = "NAME_AD";//广告
    /**
     * 上一次缓存的广告列表hashcode
     **/
    private static final String SP_LAST_ADLIST_HASHCODE = "SP_LAST_ADLIST_HASHCODE";
    /**
     * 上一次展示的广告index
     **/
    private static final String SP_LAST_SHOW_AD_INDEX = "SP_LAST_SHOW_AD_INDEX";

    /**
     * 上一次请求的channelList
     */
    private static final String SP_LAST_CHANNEL_LIST = "SP_LAST_CHANNEL_LIST";
    private static final String SP_MINE_COLLECTS = "SP_MINE_COLLECTS";
    /**
     * 是否显示切换城市
     **/
    private static final String SP_SHOW_SWITCH_CITY = "SP_SHOW_SWITCH_CITY";
    private static final String SP_COLED_CITY_CODE = "SP_COLED_CITY_CODE";

    private static final String SP_SYSTEM = "SP_SYSTEM";
    public static final String SYSTEM_XIAOMI = "SYSTEM_XIAOMI";
    public static final String SYSTEM_HUAWEI = "SYSTEM_HUAWEI";
    public static final String SYSTEM_MEIZU = "SYSTEM_MEIZU";
    private static final String SP_SYSTEM_NAME = "SP_SYSTEM_NAME";
    /**
     * H5字体等设置
     **/
    private static final String NAME_H5_SETTING = "NAME_H5_SETTING";
    private static final String SP_H5_FONT_SIZE = "SP_H5_FONT_SIZE";

    private static final String NEW_READ = "NEW_READ";
    private static final String TV_READ = "TV_READ";
    private static final String LIVE_READ = "LIVE_READ";
    private static final String ADVERTISE_READ = "ADVERTISE_READ";
    private static final String MIX_SPECIAL_READ = "MIX_SPECIAL_READ";
//    private static final String SP_IPMAC_FILE="SP_IPMAC_FILE";
//    private static final String SP_IPMAC_IP="SP_IPMAC_IP";
//    private static finl String SP_IPMAC_MAC="SP_IPMAC_MAC";
    /**
     * h5 分享域名地址
     **/
    private static final String SP_H5_SHARE_DOMAIN = "SP_H5_SHARE_DOMAIN";

    private static final String SP_ORIGINAL_CHANNELIDS = "sp_original_channelids";
    private static final String SP_HAS_CHANGE_CHANNEL = "sp_has_change_channel";

    private static final String NAME_PUSH_SETTING = "name_push_setting";
    private static final String SP_LAST_USER_TAG = "sp_last_user_tag";
    private static final String SP_CURRENT_USER_TAG = "sp_current_user_tag";
    /**
     * 待取消的标签
     */
    private static final String SP_UNSUBSCRIBE_TAGS = "sp_unsubscribe_tags";
    /**
     * 上一次获取用户标签的时间
     */
    private static final String SP_LAST_GET_USER_TAG_TIME = "sp_last_get_user_tag_time";
    /**
     * 上一次上报华为token的时间
     */
    private static final String SP_LAST_UPLOAD_TOKEN_TIME = "sp_last_upload_token_time";


    private static final String SP_WEATHER_STATUS = "SP_WEATHER_STATUS";

    private static final String SP_PUSH_HUAWEI_TOKEN = "sp_push_huawei_token";

    private static final String SP_PUSH_ALIAS = "sp_push_alias_new";
    private static final String NAME_MARKETID="name_marketid";
    private static final String SP_LAST_MARKETID="sp_last_marketid";
    private static final String NAME_REFRESH_TIME="name_refresh_time";
    private static final String SP_LAST_FRFRESH_TIME="sp_last_refresh_time";
    private static final String NAME_WIFI_PHOTO_SETTING="name_photo_setting";
    private static final String SP_WIFI_PHOTO_SETTING="sp_photo_setting";
    /**
     * 华为推送连接失败 提示日期  每日首次打开提示一次，共提示三次，超过了就不再提示弹框，用极光
     */
    private static final String SP_HUAWEI_APPCLIENT_CONNENT_FAIL_DATE="sp_huawei_appclient_connent_fail_date";
    private static final String SP_HUAWEI_APPCLIENT_CONNENT_NOTIFY_COUNT="sp_huawei_appclient_connent_notify_count";
    private static final String SP_HUAWEI_APPCLIENT_CONNENT_FAIL_APP_VERSION="sp_huawei_appclient_connent_fail_app_version";


    public static String getRemindList(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(REMIND_LIST_TABLE,
                Activity.MODE_PRIVATE);
        return preferences.getString(REMIND_LIST_TITLE + id, "");
    }

    public static void setRemindList(Context context, String s, String id) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(REMIND_LIST_TABLE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(REMIND_LIST_TITLE + id, s);
        editor.commit();
    }





    public static String getUserChannelS(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CHANNEL_LIST,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_USER_CHANNEL_LIST, "");
    }


    public static String getOtherChannelS(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CHANNEL_LIST,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_OTHER_CHANNEL_LIST, "");
    }

    public static void saveWifiSetting(Context context, boolean b) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(WIFI_SETTING_TABLE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(WIFI_SETTING_TITLE, b);
        editor.commit();
    }

    public static boolean getWifiSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(WIFI_SETTING_TABLE,
                Activity.MODE_PRIVATE);
        return preferences.getBoolean(WIFI_SETTING_TITLE, true);
    }

    public static void savePushSetting(Context context, boolean b) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(PUSH_SETTING_TABLE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean(PUSH_SETTING_TITLE, b);
        editor.commit();
    }

    public static boolean getPushSetting(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PUSH_SETTING_TABLE,
                Activity.MODE_PRIVATE);
        return preferences.getBoolean(PUSH_SETTING_TITLE, true);
    }


    public static void saveLastChannelList(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(CHANNEL_LIST,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_LAST_CHANNEL_LIST, s);
        editor.commit();
    }


    public static String getLastChannelList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CHANNEL_LIST,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_LAST_CHANNEL_LIST, "");
    }


    public static void saveMineCollect(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(CHANNEL_LIST,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_MINE_COLLECTS, s);
        editor.commit();
    }


    public static String getMineCollect(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CHANNEL_LIST,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_MINE_COLLECTS, "");
    }

    private static final String SubscriptionLocalList = "SubscriptionLocalList";
    private static final String SubscriptionNetList = "SubscriptionNetList";

    public static String getSubscriptionLocalList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SubscriptionLocalList,
                Activity.MODE_PRIVATE);
        return preferences.getString(SubscriptionLocalList, "");
    }

    public static void saveSubscriptionLocalList(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SubscriptionLocalList,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SubscriptionLocalList, s);
        editor.commit();
    }

    public static String getSubscriptionNetList(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences(userId + "_" + SubscriptionNetList,
                Activity.MODE_PRIVATE);
        return preferences.getString(userId + "_" + SubscriptionNetList, "");
    }

    public static void saveSubscriptionNetList(Context context, String s, String userId) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(userId + "_" + SubscriptionNetList,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(userId + "_" + SubscriptionNetList, s);
        editor.commit();
    }

    public static String getLocation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_LOCATION,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_LOCATION, "");
    }

    public static void saveLocation(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SP_LOCATION,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_LOCATION, s);
        editor.commit();
    }
    public static String getIpLocation(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_IP_LOCATION,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_IP_LOCATION, "");
    }

    public static void saveIpLocation(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SP_IP_LOCATION,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_IP_LOCATION, s);
        editor.commit();
    }

    private static final String DiscoverMediaListFragment_list = "DiscoverMediaListFragment_list";

    public static String getDiscoverMediaListFragment_list(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DiscoverMediaListFragment_list,
                Activity.MODE_PRIVATE);
        return preferences.getString(DiscoverMediaListFragment_list, "");
    }

    public static void saveDiscoverMediaListFragment_list(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(DiscoverMediaListFragment_list,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(DiscoverMediaListFragment_list, s);
        editor.commit();
    }


    public static String getDiscoveToday(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_DICOVER_CACHE,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_DICOVER_TODAY, "");
    }

    public static void saveDiscoveToday(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(NAME_DICOVER_CACHE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_DICOVER_TODAY, s);
        editor.commit();
    }


    public static String getDiscoveHot(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_DICOVER_CACHE,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_DICOVER_HOT, "");
    }

    public static void saveDiscoveHot(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(NAME_DICOVER_CACHE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_DICOVER_HOT, s);
        editor.commit();
    }

    public static String getDiscoveLive(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_DICOVER_CACHE,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_DICOVER_live, "");
    }

    public static void saveDiscoveLive(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(NAME_DICOVER_CACHE,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_DICOVER_live, s);
        editor.commit();
    }

    private static final String discoverFragment_list = "discoverFragment_list";

    public static String getSubscribedMediaNews_list(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(discoverFragment_list,
                Activity.MODE_PRIVATE);
        return preferences.getString(discoverFragment_list, "");
    }

    public static void saveSubscribedMediaNews_list(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(discoverFragment_list,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(discoverFragment_list, s);
        editor.commit();
    }

    private static final String hotMediaState_list = "hotMediaState_list";

    public static String getHotMediaState_list(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(hotMediaState_list,
                Activity.MODE_PRIVATE);
        return preferences.getString(hotMediaState_list, "");
    }

    public static void saveHotMediaState_list(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(hotMediaState_list,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(hotMediaState_list, s);
        editor.commit();
    }
//    public static String getHostFixVersion(Context context){
//        SharedPreferences preferences= context.getSharedPreferences(NAME_HOTFIX,
//                Activity.MODE_PRIVATE);
//        return preferences.getString(SP_HOTFIX_VERSION, "");
//    }
//
//    public static void saveHostFixVersion(Context context,String s){
//        SharedPreferences mySharedPreferences= context.getSharedPreferences(NAME_HOTFIX,
//                Activity.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySharedPreferences.edit();
//        editor.putString(SP_HOTFIX_VERSION,s);
//        editor.commit();
//    }

    private static final String LikeList = "LikeList";

    public static String getLikeList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(LikeList,
                Activity.MODE_PRIVATE);
        return preferences.getString(LikeList, "");
    }

    public static void saveLikeList(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(LikeList,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(LikeList, s);
        editor.commit();
    }

    private static final String UniqueId = "UniqueId";

    public static String getUniqueId(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(UniqueId,
                    Activity.MODE_PRIVATE);
            return preferences.getString(UniqueId, "");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setUniqueId(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(UniqueId,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(UniqueId, s);
        editor.commit();
    }



    /**
     * 存储数据
     *
     * @param context
     * @param fileName
     * @param data
     */
    private static void save(Context context, String fileName, String data) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(fileName,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(fileName, data);
        editor.commit();
    }

    private static String get(Context context, String fileName) {
        SharedPreferences preferences = context.getSharedPreferences(fileName,
                Activity.MODE_PRIVATE);
        return preferences.getString(fileName, "");
    }

    private static String parseString(Object obj) {
        return new Gson().toJson(obj);
    }

    private static final String updateDate = "updateDate";

    public static String getUpdateDate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(updateDate,
                Activity.MODE_PRIVATE);
        return preferences.getString(updateDate, "");
    }

    public static void saveUpdateDate(Context context, String s) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(updateDate,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(updateDate, s);
        editor.commit();
    }

    private static final String updateCount = "updateCount";



    public static void saveSystemName(Context context, String name) {
        SharedPreferences mySharedPreferences = context.getSharedPreferences(SP_SYSTEM,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(SP_SYSTEM_NAME, name);
        editor.commit();
    }

    public static String getSystemName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_SYSTEM,
                Activity.MODE_PRIVATE);
        return preferences.getString(SP_SYSTEM_NAME, "");
    }

}
