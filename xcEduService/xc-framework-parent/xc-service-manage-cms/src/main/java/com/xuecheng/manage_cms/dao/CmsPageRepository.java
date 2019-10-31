package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/10/16 - 20:22
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    //根据页面名称、站点id、页面访问路径查询一个CmsPage对象
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
