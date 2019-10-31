package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/10/29 - 17:04
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
