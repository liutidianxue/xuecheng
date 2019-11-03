package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/11/3 - 16:26
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
