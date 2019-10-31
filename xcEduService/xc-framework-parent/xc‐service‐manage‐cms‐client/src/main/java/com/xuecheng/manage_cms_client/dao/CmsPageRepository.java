package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/10/29 - 17:03
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

}
