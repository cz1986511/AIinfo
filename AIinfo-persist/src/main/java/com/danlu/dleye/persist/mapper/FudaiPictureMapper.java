package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.FudaiPicture;

public interface FudaiPictureMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(FudaiPicture record);

    int insertSelective(FudaiPicture record);

    FudaiPicture selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FudaiPicture record);

    int updateByPrimaryKey(FudaiPicture record);
}