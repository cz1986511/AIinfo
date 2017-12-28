/**
 * Copyright (c) danlu.com 2017 All Rights Reserved. 
 * @Title:	BaseController.java 
 * @Package:	com.danlu.web.controller 
 * @Description:	TODO(用一句话描述该文件做什么) 
 * @author:		WL 
 * @date:		2017年12月28日 下午4:15:37 
 * @version:	V1.0   
 */
package com.danlu.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.danlu.dleye.client.entity.JsonResult;

/** 
 * @ClassName:	BaseController
 * @Version:	1.0
 * @Description:	TODO
 * @author:	WL
 * @date:	2017年12月28日 下午4:15:37
 */
public class BaseController
{

    protected boolean hasLogin(HttpServletRequest request){
        return null != request.getSession().getAttribute("userId");
    }
    protected void setNotLoginMsg(JsonResult result){
        result.setMsg("未登录");
        result.setStatus(2);
    }
    
}
