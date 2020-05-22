package com.atguigu.eduservice.service.impl;



import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.entity.chapter.chapterVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程收藏 服务实现类
 * </p>
 *
 * @author szy
 * @since 2020-05-20
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;//注入小结service
    //课程大纲列表,根据课程id查询
    @Override
    public List<chapterVo> getChapterVideoByCourseId(String courseId) {
        //1.根据id查询所有章节
        QueryWrapper<EduChapter> wrapperChapter=new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2.根据id查询所有小结
        QueryWrapper<EduVideo> wrapperVideo=new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);

        //创建list集合，用于最终封装数据
        ArrayList<chapterVo> finalList = new ArrayList<>();
        //3.遍历查询章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            chapterVo chapterVo = new chapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            //创建集合用于封装章节的小结
            ArrayList<VideoVo> videoList = new ArrayList<>();
            //4.遍历查询小结list集合进行封装
            for (int i1 = 0; i1 < eduVideoList.size(); i1++) {
                EduVideo eduVideo = eduVideoList.get(i1);
                //判断
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoList.add(videoVo);
                }

            }
            chapterVo.setChildren(videoList);

        }
        return finalList;
    }
}
