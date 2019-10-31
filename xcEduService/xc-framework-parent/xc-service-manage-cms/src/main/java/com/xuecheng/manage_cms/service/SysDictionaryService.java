package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hewei
 * @date 2019/10/31 - 23:01
 */
@Service
public class SysDictionaryService {
    @Autowired
    SysDictionaryRepository sysDictionaryRepository;


    public SysDictionary getByType(String type) {
        return sysDictionaryRepository.findBydType(type);
    }
}
