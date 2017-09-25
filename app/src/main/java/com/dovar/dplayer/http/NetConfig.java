package com.dovar.dplayer.http;

/**
 * Created by Administrator on 2017-09-12.
 */

public class NetConfig {
    /**
     * 开眼 Eyepetizer
     * 拼接参数：
     * <p>
     * udid：用户唯一标识。该参数可为空也可去除
     * vc：???，固定值168。该参数可为空也可去除
     * vn：客户端版本。该参数可为空也可去除
     * deviceModel：手机信息。该参数可为空也可去除
     * first_channel：???，固定值 eyepetizer_baidu_market。该参数可为空也可去除
     * last_channel：???，固定值 eyepetizer_baidu_market。该参数可为空也可去除
     * system_version_code：手机系统版本。该参数可为空也可去除
     */
    public static final String Url_Video = "http://baobab.kaiyanapp.com/api/v4/tabs/selected";

    public static final String BaseUrl_kaiyan="http://baobab.kaiyanapp.com/api/";

    //一个
    public static final String Url_Music = "";
}
