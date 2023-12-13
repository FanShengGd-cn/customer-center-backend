package com.fxm.customercenterbackend.model.request;

import lombok.Data;

@Data
public class UserRegisterRequest {
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 账户
     */
    private String userAccount;

    /**
     * 密码
     */
    private String password;
}
