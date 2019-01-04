package xiaozhuo.info.persist.base;

import java.util.Date;

public class WeatherInfo {
    private Long id;

    private String cityName;

    private String dateTime;

    private Integer highTemperature;

    private Integer lowTemperature;

    private String textDay;

    private String codeDay;

    private String textNight;

    private String codeNight;

    private String windDirection;

    private String suggestion;

    private String status;

    private Date gmtCreate;

    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime == null ? null : dateTime.trim();
    }

    public Integer getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(Integer highTemperature) {
        this.highTemperature = highTemperature;
    }

    public Integer getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(Integer lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public String getTextDay() {
        return textDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay == null ? null : textDay.trim();
    }

    public String getCodeDay() {
        return codeDay;
    }

    public void setCodeDay(String codeDay) {
        this.codeDay = codeDay == null ? null : codeDay.trim();
    }

    public String getTextNight() {
        return textNight;
    }

    public void setTextNight(String textNight) {
        this.textNight = textNight == null ? null : textNight.trim();
    }

    public String getCodeNight() {
        return codeNight;
    }

    public void setCodeNight(String codeNight) {
        this.codeNight = codeNight == null ? null : codeNight.trim();
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection == null ? null : windDirection.trim();
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion == null ? null : suggestion.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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
}