package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/10/31 - 23:03MongoRepository
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {
    //根据字典分类查询字典信息
    SysDictionary findBydType(String dType);
}
