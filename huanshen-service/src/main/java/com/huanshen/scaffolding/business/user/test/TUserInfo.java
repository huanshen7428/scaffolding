package com.huanshen.scaffolding.business.user.test;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User table
 * @TableName t_user_info
 */
@TableName(value ="t_user_info")
@Data
public class TUserInfo implements Serializable {
    /**
     * Auto-increment primary key
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * account
     */
    private String account;

    private String roleName;

    /**
     * password
     */
    private String password;

    /**
     * Password type
     */
    private Integer passwordType;

    /**
     * status
     */
    private Integer locked;

    /**
     * email
     */
    private String email;

    private String address;

    /**
     * office phone
     */
    private String officePhone;

    /**
     * mobile phone
     */
    private String mobilePhone;

    /**
     * Last password modification time
     */
    private Date lastPwdModifiedTime;

    /**
     * Creation time
     */
    private Date createdTime;

    /**
     * update_time
     */
    private Date updateTime;

    private String creator;

    private String editor;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
