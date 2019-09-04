package xiaozhuo.info.persist.base;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class TemplateInfo {
    private Long id;

    private String templateTitle;

    private String templateContent;

    private Integer templateType;

    private Integer templateStatus;

    private LocalDate gmtCreate;

    private LocalDate gmtModifed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle == null ? null : templateTitle.trim();
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent == null ? null : templateContent.trim();
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public Integer getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(Integer templateStatus) {
        this.templateStatus = templateStatus;
    }

    public LocalDate getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDate gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDate getGmtModifed() {
        return gmtModifed;
    }

    public void setGmtModifed(LocalDate gmtModifed) {
        this.gmtModifed = gmtModifed;
    }
}