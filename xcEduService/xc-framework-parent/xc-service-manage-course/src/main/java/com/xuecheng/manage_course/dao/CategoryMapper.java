package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hewei
 * @date 2019/10/31 - 22:17
 */
@Mapper
public interface CategoryMapper {
    CategoryNode findList();
}
