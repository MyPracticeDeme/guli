package com.atguigu.eduservice.controller;




import com.atguigu.common.utils.R;
import com.atguigu.eduservice.entity.chapter.chapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 课程收藏 前端控制器
 * </p>
 *
 * @author szy
 * @since 2020-05-20
 */
@RestController
@RequestMapping("/eduservice/edu-chapter")
public class EduChapterController {
    @Autowired
    private EduChapterService eduChapterService;

    //课程大纲列表,根据课程id查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<chapterVo> list=eduChapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("allchapterVo",list);
    }
}

