package com.newsservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class News implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8615174461081097472L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NEWS_ID")
	private Long id;
	
	private String title;
	
	private String text;
	
	@Column(name = "CREATION_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Column(name="VALID_FROM")
	@Temporal(TemporalType.TIMESTAMP)
	private Date validFrom;
	
	@Column(name="VALID_TO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date validTo;
	
	private Boolean isPublished;

	private String createdBy;
	@OneToMany(cascade= CascadeType.ALL, mappedBy = "news")
	private Set<Picture> picture;

	@OneToMany(cascade= CascadeType.ALL, mappedBy = "news")
	private Set<ReadStatus> readStatus = new HashSet<ReadStatus>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}

	public Set<Picture> getPicture() {
		return picture;
	}

	public void setPicture(Set<Picture> picture) {
		this.picture = picture;
	}
	
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public News(Long id, String title, String text, Date creationDate, Date validFrom, Date validTo,
			Boolean isPublished, Set<ReadStatus> readStatus, Set<Picture> picture, String createdBy) {
		super();
		this.id = id;
		this.title = title;
		this.text = text;
		this.creationDate = creationDate;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.isPublished = isPublished;
		this.readStatus = readStatus;
		this.picture = picture;
		this.createdBy = createdBy;
	}

	public News() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<ReadStatus> getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(Set<ReadStatus> readStatus) {
		this.readStatus = readStatus;
	}
	
	
}
