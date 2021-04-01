package xiaozhuo.info.web.common;

import lombok.Data;

/**
 * @author chenzhuo
 * @date   2021-02-06 16:35
 */
@Data
public class Page {

    private Integer totalCount;

    private Integer totalPage;

    private Integer currentPage;

    private Integer pageSize;

    public Page(){}
    public Page(Pageable pageable){
        this.totalCount = pageable.getRecord();
        this.totalPage = pageable.getTotalPageCount();
        this.currentPage = pageable.getPageIndex();
        this.pageSize = pageable.getPageSize();
    }

    public void init() {
        this.totalPage = 0;
        this.currentPage = 1;
        this.totalCount = 0;
        this.pageSize = 0;
    }
}
