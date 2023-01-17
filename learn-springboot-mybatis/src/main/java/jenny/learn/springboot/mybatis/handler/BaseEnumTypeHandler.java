package jenny.learn.springboot.mybatis.handler;

import jenny.learn.springboot.mybatis.base.BaseEnum;
import jenny.learn.springboot.mybatis.util.BaseEnumUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器
 * @author: wanggc
 */
public class BaseEnumTypeHandler<E extends BaseEnum> extends BaseTypeHandler<BaseEnum> {

    private Class<E> type;

    public BaseEnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseEnum baseEnum, JdbcType jdbcType) throws SQLException {
        ps.setString(i, baseEnum.getCode());
    }

    @Override
    public BaseEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return rs.wasNull() ? null : BaseEnumUtil.codeOf(type, code);
    }

    @Override
    public BaseEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return rs.wasNull() ? null : BaseEnumUtil.codeOf(type, code);
    }

    @Override
    public BaseEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return cs.wasNull() ? null : BaseEnumUtil.codeOf(type, code);
    }
}
