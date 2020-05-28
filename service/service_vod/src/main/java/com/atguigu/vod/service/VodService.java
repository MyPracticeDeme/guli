package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    //上传视频到aliyun
    String uploadVideoAliyun(MultipartFile file);

    void removeMoreAliyunVideo(List videoIdList);
}
