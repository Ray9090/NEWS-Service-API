package com.newsservice.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ReadStatus implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -826819203061358173L;

	@Id
    @GeneratedValue
    @Column(name = "READ_STATUS_ID")
	private Long id;

    @ManyToOne()
    @JoinColumn(name = "NEWS_ID")  
	private News news;
	
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")  
	private User user;
	
    @Column(name = "READ_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date readDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getReadDate() {
		return readDate;
	}

	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}

	public ReadStatus(Long id, News news, User user, Date readDate) {
		super();
		this.id = id;
		this.news = news;
		this.user = user;
		this.readDate = readDate;
	}

	public ReadStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
