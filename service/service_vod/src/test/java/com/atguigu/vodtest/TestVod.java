package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class TestVod {
    public static void main(String[] args) throws Exception {
        //上传视频方法
        /**
         * 本地文件上传接口
         *
         * @param accessKeyId
         * @param accessKeySecret
         * @param title
         * @param fileName
         */
            String accessKeyId="LTAI4G4kxFVJAFupJzwK1qdT";
            String accessKeySecret="yaGthBKMzluZAYXH20mqFanRQtxRQu";
            String title="3 - What If I Want to Move Faster - upload by sdk"; //上传后文件的名称
            String fileName="D:/MyData/上硅谷项目/项目资料/1-阿里云上传测试视频/6 - What If I Want to Move Faster.mp4";//本地文件的路径和名称
            UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
            /* 可指定分片上传时每个分片的大小，默认为2M字节 */
            request.setPartSize(2 * 1024 * 1024L);
            /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
            request.setTaskNum(1);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadVideoResponse response = uploader.uploadVideo(request);
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
            } else {
                /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");

        }

    }

    public static void getPlayAuth() throws Exception{
        //1.根据视频id获取视频播放品证
        DefaultAcsClient client = InitObject.initVodClient("LTAI4G4kxFVJAFupJzwK1qdT", "yaGthBKMzluZAYXH20mqFanRQtxRQu");
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        request.setVideoId("c4d2587668f64e99baf0e65896ee930d");
        response = client.getAcsResponse(request);
        System.out.println("playAuth"+response.getPlayAuth());
    }
    public static void getPlayUrl() throws Exception{
        //1.根据视频id获取视频播放地址
        //创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI4G4kxFVJAFupJzwK1qdT", "yaGthBKMzluZAYXH20mqFanRQtxRQu");
        //创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        //向request对象里面设置视频id
        request.setVideoId("c4d2587668f64e99baf0e65896ee930d");

        //调用初始化对象里面的方法，传递request，获取数值
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");

    }
}
