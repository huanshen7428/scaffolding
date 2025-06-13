package com.huanshen.scaffolding.business.common.utils.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * StreamDataProcessor
 *
 */
@Slf4j
@Component
public class StreamDataProcessor {

    private final DataSource dataSource;

    @Autowired
    public StreamDataProcessor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void processAllRecords(String sql, ResultSetConsumer consumer) {
        log.info("sql={}", sql);
        try (PreparedStatement stmt = dataSource.getConnection().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setFetchSize(Integer.MIN_VALUE);
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                consumer.accept(rs);
            }
        } catch (SQLException e) {
            log.error("process data failed,", e);
        }
        log.info("sql execute end");
    }

}
