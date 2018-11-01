package com.yousucc.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.duanqu.qupai.android.app.QupaiServiceImpl;
import com.duanqu.qupai.engine.session.MovieExportOptions;
import com.duanqu.qupai.engine.session.VideoSessionCreateInfo;
import com.duanqu.qupai.recorder.EditorCreateInfo;
import com.yousucc.R;
import com.yousucc.config.QupaiContant;

/**
 * Created by SensYang on 2016/4/13 0013.
 */
public class QuPaiUtil {
    private static QuPaiUtil instances;
    private QupaiServiceImpl qupaiService;
    private Context context;

    public static QuPaiUtil getInstances(Context context) {
        if (instances == null) {
            instances = new QuPaiUtil(context);
        }
        instances.context = context;
        return instances;
    }

    private QuPaiUtil(Context context) {
        /**
         * 压缩参数，可以自由调节
         */
        MovieExportOptions movieExportOptions = new MovieExportOptions.Builder()
                //baseline, main, high, high10, high422, high444
                .setVideoProfile("high")
                //视频码率 默认6K
                .setVideoBitrate(QupaiContant.DEFAULT_BITRATE)
                //压缩速度和压缩比
                .setVideoPreset(QupaiContant.DEFAULT_VIDEO_PRESET)
                .setVideoRateCRF(QupaiContant.DEFAULT_VIDEO_RATE_CRF)
                //比特流的Level值 VideoLevel
                .setOutputVideoLevel(QupaiContant.DEFAULT_VIDEO_LEVEL)
                .setOutputVideoTune(QupaiContant.DEFAULT_VIDEO_TUNE)
                .configureMuxer(QupaiContant.DEFAULT_VIDEO_MOV_FLAGS_KEY, QupaiContant.DEFAULT_VIDEO_MOV_FLAGS_VALUE)
                .build();

        /**
         * 界面参数
         */
        VideoSessionCreateInfo videoSessionCreateInfo = new VideoSessionCreateInfo.Builder()
                .setOutputDurationLimit(QupaiContant.DEFAULT_DURATION_MAX_LIMIT)
                .setOutputDurationMin(QupaiContant.DEFAULT_DURATION_LIMIT_MIN)
                .setMovieExportOptions(movieExportOptions)
                //美颜参数:1-100.这里不设指定为80,这个值只在第一次设置，之后在录制界面滑动美颜参数之后系统会记住上一次滑动的状态
                .setBeautyProgress(QupaiContant.DEFAULT_BEAUTY_PROGRESS)
                //是否开启美颜
                .setBeautySkinOn(QupaiContant.DEFAULT_BEAUTY_SKIN_ON)
                .setCameraFacing(QupaiContant.CAMERA_FACING_FRONT)
                .setVideoSize(QupaiContant.VIDEO_WIDTH, QupaiContant.VIDEO_HEIGHT)
                .setCaptureHeight(context.getResources().getDimension(R.dimen.qupai_recorder_capture_height_size))
                //是否显示美颜图标
                .setBeautySkinViewOn(true)
                //是否开启闪光灯
                .setFlashLightOn(true)
                //是否显示时间轴
                .setTimelineTimeIndicator(true)
                .build();
        //声明输出视频实例
        //以上配置都完成后，再配置视频编辑器信息
        EditorCreateInfo editorCreateInfo = new EditorCreateInfo();
        editorCreateInfo.setSessionCreateInfo(videoSessionCreateInfo);
        editorCreateInfo.setNextIntent(null);
        //输出图片宽高
        editorCreateInfo.setOutputThumbnailSize(QupaiContant.VIDEO_WIDTH, QupaiContant.VIDEO_HEIGHT);//输出图片宽高
        //创建视频录制界面控制器
        qupaiService = new QupaiServiceImpl.Builder().setEditorCreateInfo(editorCreateInfo).build();
    }

    public void startRecoedActivity(Activity activity, int requestCode) {
        String videoPath = FileAccessor.newOutgoingFilePath();
        qupaiService.getCreateInfo().setOutputVideoPath(videoPath);//输出视频路径
        qupaiService.getCreateInfo().setOutputThumbnailPath(videoPath + ".png");//输出图片路径
        //第二个参数为requestCode
        qupaiService.showRecordPage(activity, requestCode);
    }

    public void startRecoedActivity(Fragment fragment, int requestCode) {
        String videoPath = FileAccessor.newOutgoingFilePath();
        qupaiService.getCreateInfo().setOutputVideoPath(videoPath);//输出视频路径
        qupaiService.getCreateInfo().setOutputThumbnailPath(videoPath + ".png");//输出图片路径
        //第二个参数为requestCode
        qupaiService.showRecordPage(fragment, requestCode);
    }


//    /**
//     * 开始上传
//     */
//    public void startUpload(final String videoPath) {
//        UploadService.getInstance().setQupaiUploadListener(new QupaiUploadListener() {
//            @Override
//            public void onUploadProgress(String uuid, long uploadedBytes, long totalBytes) {
//                int percentsProgress = (int) (uploadedBytes * 100 / totalBytes);
//                Log.e("onUploadProgress--->", "uploadedBytes->" + uploadedBytes + "totalBytes->" + totalBytes);
//            }
//
//            @Override
//            public void onUploadError(String uuid, int errorCode, String message) {
//            }
//
//            @Override
//            public void onUploadComplte(String uuid, int responseCode, String responseMessage) {
//                //http://{DOMAIN}/v/{UUID}.mp4?token={ACCESS-TOKEN}
//
//                //这里返回的uuid是你创建上传任务时生成的uuid.开发者可以使用其他作为标识
//                //videoUrl返回的是上传成功的video地址
//                String videoUrl = QupaiContant.domain + "/v/" + responseMessage + ".mp4" + "?token=" + QupaiContant.accessToken;
//                Log.e("onUploadComplte--->", videoUrl);
//            }
//        });
//        String uuid = UUID.randomUUID().toString();
//        startUpload(createUploadTask(context, uuid, new File(videoPath), new File(videoPath + ".png"), QupaiContant.accessToken, uuid, QupaiContant.shareType, QupaiContant.tags, QupaiContant.description));
//    }
//
//    /**
//     * 创建一个上传任务
//     *
//     * @param context
//     * @param uuid        随机生成的UUID
//     * @param _VideoFile  完整视频文件
//     * @param _Thumbnail  缩略图
//     * @param accessToken 通过调用鉴权得到token
//     * @param space       开发者生成的Quid，必须要和token保持一致
//     * @param share       是否公开 0公开分享 1私有(default) 公开类视频不需要AccessToken授权
//     * @param tags        标签 多个标签用 "," 分隔符
//     * @param description 视频描述
//     * @return
//     */
//    private QupaiUploadTask createUploadTask(Context context, String uuid, File _VideoFile, File _Thumbnail, String accessToken, String space, int share, String tags, String description) {
//        return UploadService.getInstance().createTask(context, uuid, _VideoFile, _Thumbnail, accessToken, space, share, tags, description);
//    }
//
//    /**
//     * 开始上传
//     *
//     * @param data 上传任务的task
//     */
//    private void startUpload(QupaiUploadTask data) {
//        try {
//            UploadService.getInstance().startUpload(data);
//        } catch (IllegalArgumentException exc) {
//            Log.d("upload", "Missing some arguments. " + exc.getMessage());
//        }
//    }
}
