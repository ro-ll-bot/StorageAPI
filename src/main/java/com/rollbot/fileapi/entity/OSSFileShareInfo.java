package com.rollbot.fileapi.entity;


import java.util.Date;

public class OSSFileShareInfo {

    private int ownerUserId;
    private int sharedUserId;
    private String title;
    private String description;
    private String filename;
    private boolean sendMail;
    private Date expireTime;

    private OSSFileShareInfo(){
        ownerUserId = -1;
        sharedUserId = -1;
        filename = null;
        title = null;
        description = null;
        sendMail = false;
        expireTime = null;
    }


    public static class Builder{
        private OSSFileShareInfo ossFileShareInfo;
        public Builder(int ownerUserId, int sharedUserId, String filename){
            ossFileShareInfo = new OSSFileShareInfo();
            ossFileShareInfo.ownerUserId = ownerUserId;
            ossFileShareInfo.sharedUserId = sharedUserId;
            ossFileShareInfo.filename = filename;
        }

        public Builder addTitle(String title){
            ossFileShareInfo.title = title;
            return this;
        }
        public Builder addDescription(String description){
            ossFileShareInfo.description = description;
            return this;
        }
        public Builder sendMail(boolean sendMail){
            ossFileShareInfo.sendMail = sendMail;
            return this;
        }

        public Builder setExpireTime(Date expireTime){
            ossFileShareInfo.expireTime = expireTime;
            return this;
        }

        public OSSFileShareInfo build(){
            return ossFileShareInfo;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public int getSharedUserId() {
        return sharedUserId;
    }

    public String getFilename() {
        return filename;
    }

    public boolean sendMail() {
        return sendMail;
    }

    public Date getExpireTime() {
        return expireTime;
    }
}
