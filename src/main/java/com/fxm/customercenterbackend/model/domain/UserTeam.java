package com.fxm.customercenterbackend.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 用户队伍关系
 * @TableName user_team
 */
@TableName(value ="user_team")
@Data
public class UserTeam implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 加入时间
     */
    private Date joinTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTeam userTeam)) return false;
        return Objects.equals(id, userTeam.id) && Objects.equals(userId, userTeam.userId) && Objects.equals(teamId, userTeam.teamId) && Objects.equals(joinTime, userTeam.joinTime) && Objects.equals(createTime, userTeam.createTime) && Objects.equals(updateTime, userTeam.updateTime) && Objects.equals(isDelete, userTeam.isDelete);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, teamId, joinTime, createTime, updateTime, isDelete);
    }

    @Override
    public String toString() {
        return "UserTeam{" +
                "id=" + id +
                ", userId=" + userId +
                ", teamId=" + teamId +
                ", joinTime=" + joinTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isDelete=" + isDelete +
                '}';
    }
}