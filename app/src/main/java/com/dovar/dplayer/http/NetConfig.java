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
    public static final String BaseUrl_Music = "http://v3.wufazhuce.com:8000/api/";

    public static final String BAIDU_MUSIC = "http://tingapi.ting.baidu.com/v1/restserver/";

    /**
     *   百度新歌榜
     *   此url使用Volley直接请求能正常返回，使用浏览器也可正常访问
     *   但是使用okHttp则必须添加header{key:User-Agent value:任意}，否则返回错误码403{}
     */
    public static final String BAIDU_HOT = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=20";
}
