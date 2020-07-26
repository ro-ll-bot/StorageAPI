package com.rollbot.fileapi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class OSSFile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer userId;
    private long size;
    private String name, description;
    private String mimeType;

    @Column(unique= true)
    private String filePath;
    private String absoluteFilePath;

    private Date uploadTime, createTime, updateTime;
    // private TrustedFileType fileType;
    private String extension;

    // @OneToMany
    // private Set<OSSShare> shared;



    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public long getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getAbsoluteFilePath() {
        return absoluteFilePath;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getExtension() {
        return extension;
    }


    public String getMimeType() {
        return mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OSSFile ossFile = (OSSFile) o;
        return size == ossFile.size &&
                Objects.equals(id, ossFile.id) &&
                Objects.equals(userId, ossFile.userId) &&
                Objects.equals(name, ossFile.name) &&
                Objects.equals(description, ossFile.description) &&
                Objects.equals(filePath, ossFile.filePath) &&
                Objects.equals(absoluteFilePath, ossFile.absoluteFilePath) &&
                Objects.equals(uploadTime.getTime(), ossFile.uploadTime.getTime()) &&
                Objects.equals(createTime.getTime(), ossFile.createTime.getTime()) &&
                Objects.equals(updateTime==null?null:updateTime.getTime(), ossFile.updateTime==null?null:ossFile.updateTime.getTime()) &&
                Objects.equals(extension, ossFile.extension);
    }



    public static class Builder{
        private OSSFile file;

        public Builder(String filename){
            this.file = new OSSFile();

            this.file.name = filename;
            this.file.uploadTime = new Date();
            this.file.createTime = new Date();
        }

        public Builder extension(String extension){
            this.file.extension = extension;
            return this;
        }

        public Builder path(String path){
            this.file.filePath = path;
            return this;
        }

        public Builder absoluteFilePath(String path){
            this.file.absoluteFilePath = path;
            return this;
        }

        public Builder size(long size){
            this.file.size = size;
            return this;
        }

        public Builder description(String description){
            if(description==null) description= "";
            this.file.description = description;
            return this;
        }

        public Builder userId(int userId){
            this.file.userId = userId;
            return this;
        }

        public Builder mimeType(String mimeType){
            this.file.mimeType = mimeType;
            return this;
        }

        public OSSFile build(){
            return this.file;
        }
    }

    @Override
    public String toString() {
        return "OSSFile{" +
                "id=" + id +
                ", userId=" + userId +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", absoluteFilePath='" + absoluteFilePath + '\'' +
                ", uploadTime=" + uploadTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", extension='" + extension + '}';
    }
}
