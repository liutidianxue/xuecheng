package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hewei
 * @date 2019/11/10 - 22:24
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {
    //根据用户id查询改用户所属的公司id
    XcCompanyUser findByUserId(String userId);

}
