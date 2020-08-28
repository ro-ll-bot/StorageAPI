package com.rollbot.fileapi.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class OSSShared {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer sharedUserId;
    private Integer ownerUserId;
    private String title;
    private String description;
    private boolean sendMail;
    // private boolean update;
    private Date expireTime;
    private Date sharedTime;
    private String shareUrl;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private OSSFile sharedFile;


    public OSSShared(){}

    public static class Builder{
        private OSSShared ossShared;
        public Builder(int sharedUserId, OSSFile sharedFile){
            ossShared = new OSSShared();
            ossShared.sharedUserId = sharedUserId;
            ossShared.sharedFile = sharedFile;
            ossShared.ownerUserId = sharedFile.getUserId();
            ossShared.sharedTime = new Date(); // Initialize shared time.
        }

        public Builder addTitle(String title){
            ossShared.title = title;
            return this;
        }

        public Builder addDescription(String description){
            ossShared.description = description;
            return this;
        }

        public Builder sendMail(boolean sendMail){
            ossShared.sendMail = sendMail;
            return this;
        }

        public Builder expireTime(Date expireTime){
            ossShared.expireTime = expireTime;
            return this;
        }

        public OSSShared build(){
            return ossShared;
        }
    }

    @Override
    public String toString() {
        return "OSSShared{" +
                "id=" + id +
                ", sharedUserId=" + sharedUserId +
                ", ownerUserId=" + ownerUserId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", sendMail=" + sendMail +
                ", expireTime=" + expireTime +
                ", sharedTime=" + sharedTime +
                ", shareUrl='" + shareUrl + '\'' +
                ", sharedFile=" + sharedFile +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OSSShared ossShared = (OSSShared) o;
        return sendMail == ossShared.sendMail &&
                sharedUserId.equals(ossShared.sharedUserId) &&
                ownerUserId.equals(ossShared.ownerUserId) &&
                Objects.equals(title, ossShared.title) &&
                Objects.equals(description, ossShared.description) &&
                //Objects.equals(expireTime, ossShared.expireTime) &&
                //Objects.equals(sharedTime, ossShared.sharedTime) &&
                Objects.equals(shareUrl, ossShared.shareUrl) &&
                sharedFile.equals(ossShared.sharedFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sharedUserId, ownerUserId, title, description, sendMail, expireTime, sharedTime, shareUrl, sharedFile);
    }

    public Integer getId() {
        return id;
    }

    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    public Integer getSharedUserId() {
        return sharedUserId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSendMail() {
        return sendMail;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public OSSFile getSharedFile() {
        return sharedFile;
    }

    public Date getSharedTime() {
        return sharedTime;
    }
}
