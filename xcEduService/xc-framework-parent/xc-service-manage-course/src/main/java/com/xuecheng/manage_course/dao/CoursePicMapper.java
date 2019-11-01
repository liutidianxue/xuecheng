package com.xuecheng.manage_course.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hewei
 * @date 2019/11/2 - 2:30
 */
@Mapper
public interface CoursePicMapper {

    @Delete("delete from course_pic where courseId=#{courseId}")
    int delete(String courseId);
}
