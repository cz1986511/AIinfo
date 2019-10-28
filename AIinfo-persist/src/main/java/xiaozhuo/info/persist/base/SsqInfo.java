package xiaozhuo.info.persist.base;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SsqInfo {
    private Long id;

    private Integer qid;

    private Integer cpid;

    private LocalDate openDate;

    private LocalDate deadLine;

    private String saleAmount;

    private String totalMoney;

    private Integer h1Num;

    private Integer h2Num;

    private Integer h3Num;

    private Integer h4Num;

    private Integer h5Num;

    private Integer h6Num;

    private Integer lNum;

    private Integer p1Num;

    private String p1Bonus;

    private Integer p2Num;

    private String p2Bonus;

    private Integer status;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModify;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQid() {
        return qid;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public Integer getCpid() {
        return cpid;
    }

    public void setCpid(Integer cpid) {
        this.cpid = cpid;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount == null ? null : saleAmount.trim();
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney == null ? null : totalMoney.trim();
    }

    public Integer getH1Num() {
        return h1Num;
    }

    public void setH1Num(Integer h1Num) {
        this.h1Num = h1Num;
    }

    public Integer getH2Num() {
        return h2Num;
    }

    public void setH2Num(Integer h2Num) {
        this.h2Num = h2Num;
    }

    public Integer getH3Num() {
        return h3Num;
    }

    public void setH3Num(Integer h3Num) {
        this.h3Num = h3Num;
    }

    public Integer getH4Num() {
        return h4Num;
    }

    public void setH4Num(Integer h4Num) {
        this.h4Num = h4Num;
    }

    public Integer getH5Num() {
        return h5Num;
    }

    public void setH5Num(Integer h5Num) {
        this.h5Num = h5Num;
    }

    public Integer getH6Num() {
        return h6Num;
    }

    public void setH6Num(Integer h6Num) {
        this.h6Num = h6Num;
    }

    public Integer getlNum() {
        return lNum;
    }

    public void setlNum(Integer lNum) {
        this.lNum = lNum;
    }

    public Integer getP1Num() {
        return p1Num;
    }

    public void setP1Num(Integer p1Num) {
        this.p1Num = p1Num;
    }

    public String getP1Bonus() {
        return p1Bonus;
    }

    public void setP1Bonus(String p1Bonus) {
        this.p1Bonus = p1Bonus == null ? null : p1Bonus.trim();
    }

    public Integer getP2Num() {
        return p2Num;
    }

    public void setP2Num(Integer p2Num) {
        this.p2Num = p2Num;
    }

    public String getP2Bonus() {
        return p2Bonus;
    }

    public void setP2Bonus(String p2Bonus) {
        this.p2Bonus = p2Bonus == null ? null : p2Bonus.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(LocalDateTime gmtModify) {
        this.gmtModify = gmtModify;
    }
}