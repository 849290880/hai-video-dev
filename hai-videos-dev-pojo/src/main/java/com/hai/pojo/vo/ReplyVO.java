package com.hai.pojo.vo;

import java.util.Date;
import javax.persistence.*;

public class ReplyVO {
    private String id;
    
    /**
     * 回复人的头像
     */
    private String userFace;
    
    /**
     * 回复人的昵称
     */
    private String nickname;
    
    
    @Column(name = "father_id")
    private String fatherId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private Date createTime;

    private String comments;

    
    public String getUserFace() {
		return userFace;
	}

	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return father_id
     */
    public String getFatherId() {
        return fatherId;
    }

    /**
     * @param fatherId
     */
    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}