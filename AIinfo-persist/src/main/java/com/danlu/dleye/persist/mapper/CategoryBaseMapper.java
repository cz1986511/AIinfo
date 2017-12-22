package com.danlu.dleye.persist.mapper;

import com.danlu.dleye.persist.base.CategoryBase;

public interface CategoryBaseMapper
{
    int deleteByPrimaryKey(Long categoryId);

    int insert(CategoryBase record);

    int insertSelective(CategoryBase record);

    CategoryBase selectByPrimaryKey(Long categoryId);

    int updateByPrimaryKeySelective(CategoryBase record);

    int updateByPrimaryKey(CategoryBase record);
}