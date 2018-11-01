package com.yousucc.config;

/**
 * Created by SensYang on 2016/4/13 0013.
 */
public class QupaiContant {
    /**
     * 默认最大时长
     * 允许拍摄的最大时长，时长越大，产生的视频文件越大
     */
    public static int DEFAULT_DURATION_MAX_LIMIT = 8;
    /**
     * 允许拍摄的最小时长，时长越大，产生的视频文件越大。
     */
    public static int DEFAULT_DURATION_LIMIT_MIN = 2;
    /**
     * 默认码率
     * 视频码率 默认6K
     */
    public static int DEFAULT_BITRATE = 600 * 1024;
    /**
     * 默认CRF参数
     * 可选范围是0-51, 值越低质量越好、正常范围是18-28，0是无损的，缺省值为23.
     */
    public static int DEFAULT_VIDEO_RATE_CRF = 6;

    /**
     * 压缩速度和压缩比VideoPreset
     * ultrafast,superfast, veryfast, faster, fast, medium, slow, slower, veryslow, placebo
     */
    public static String DEFAULT_VIDEO_PRESET = "faster";
    /**
     * 比特流的Level值 VideoLevel
     * 可选取值范围10-51，默认40。它用来告诉解码器需要支持的什么级别的兼容性。
     */
    public static int DEFAULT_VIDEO_LEVEL = 30;

    /**
     * 可选择参数 film, animation, grain, stillimage, psnr, ssim, fastdecode, zerolatency
     * VideoTune
     */
    public static String DEFAULT_VIDEO_TUNE = "zerolatency";
    /**
     * movflags_KEY
     */
    public static String DEFAULT_VIDEO_MOV_FLAGS_KEY = "movflags";

    /**
     * movflags_VALUE
     */
    public static String DEFAULT_VIDEO_MOV_FLAGS_VALUE = "+faststart";

    /**
     * 设定美颜程度的参数，可设置的值为1-100
     */
    public static int DEFAULT_BEAUTY_PROGRESS = 80;
    /**
     * 默认是否开启美颜
     */
    public static boolean DEFAULT_BEAUTY_SKIN_ON = true;
    /***/
    /**
     * 前置摄像头
     */
    public static int CAMERA_FACING_FRONT = 1;
    /**
     * 后置摄像头
     */
    public static int CAMERA_FACING_BACK = 0;
    /**
     * 视频宽度
     */
    public static int VIDEO_WIDTH = 360;
    /**
     * 视频高度
     */
    public static int VIDEO_HEIGHT = 360;

    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    public static String WATER_MARK_PATH = "assets://Qupai/watermark/qupai-logo.png";
    /**
     * 水印位置1为右上，2为右下
     */
    public static int WATER_MARK_POSITION = 1;

    /**
     * 趣拍appkey
     */
    public static String appkey = "206d95ab3c22eaf";
    /**
     * 趣拍appsecret
     */
    public static String appsecret = "6f697534dba542538dd2541fa5c689df";
    /**
     * 视频标签
     * */
    public static String tags = "tags";
    /**
     * 视频描述
     * */
    public static String description = "description";

    public static int shareType = 1; //是否公开 0公开分享 1私有(default) 公开类视频不需要AccessToken授权

    public static String accessToken;//accessToken 通过调用授权接口得到
    public static String space = "workspace"; //存储目录 建议使用uid cid之类的信息
    public static String domain = "yousucc.s.qupai.me";//当前TEST应用的域名。该地址每个应用都不同 sdk.s.qupai.me
}
