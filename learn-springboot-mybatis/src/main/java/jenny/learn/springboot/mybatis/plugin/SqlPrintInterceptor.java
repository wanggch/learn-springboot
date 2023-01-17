package jenny.learn.springboot.mybatis.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.assertj.core.util.Lists;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class SqlPrintInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> args = Lists.newArrayList(invocation.getArgs());
        MappedStatement mappedStatement = (MappedStatement) args.get(0);
        Object param = args.size() > 1 ? args.get(1) : null;
        // 开始时间
        long beginTimeMillis = System.currentTimeMillis();
        // SQL执行时间
        long costTimeMillis;
        // SQL执行结果
        Object result;
        try {
            result = invocation.proceed();
        } finally {
            costTimeMillis = System.currentTimeMillis() - beginTimeMillis;
        }

        BoundSql boundSql = mappedStatement.getBoundSql(param);
        // SQL，最初的SQL中含有?，我们会将SQL中的?替换成对应的参数
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        log.info("sql: {}, cost time millis: {}", sql, costTimeMillis);

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = new StringBuffer("'").append(obj).append("'").toString();
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = new StringBuffer("'").append(formatter.format(new Date())).append("'").toString();
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value.replace("$", "\\$");
    }
}
