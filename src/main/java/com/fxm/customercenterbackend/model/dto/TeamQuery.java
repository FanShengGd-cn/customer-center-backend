package com.fxm.customercenterbackend.model.dto;

import com.fxm.customercenterbackend.model.request.PageRequest;

import java.io.Serializable;
import java.util.Date;


public class TeamQuery extends PageRequest implements Serializable {
    /**
     * 搜索内容
     */
    private String searchText;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 过期时间
     */
    private Date expireTime;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public TeamQuery() {}


}
