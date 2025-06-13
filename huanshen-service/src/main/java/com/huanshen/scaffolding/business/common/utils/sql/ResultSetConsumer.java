package com.huanshen.scaffolding.business.common.utils.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ResultSetConsumer
 *
 */
public interface ResultSetConsumer {

    void accept(ResultSet resultSet) throws SQLException;
}
