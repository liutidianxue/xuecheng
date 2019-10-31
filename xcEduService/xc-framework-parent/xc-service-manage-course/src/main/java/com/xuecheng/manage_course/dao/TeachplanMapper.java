package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hewei
 * @date 2019/10/30 - 23:41
 */
@Mapper
public interface TeachplanMapper {
    //课程计划查询
    public TeachplanNode selectList(String courseId);


    //想跟众筹一样拿数据而写的方法，跟视频无关
    //根据课程id查询所有的课程计划
    @Select("select * from teachplan where courseId=#{courseId}")
    List<Teachplan> findAllByCourseId(String courseId);
}
