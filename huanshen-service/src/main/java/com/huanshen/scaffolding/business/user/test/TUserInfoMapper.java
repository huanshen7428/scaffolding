package com.huanshen.scaffolding.business.user.test;

import com.huanshen.scaffolding.common.dynamic.DataSource;
import com.huanshen.scaffolding.common.dynamic.DataSourceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
@DataSource(value = DataSourceType.SLAVE)
public interface TUserInfoMapper extends BaseMapper<TUserInfo> {

}




