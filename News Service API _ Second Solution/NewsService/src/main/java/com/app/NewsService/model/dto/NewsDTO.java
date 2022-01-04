package com.app.NewsService.model.dto;

import java.sql.Date;

public class NewsDTO {
    Integer newsId;
    String title;
    String text;
    Date creationDate;
    Date validFrom;
    Date validTo;

    public NewsDTO() {
    }

    public NewsDTO(Integer newsId, String title, String text, Date creationDate, Date validFrom, Date validTo) {
        this.newsId = newsId;
        this.title = title;
        this.text = text;
        this.creationDate = creationDate;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
}
