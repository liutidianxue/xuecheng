package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hewei
 * @date 2019/11/1 - 19:15
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
