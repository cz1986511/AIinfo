package com.idmaker.oa.server.model.vo.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;

/**
 * @author chenzhuo
 * @date   2021-02-06 16:35
 */
public class Pageable extends BasicObject {

    public static final int CONSTANT_DEFAULT_PAGE_SIZE = 10;
    public static final int CONSTANT_MAX_PAGE_SIZE = 1000;

    @JsonIgnore
    @Setter
    private Integer pageSize;

    @JsonIgnore
    @Setter
    private Integer pageIndex;

    @JsonIgnore
    private Integer record;

    @JsonIgnore
    private Integer totalPageCount;

    @JsonIgnore
    @Setter
    private Integer startIndex;

    public Integer getPageSize() {
        if (pageSize == null || pageSize == 0) {
            pageSize = CONSTANT_DEFAULT_PAGE_SIZE;
        }
        if (pageSize > CONSTANT_MAX_PAGE_SIZE) {
//            pageSize = CONSTANT_MAX_PAGE_SIZE;
        }
        return pageSize;
    }

    public void setRecord(Integer record) {
        this.record = record;
        setTotalPageCount();
    }

    public Integer getRecord() {
        if (record == null) {
            return 0;
        }
        return record;
    }

    public int getTotalPageCount() {
        if (totalPageCount == null) {
            return 1;
        }
        return totalPageCount;
    }

    public void setTotalPageCount() {
        int totalP = getRecord() % getPageSize() == 0 ? getRecord() / getPageSize() :
                getRecord() / getPageSize() + 1;
        this.totalPageCount = totalP;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex == 0) {
            return 1;
        }
        return pageIndex;
    }

    @JsonIgnore
    public Integer getStartindex() {
        return startIndex;
    }

    public Integer getStartIndex() {
        if (null == startIndex || 0 == startIndex) {
            return (getPageIndex() - 1) * getPageSize();
        }
        return startIndex;
    }

}
