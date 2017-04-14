package com.danlu.dleye.core;

import java.util.List;

import com.danlu.dleye.persist.base.Info;

public interface InfoManager {
    
    List<Info> getAllInfos();
    
    int addInfo(Info info);
    
    int updateInfo(Info info);
    
    int deleteInfo(long infoId);
    
    List<Info> getInfosByName(String infoName);
    
    Info getInfoById(long infoId);

}
