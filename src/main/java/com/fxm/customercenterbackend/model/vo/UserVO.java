package com.fxm.customercenterbackend.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账户
     */
    private String userAccount;


    /**
     * 标签 json 列表
     */
    private String tags;

    /**
     * 是否有效
     */
    private Integer isValid;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户描述
     */
    private String description;

    /**
     * 用户权限 0-管理员 1-普通用户
     */
    private Integer userRole;
}
