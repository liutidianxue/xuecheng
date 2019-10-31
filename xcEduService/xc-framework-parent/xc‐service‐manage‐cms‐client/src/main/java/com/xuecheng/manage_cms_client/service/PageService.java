package com.xuecheng.manage_cms_client.service;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;


/**
 * @author hewei
 * @date 2019/10/29 - 17:04
 */
@Service
public class PageService {
    public static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    public void  savePageToServerPath(String pageId){
        //根据pageId查询cmsPage
        CmsPage cmsPage = this.findCmsPageById(pageId);
        //得到html的文件id，从cmsPage中获取
        String htmlFileId = cmsPage.getHtmlFileId();
        //从grids中查询html文件
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream == null) {
            LOGGER.error("getFileById InputStream is null,htmlFileId:{}", htmlFileId);
            return;
        }
        //得到站点id
        String siteId = cmsPage.getSiteId();
        //得到站点信息
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        //得到站点的物理路径
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //得到页面的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        //将html文件保存到服务器物理路径上
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //根据站点id查询站点信息
    private CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }


    //根据文件id从Grids中查询文件内容
    private InputStream getFileById(String fileId) {
        //文件对象
        GridFSFile gridFSFile = this.gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //根据页面id查询页面信息
    public CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> optional = this.cmsPageRepository.findById(pageId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
