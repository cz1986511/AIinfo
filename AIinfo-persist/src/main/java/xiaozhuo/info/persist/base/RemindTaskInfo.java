package xiaozhuo.info.persist.base;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RemindTaskInfo {
    private Long id;

    private String remindName;

    private LocalDate remindContentTime;

    private Integer remindTaskType;

    private Integer remindCycle;

    private LocalDate remindDateTime;

    private Long templateId;

    private String remindPerson;

    private String remindPersonTel;

    private String remindPersonEmail;

    private Integer remindStatus;
    
    private String remindFrom;
    
    private String remindFromPass;

    private LocalDate gmtCreate;

    private LocalDate gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemindName() {
        return remindName;
    }

    public void setRemindName(String remindName) {
        this.remindName = remindName == null ? null : remindName.trim();
    }

    public LocalDate getRemindContentTime() {
        return remindContentTime;
    }

    public void setRemindContentTime(LocalDate remindContentTime) {
        this.remindContentTime = remindContentTime;
    }

    public Integer getRemindTaskType() {
        return remindTaskType;
    }

    public void setRemindTaskType(Integer remindTaskType) {
        this.remindTaskType = remindTaskType;
    }

    public Integer getRemindCycle() {
        return remindCycle;
    }

    public void setRemindCycle(Integer remindCycle) {
        this.remindCycle = remindCycle;
    }

    public LocalDate getRemindDateTime() {
        return remindDateTime;
    }

    public void setRemindDateTime(LocalDate remindDateTime) {
        this.remindDateTime = remindDateTime;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getRemindPerson() {
        return remindPerson;
    }

    public void setRemindPerson(String remindPerson) {
        this.remindPerson = remindPerson == null ? null : remindPerson.trim();
    }

    public String getRemindPersonTel() {
        return remindPersonTel;
    }

    public void setRemindPersonTel(String remindPersonTel) {
        this.remindPersonTel = remindPersonTel == null ? null : remindPersonTel.trim();
    }

    public String getRemindPersonEmail() {
        return remindPersonEmail;
    }

    public void setRemindPersonEmail(String remindPersonEmail) {
        this.remindPersonEmail = remindPersonEmail == null ? null : remindPersonEmail.trim();
    }

    public Integer getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(Integer remindStatus) {
        this.remindStatus = remindStatus;
    }

    public String getRemindFrom() {
		return remindFrom;
	}

	public void setRemindFrom(String remindFrom) {
		this.remindFrom = remindFrom;
	}

	public String getRemindFromPass() {
		return remindFromPass;
	}

	public void setRemindFromPass(String remindFromPass) {
		this.remindFromPass = remindFromPass;
	}

	public LocalDate getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDate gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDate getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDate gmtModified) {
        this.gmtModified = gmtModified;
    }
}