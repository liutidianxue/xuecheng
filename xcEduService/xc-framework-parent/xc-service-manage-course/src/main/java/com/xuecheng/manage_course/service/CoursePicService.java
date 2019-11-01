package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CoursePicMapper;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author hewei
 * @date 2019/11/2 - 0:38
 */
@Service
public class CoursePicService {
    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CoursePicMapper coursePicMapper;

    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic  coursePic= null;
        if(optional.isPresent()){
            coursePic = optional.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        //根据id查找CoursePic，没有就是新增，有就是修改了。
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic findById(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    public ResponseResult delete(String courseId) {
        //执行删除，返回1表示删除成功，否则表示删除失败
        int result = coursePicMapper.delete(courseId);
        //long result =  coursePicRepository.deleteByCourseid(courseId);    //不行
        if(result == 1){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }
}
