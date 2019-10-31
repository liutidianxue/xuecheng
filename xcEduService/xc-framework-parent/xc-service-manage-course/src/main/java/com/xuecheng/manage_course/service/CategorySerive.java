package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hewei
 * @date 2019/10/31 - 22:16
 */
@Service
public class CategorySerive {
    @Autowired
    CategoryMapper categoryMapper;

    public CategoryNode findList() {

        return categoryMapper.findList();
    }
}
