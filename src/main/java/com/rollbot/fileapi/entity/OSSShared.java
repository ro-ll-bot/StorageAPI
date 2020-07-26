package com.rollbot.fileapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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
    private String shareUrl;
    private OSSFile sharedFile;


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
}
