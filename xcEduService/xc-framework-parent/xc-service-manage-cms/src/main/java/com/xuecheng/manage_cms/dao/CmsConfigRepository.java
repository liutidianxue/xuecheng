package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/10/21 - 11:36
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
