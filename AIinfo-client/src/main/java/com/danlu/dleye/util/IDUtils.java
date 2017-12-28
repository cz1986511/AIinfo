/**
 * Copyright (c) danlu.com 2017 All Rights Reserved. 
 * @Title:	IDUtils.java 
 * @Package:	com.danlu.dleye.util 
 * @Description:	TODO(用一句话描述该文件做什么) 
 * @author:		WL 
 * @date:		2017年12月28日 下午2:26:58 
 * @version:	V1.0   
 */
package com.danlu.dleye.util;

import java.util.UUID;

/** 
 * @ClassName:	IDUtils
 * @Version:	1.0
 * @Description:	TODO
 * @author:	WL
 * @date:	2017年12月28日 下午2:26:58
 */
public class IDUtils
{
    /**
     * 生成32位主键
     * @return
     */
    public static String nextUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
