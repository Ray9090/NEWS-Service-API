package com.newsservice.model;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Picture implements Serializable {

	@Id
    @GeneratedValue
	@Column(name = "PICTURE_ID", nullable = false)
	private Long id;
	
	private String pictureName;
	
	private String pictureData;
	
	@ManyToOne()
    @JoinColumn(name = "NEWS_ID")
	private News news;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}

	public String getPictureData() {
		return pictureData;
	}

	public void setPictureData(String pictureData) {
		this.pictureData = pictureData;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public Picture(Long id, String pictureName, String pictureData, News news) {
		super();
		this.id = id;
		this.pictureName = pictureName;
		this.pictureData = pictureData;
		this.news = news;
	}

	public Picture() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Picture{" +
				"id=" + id +
				", pictureName='" + pictureName + '\'' +
				", pictureData='" + pictureData + '\'' +
				", news=" + news +
				'}';
	}
}
