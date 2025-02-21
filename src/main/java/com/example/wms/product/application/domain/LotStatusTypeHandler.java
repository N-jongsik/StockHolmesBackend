package com.example.wms.product.application.domain;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LotStatusTypeHandler extends BaseTypeHandler<LotStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LotStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public LotStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String status = rs.getString(columnName);
        return status == null ? null : LotStatus.valueOf(status);
    }

    @Override
    public LotStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String status = rs.getString(columnIndex);
        return status == null ? null : LotStatus.valueOf(status);
    }

    @Override
    public LotStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String status = cs.getString(columnIndex);
        return status == null ? null : LotStatus.valueOf(status);
    }
}
