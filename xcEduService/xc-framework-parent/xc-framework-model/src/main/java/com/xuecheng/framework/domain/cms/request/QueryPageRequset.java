package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

/**
 * @author hewei
 * @date 2019/10/16 - 17:20
 */
@Data
public class QueryPageRequset {
    //站点id
    private String siteId;
    //页面ID
    private String pageId;
    //页面名称
    private String pageName;
    //别名
    private String pageAliase;
    //模板id
    private String templated;


}
