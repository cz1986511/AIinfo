package com.danlu.dleye.persist.base;

import java.util.Date;

public class ProductExtend {
    private Long productExtendId;

    private Long productId;

    private String productCoverPic;

    private String productDetailPic;

    private String productExtendStatus;

    private Date gmtCreate;

    private Date gmtModified;

    private String productDetailInfo;

    public Long getProductExtendId() {
        return productExtendId;
    }

    public void setProductExtendId(Long productExtendId) {
        this.productExtendId = productExtendId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductCoverPic() {
        return productCoverPic;
    }

    public void setProductCoverPic(String productCoverPic) {
        this.productCoverPic = productCoverPic == null ? null : productCoverPic.trim();
    }

    public String getProductDetailPic() {
        return productDetailPic;
    }

    public void setProductDetailPic(String productDetailPic) {
        this.productDetailPic = productDetailPic == null ? null : productDetailPic.trim();
    }

    public String getProductExtendStatus() {
        return productExtendStatus;
    }

    public void setProductExtendStatus(String productExtendStatus) {
        this.productExtendStatus = productExtendStatus == null ? null : productExtendStatus.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getProductDetailInfo() {
        return productDetailInfo;
    }

    public void setProductDetailInfo(String productDetailInfo) {
        this.productDetailInfo = productDetailInfo == null ? null : productDetailInfo.trim();
    }
}