package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourseCollect;
import com.atguigu.eduservice.entity.chapter.chapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author szy
 * @since 2020-05-20
 */
public interface EduChapterService extends IService<EduChapter> {

    List<chapterVo> getChapterVideoByCourseId(String courseId);
}
