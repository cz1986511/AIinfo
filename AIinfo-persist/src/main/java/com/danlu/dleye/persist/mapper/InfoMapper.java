package com.danlu.dleye.persist.mapper;

import java.util.HashMap;
import java.util.List;

import com.danlu.dleye.persist.base.Info;

public interface InfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Info info);

    int insertSelective(Info info);

    Info selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Info info);

    int updateByPrimaryKey(Info info);
    
    List<Info> selectAllInfos(HashMap<String, Integer> hashMap);
}