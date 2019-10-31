package com.xuecheng.api.sys_dictionary;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hewei
 * @date 2019/10/31 - 22:57
 */
@Api(value="数据字典接口",description="提供数据字典接口的管理、查询功能")
public interface SysDictionaryControllerApi {
    //根据字典type查询字典信息
    @ApiOperation(value="数据字典查询接口")
    SysDictionary getByType(String type);

}
