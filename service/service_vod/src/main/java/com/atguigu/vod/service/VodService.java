package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface VodService {
    //上传视频到aliyun
    String uploadVideoAliyun(MultipartFile file);
}
