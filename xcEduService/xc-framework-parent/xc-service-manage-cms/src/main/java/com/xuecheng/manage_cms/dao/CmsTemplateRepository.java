package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/10/23 - 16:31
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
