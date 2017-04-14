package com.danlu.dleye.core;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.persist.base.UserInfoEntity;

public interface UserInfoManager {

    /**
     * 根据userId获取用户信息
     * param: userId
     * result: UserInfoEntity
     */
    UserInfoEntity getUserInfoByUserId(Long userId);

    /**
     * 根据参数获取用户列表
     * param: Map<String, Object>map
     * result: List<UserInfoEntity>
     */
    List<UserInfoEntity> getUserListByParams(Map<String, Object> map);

    /**
     * 新增用户
     * param: UserInfoEntity
     * result:
     */
    Long addUserInfo(UserInfoEntity userInfoEntity);

    /**
     * 修改用户信息
     * param: UserInfoEntity
     * result:
     */
    int updateUserInfo(UserInfoEntity userInfoEntity);

    /**
     * 获取用户列表总数
     * param: Map<String, Object>map
     * result: Long
     */
    Long getUserListCount(Map<String, Object> map);

}
