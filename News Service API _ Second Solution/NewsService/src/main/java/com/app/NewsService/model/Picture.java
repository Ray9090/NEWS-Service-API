package com.app.NewsService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pictures")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_id")
    Integer pictureID;
    @Column(name = "picture_name")
    String pictureName;
    @Column(name = "picture_type")
    String pictureType;
    @JsonIgnore
    @Lob
    private byte[] metadata;

    public Picture() {
    }

    public Picture(String pictureName, String pictureType, byte[] metadata) {
        this.pictureName = pictureName;
        this.pictureType = pictureType;
        this.metadata = metadata;
    }

    public Integer getPictureID() {
        return pictureID;
    }

    public void setPictureID(Integer pictureID) {
        this.pictureID = pictureID;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String additionalPictureData) {
        this.pictureType = additionalPictureData;
    }

    public byte[] getMetadata() {
        return metadata;
    }

    public void setMetadata(byte[] metadata) {
        this.metadata = metadata;
    }
}
