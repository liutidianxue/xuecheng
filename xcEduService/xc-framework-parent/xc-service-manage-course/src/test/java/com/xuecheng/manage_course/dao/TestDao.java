package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.manage_course.service.CategorySerive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CategorySerive categorySerive;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }

    @Test
    public void testFindTeachplan(){
        TeachplanNode teachplanNode = teachplanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode);
    }

    //测试分页
    @Test
    public void testPageHelper(){
        CourseListRequest courseListRequest = new CourseListRequest();
        PageHelper.startPage(1,10);
        List<CourseInfo> courseInfoList = courseMapper.findCourseInfoList(courseListRequest);
        PageInfo<CourseInfo> pageInfo = new PageInfo<>(courseInfoList);
        long total = pageInfo.getTotal();
        List<CourseInfo> list = pageInfo.getList();


        //用了PageHelper.startPage(1,10);后，返回值确实是Page<CourseInfo>视频确实可以那样写。不过我用List<CourseInfo>作为返回值，算是父类接收子类
        //要想用getTotal()，当然得强转成子类了或者是new PageInfo()。但是别的地方也可能会用到findCourseInfoList的方法，查询的结果返回值肯定就不能是Page<CourseInfo>
        //所以还是用List<CourseInfo>，想用getTotal()就new PageInfo<>(courseInfoList);而且PageInfo里面的内容更加详细。
        Page<CourseInfo> aaaa = (Page<CourseInfo>) courseInfoList;
        System.out.println(aaaa.getResult());
        System.out.println("------------");
        System.out.println(aaaa.getTotal());
        System.out.println("------------");
        System.out.println(list);
        System.out.println("------------");
        System.out.println(courseInfoList==list);
        System.out.println("------------");
        System.out.println(pageInfo);
        System.out.println("------------");
        System.out.println(total);


    }


    @Test
    public void findCategoryList(){
        CategoryNode list = categorySerive.findList();
        System.out.println(list);
    }















    //跟视频无关，想跟众筹一样只查询一次拿数据。失败了。
    @Test
    public void testFindAllTeachplanByCourseId(){
        List<Teachplan> teachplanList = teachplanMapper.findAllByCourseId("4028e581617f945f01617f9dabc40000");

        Map<String,Teachplan> map = new HashMap<>();
        for (Teachplan teachplan : teachplanList) {
            map.put(teachplan.getId(),teachplan);
        }

        TeachplanNode result = new TeachplanNode();
        /*for (Teachplan teachplan : teachplanList) {
            Teachplan child = teachplan;
            String parentid = teachplan.getParentid();
            Teachplan parent = map.get(parentid);

            TeachplanNode childrenNode = new TeachplanNode();
            BeanUtils.copyProperties(child,childrenNode);

            TeachplanNode parentNode = new TeachplanNode();
            BeanUtils.copyProperties(parent,parentNode);
            parentNode.getChildren().add(childrenNode);

            if(parentNode.getParentid().equals("0")){
                result = parentNode;
            }else{

            }
        }*/

        for (Teachplan teachplan : teachplanList) {
            Teachplan child = teachplan;
            TeachplanNode childrenNode = new TeachplanNode();
            BeanUtils.copyProperties(child,childrenNode);
            if (teachplan.getParentid().equals("0")){
                result = childrenNode;
            }else{
                Teachplan parent = map.get(teachplan.getParentid());
                TeachplanNode parentNode = new TeachplanNode();
                BeanUtils.copyProperties(parent,parentNode);
                parentNode.getChildren().add(childrenNode);
            }
        }


        System.out.println(result);
        System.out.println(111);

    }


}
