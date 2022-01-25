package org.humor.zxc.library.commons.dao.interceptor;

import com.alibaba.druid.pool.DruidPooledPreparedStatement;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxyImpl;
import com.google.common.collect.Sets;
import com.mysql.cj.jdbc.ServerPreparedStatement;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.humor.zxc.library.commons.dao.dto.req.SqlLogAddQuery;
import org.humor.zxc.library.commons.dao.enums.OperateTypeEnum;
import org.humor.zxc.library.commons.dao.service.impl.SendLogServiceImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 操作记录插件
 * 同步信息到其他系统
 * 记录操作人
 *
 * 关于拦截器：
 * Executor > StatementHandler
 * 2019-08-12 12:08
 * @author xuzz
 */
@Intercepts({
    @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
    @Signature(type = StatementHandler.class, method = "batch", args = Statement.class)}
)
@Slf4j
public class SqlLogInterceptor implements Interceptor {

    @Resource
    private SendLogServiceImpl sendLogService;
    private ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
    private Set<String> excludeTables = Sets.newHashSet("operate_log");

    public SqlLogInterceptor() {
    }

    public SqlLogInterceptor(SendLogServiceImpl sendLogService, Set<String> excludeTables) {
        this.sendLogService = sendLogService;
        if (!CollectionUtils.isEmpty(excludeTables)) {
            this.excludeTables.addAll(excludeTables);
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object proceed = invocation.proceed();

        String sql = getSql(invocation);
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(CCJSqlParserUtil.parse(sql));
        String tableName = tableList.get(0);
        if (excludeTables.contains(tableName)) {
            return proceed;
        }

        StatementHandler sh = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = sh.getBoundSql();
        MetaObject metaObject = MetaObject.forObject(sh, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
            SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, reflectorFactory);

        MappedStatement ms =
            (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        /*
        批量新增，批量修改
        拿不到参数
         */
        Object parameterObject = boundSql.getParameterObject();
        if (proceed == null) {
            if (SqlCommandType.SELECT != ms.getSqlCommandType()) {
                sendLogService.sendSqlLog(parseEvent(ms.getSqlCommandType(),
                    parameterObject, sql, tableName));
            }
        } else {
            int result = (int) proceed;
            if (result != 0) {
                sendLogService.sendSqlLog(parseEvent(ms.getSqlCommandType(),
                    parameterObject, sql, tableName));
            }
        }

        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private SqlLogAddQuery parseEvent(SqlCommandType commandType,
                                      Object parameterObject, String sql, String tableName) throws Exception {

        Class<?> clazz = parameterObject.getClass();
        AtomicReference<Long> id = new AtomicReference<>(0L);
        try {
            Field idField = ReflectionUtils.findField(clazz, "id");
            if (Objects.nonNull(idField)) {
                idField.setAccessible(true);
                Optional.ofNullable(idField.get(parameterObject))
                    .ifPresent(field -> id.set(Long.parseLong(field.toString())));
            }
        } catch (Exception e) {
            log.error("operate log interceptor parseEvent get insert data id failed! message=", e);
        }

        SqlLogAddQuery addQuery = new SqlLogAddQuery();
        if (SqlCommandType.INSERT == commandType) {
            addQuery.setOperateType(OperateTypeEnum.INSERT.toString());
        }
        if (SqlCommandType.UPDATE == commandType) {
            addQuery.setOperateType(OperateTypeEnum.UPDATE.toString());
        }

        addQuery.setTableName(tableName);
        addQuery.setTableId(id.get());
        addQuery.setSql(sql);
        return addQuery;
    }

    private String getSql(Invocation invocation) throws SQLException {
        Statement s = (Statement) invocation.getArgs()[0];

        Statement stmt = s.unwrap(DruidPooledPreparedStatement.class).getStatement();

        // 配置druid连接时使用filters: stat配置
        if (stmt instanceof PreparedStatementProxyImpl) {
            stmt = ((PreparedStatementProxyImpl) stmt).getRawObject();
        }
        return stmt.unwrap(ServerPreparedStatement.class).asSql().replaceAll("\\s+", " ");
    }
}
