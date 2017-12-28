package com.danlu.dleye.client.entity;

/**
 * @商品对外DO
 */
public class ItemInfo {

    private Long itemId;

    private String itemName;

    private Long itemProductId;

    private String itemCategoryCode;

    private String itemCategoryName;

    private String itemBrandCode;

    private String itemBrandName;

    private Long itemShopId;

    private String itemStatus;

    private String itemDesc;
    private String itemUrl;

    private Long itemPrice;

    private Long itemOriginPrice;

    private Integer itemStock;

    private String itemType;

    private String itemCoverPic;

    private String itemDetailPic;

    private String itemDetailInfo;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getItemProductId() {
        return itemProductId;
    }

    public void setItemProductId(Long itemProductId) {
        this.itemProductId = itemProductId;
    }

    public String getItemCategoryCode() {
        return itemCategoryCode;
    }

    public void setItemCategoryCode(String itemCategoryCode) {
        this.itemCategoryCode = itemCategoryCode;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public String getItemBrandCode() {
        return itemBrandCode;
    }

    public void setItemBrandCode(String itemBrandCode) {
        this.itemBrandCode = itemBrandCode;
    }

    public String getItemBrandName() {
        return itemBrandName;
    }

    public void setItemBrandName(String itemBrandName) {
        this.itemBrandName = itemBrandName;
    }

    public Long getItemShopId() {
        return itemShopId;
    }

    public void setItemShopId(Long itemShopId) {
        this.itemShopId = itemShopId;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Long getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Long getItemOriginPrice() {
        return itemOriginPrice;
    }

    public void setItemOriginPrice(Long itemOriginPrice) {
        this.itemOriginPrice = itemOriginPrice;
    }

    public Integer getItemStock() {
        return itemStock;
    }

    public void setItemStock(Integer itemStock) {
        this.itemStock = itemStock;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemCoverPic() {
        return itemCoverPic;
    }

    public void setItemCoverPic(String itemCoverPic) {
        this.itemCoverPic = itemCoverPic;
    }

    public String getItemDetailPic() {
        return itemDetailPic;
    }

    public void setItemDetailPic(String itemDetailPic) {
        this.itemDetailPic = itemDetailPic;
    }

    public String getItemDetailInfo() {
        return itemDetailInfo;
    }

    public void setItemDetailInfo(String itemDetailInfo) {
        this.itemDetailInfo = itemDetailInfo;
    }

    public void setItemUrl(String itemUrl)
    {
        this.itemUrl = itemUrl;
    }
    public String getItemUrl()
    {
        return itemUrl;
    }

    @Override
    public String toString()
    {
        return "{\"itemId\":\"" + itemId + "\",\"itemName\":\"" + itemName
               + "\",\"itemProductId\":\"" + itemProductId + "\",\"itemCategoryCode\":\""
               + itemCategoryCode + "\",\"itemCategoryName\":\"" + itemCategoryName
               + "\",\"itemBrandCode\":\"" + itemBrandCode + "\",\"itemBrandName\":\""
               + itemBrandName + "\",\"itemShopId\":\"" + itemShopId + "\",\"itemStatus\":\""
               + itemStatus + "\",\"itemDesc\":\"" + itemDesc + "\",\"itemUrl\":\"" + itemUrl
               + "\",\"itemPrice\":\"" + itemPrice + "\",\"itemOriginPrice\":\"" + itemOriginPrice
               + "\",\"itemStock\":\"" + itemStock + "\",\"itemType\":\"" + itemType
               + "\",\"itemCoverPic\":\"" + itemCoverPic + "\",\"itemDetailPic\":\""
               + itemDetailPic + "\",\"itemDetailInfo\":\"" + itemDetailInfo + "\"}";
    }
    
}
