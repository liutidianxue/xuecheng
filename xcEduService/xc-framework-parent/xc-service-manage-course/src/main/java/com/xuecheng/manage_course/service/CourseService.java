package com.xuecheng.manage_course.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author hewei
 * @date 2019/10/30 - 23:59
 */
@Service
public class CourseService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CmsPageClient cmsPageClient;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;



    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isBlank(teachplan.getPname()) || StringUtils.isBlank(teachplan.getCourseid())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //课程id
        String courseid = teachplan.getCourseid();
        //父节点的id
        String parentid = teachplan.getParentid();
        if(StringUtils.isBlank(parentid)){
            //获取课程的根节点
            parentid = getTeachplanRoot(courseid);
        }
        //查询跟节点信息
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan teachplan1 = optional.get();
        //父节点的级别
        String parent_grade = teachplan1.getGrade();
        //创建一个新节点准备添加
        Teachplan teachplanNew = new Teachplan();
        //将teachplan的属性拷贝到teachplanNew中
        BeanUtils.copyProperties(teachplan, teachplanNew);
        //要设置必要的属性
        teachplanNew.setParentid(parentid);
        if(parent_grade.equals("1")){
            teachplanNew.setGrade("2");
        }else{
            teachplanNew.setGrade("3");
        }
        teachplanNew.setStatus("0");//未发布
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private String getTeachplanRoot(String courseid) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //调用dao查询teachplan表得到该课程的根节点(一级节点)
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if (teachplanList == null || teachplanList.size()<=0) {
            //新添加一个课程的根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseid);
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplan.setPname(courseBase.getName());
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }

        //返回根节点的id
        return teachplanList.get(0).getId();
    }

    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size = 10;
        }

        PageHelper.startPage(page,size);
        List<CourseInfo> courseInfoList = this.courseMapper.findCourseInfoList(courseListRequest);
        PageInfo<CourseInfo> pageInfo = new PageInfo<>(courseInfoList);

        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(courseInfoList);
        queryResult.setTotal(pageInfo.getTotal());


        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);

    }

    //添加课程提交
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);

        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    public CourseBase getCourseBaseById(String courseid) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        CourseBase one = this.getCourseBaseById(id);
        if (one == null) {
            throw new RuntimeException("找不到该课程");
        }
        //修改课程信息
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        CourseBase save = courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    //有就修改，没有就新增
    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = this.getCourseMarketById(id);
        if (one != null) {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        }else{
            //添加课营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            one.setId(id);
            courseMarketRepository.save(one);
        }
        return one;
    }

    //查询课程视图，包括基本信息、图片、营销、课程计划
    public CourseView getCoruseView(String id) {
        CourseView courseView= new CourseView();

        //查询课程基本信息
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if(courseBaseOptional.isPresent()){
            CourseBase courseBase = courseBaseOptional.get();
            courseView.setCourseBase(courseBase);
        }
        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            courseView.setCoursePic(coursePic);
        }

        //课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if(marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            courseView.setCourseMarket(courseMarket);
        }

        //课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);

        return courseView;
    }

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId){
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if(baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_DENIED_DELETE);
        return null;
    }

    public CoursePublishResult preview(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //请求cms添加页面
        //准备cmspage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//数据模型url
        cmsPage.setPageName(id+".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id

        //远程调用
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if(!cmsPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览的url
        String url = previewUrl + pageId;
        //返回CoursePublishResult对象（当中包含了页面预览的url）
        return new CoursePublishResult(CommonCode.SUCCESS,url);

    }

    //课程发布
    @Transactional
    public CoursePublishResult publish(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //准备页面信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);//站点id
        cmsPage.setDataUrl(publish_dataUrlPre+id);//数据模型url
        cmsPage.setPageName(id+".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);//页面物理路径
        cmsPage.setPageWebPath(publish_page_webpath);//页面webpath
        cmsPage.setTemplateId(publish_templateId);//页面模板id
        //调用cms一键发布接口将课程详情页面发布到服务器
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        //保存课程的发布状态为"已发布"
        CourseBase courseBase = this.saveCoursePubState(id);
        if (courseBase == null) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        //保存课程索引信息
        //...

        //缓存课程的信息
        //...
        //得到页面的url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);

    }

    //更新课程状态为已发布 202002
    private CourseBase  saveCoursePubState(String courseId){
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }
}
