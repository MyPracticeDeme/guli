package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.chapter.chapterVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author szy
 * @since 2020-05-20
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);
    //根据课程查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);
    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);
}
