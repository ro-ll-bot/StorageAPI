package com.rollbot.fileapi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class File implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer userId;
    private long size;
    private String name, description;
    private String filePath;
    private String absoluteFilePath;
    private Date uploadTime, createTime, updateTime;
    // private TrustedFileType fileType;
    private String extension;


    @OneToMany
    private Set<ShareInformation> shared;

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
}
