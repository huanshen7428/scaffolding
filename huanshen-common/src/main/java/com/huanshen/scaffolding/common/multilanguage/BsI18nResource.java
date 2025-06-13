package com.huanshen.scaffolding.common.multilanguage;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName bs_i18n_resource
 */
@TableName(value ="bs_i18n_resource")
@Data
public class BsI18nResource implements Serializable {
    /**
     *
     */
    @TableId
    private String resKey;

    /**
     *
     */
    private String enUs;

    /**
     *
     */
    private String zhCn;

    /**
     * Record status U: normal E: deleted
     */
    private String state;

    /**
     *
     */
    private String remark;

}
