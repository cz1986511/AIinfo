package com.danlu.dleye.core.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.danlu.dleye.annotation.Switch;
import com.danlu.dleye.core.InfoManager;
import com.danlu.dleye.persist.base.Info;
import com.danlu.dleye.persist.mapper.InfoMapper;

public class InfoManagerImpl implements InfoManager {
    
    private static int start = 0;
    @Switch(description = "每页限制数量", name = "limit")
    private static int limit = 200;
    
    private InfoMapper infoMapper;

    @Autowired
    public void setInfoMapper(InfoMapper infoMapper) {
        this.infoMapper = infoMapper;
    }

    @Override
    public List<Info> getAllInfos() {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("offset", start);
        hashMap.put("limit", limit);
        return infoMapper.selectAllInfos(hashMap);
    }

    @Override
    public int addInfo(Info info) {
        if (null != info) {
            return infoMapper.insert(info);
        }
        return 0;
    }

    @Override
    public int updateInfo(Info info) {
        if (null != info) {
            return infoMapper.updateByPrimaryKey(info);
        }
        return 0;
    }

    @Override
    public int deleteInfo(long infoId) {
        return infoMapper.deleteByPrimaryKey(infoId);
    }

    @Override
    public List<Info> getInfosByName(String infoName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Info getInfoById(long infoId) {
        // TODO Auto-generated method stub
        return null;
    }

}
