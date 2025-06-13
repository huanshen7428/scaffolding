package com.huanshen.scaffolding.security.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Schema(description = "个人中心")
@Data
public class LoginUser implements Serializable {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "失效时间")
    private Integer expireDays;

    @Schema(description = "姓名")
    private String userName;

    @Schema(description = "手机号")
    private String mobilePhone;

    @Schema(description = "固定电话")
    private String officePhone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "语种")
    private String lang;

    @Schema(description = "token")
    private String token;

    @Schema(description = "角色")
    private String roleName;

    @Schema(description = "是否局方账户")
    private Boolean isOwner;

}
