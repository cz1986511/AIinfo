package com.danlu.dleye.service;

import java.util.List;
import java.util.Map;

import com.danlu.dleye.client.entity.FudaiDetail;

public interface FudaiService
{

    /**
     * {
     *      fdName:"双旦福袋",
     *      fudaiItemInfos:[{
     *              fdItemId:"ajfldsjfjsfds",
     *              fdItemNumber:12
     *          }]
     *  }
     */
    int addFudai(Map<String, Object> map);

    int updateFudai(Map<String, Object> map);

    List<FudaiDetail> getFudaiDetails(Map<String, Object> map);

}
