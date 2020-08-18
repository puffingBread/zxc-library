package org.humor.zxc.library.commons.dao.translator;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;
import org.humor.zxc.library.commons.dao.dto.OperateLogDTO;
import org.humor.zxc.library.commons.dao.dto.req.SqlLogAddQuery;
import org.humor.zxc.library.commons.dao.enums.OperateTypeEnum;
import org.humor.zxc.library.commons.util.utils.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.*;

/***
 *  Date: 2019/8/28
 *  Time: 12:02
 *  @author xuzz
 */
@Slf4j
public class UpdateTranslatorImpl implements TranslatorService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OperateLogDTO> translate(SqlLogAddQuery addQuery) throws JSQLParserException {
        String type = addQuery.getOperateType();
        String sql = addQuery.getSql();
        Statement statement = CCJSqlParserUtil.parse(sql);
        Update update = (Update) statement;

        String condition = update.getWhere().toString();
        if (sql.contains("is_deleted=1")) {
            type = OperateTypeEnum.REMOVE.name();
        }

        List<OperateLogDTO> operateLogs = new LinkedList<>();
        if (Objects.nonNull(addQuery.getTableId()) && !addQuery.getTableId().equals(0L)) {
            OperateLogDTO operateLog = BeanUtils.copyProperties(addQuery, OperateLogDTO::new);
            operateLog.setOperateType(OperateTypeEnum.valueOf(type));
            operateLog.setContent(Translator.buildContent(update.getColumns(), update.getExpressions()));
            operateLog.setTableId(addQuery.getTableId());
            operateLog.setOperateTime(new Date());
            operateLogs.add(operateLog);
        } else {
            if (condition.toUpperCase().contains(" ID = ") || condition.toUpperCase().contains("(ID = ")) {
                Long tableId = this.getIdFromCondition(condition);
                OperateLogDTO operateLog = BeanUtils.copyProperties(addQuery, OperateLogDTO::new);
                operateLog.setOperateType(OperateTypeEnum.valueOf(type));
                operateLog.setContent(Translator.buildContent(update.getColumns(), update.getExpressions()));
                operateLog.setTableId(tableId);
                operateLog.setOperateTime(new Date());
                operateLogs.add(operateLog);
            } else if (condition.toUpperCase().contains("ID IN (")) {
                int startIndex = condition.toUpperCase().indexOf("ID IN (") + 7;
                String substring = condition.substring(startIndex, startIndex + condition.substring(startIndex).indexOf(")"));
                String[] ids = substring.split(",");
                for (String id : ids) {
                    OperateLogDTO operateLog = BeanUtils.copyProperties(addQuery, OperateLogDTO::new);
                    operateLog.setOperateType(OperateTypeEnum.valueOf(type));
                    operateLog.setContent(Translator.buildContent(update.getColumns(), update.getExpressions()));
                    operateLog.setTableId(Long.valueOf(id.trim()));
                    operateLog.setOperateTime(new Date());
                    operateLogs.add(operateLog);
                }
            } else {
                String sqlOld = "SELECT id FROM " + addQuery.getTableName() + " WHERE ";

                sqlOld += condition;

                List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sqlOld);
                if (!mapList.isEmpty()) {
                    for (Map<String, Object> map : mapList) {
                        OperateLogDTO operateLog = BeanUtils.copyProperties(addQuery, OperateLogDTO::new);
                        operateLog.setOperateType(OperateTypeEnum.valueOf(type));
                        operateLog.setContent(Translator.buildContent(update.getColumns(), update.getExpressions()));
                        operateLog.setTableId(map.get("id") == null ? 0L : Long.parseLong(map.get("id").toString()));
                        operateLog.setOperateTime(new Date());
                        operateLogs.add(operateLog);
                    }
                }
            }
        }

        return operateLogs;
    }

    private Long getIdFromCondition(String condition) {
        String idStr;
        if (condition.toUpperCase().contains("(ID = ")) {
            int startIndex = condition.toUpperCase().indexOf("(ID = ") + 6;
            String substring = condition.toUpperCase().substring(startIndex);
            int endIndex = substring.contains(" AND ") ? substring.indexOf(" AND") : substring.indexOf(")");
            idStr = condition.substring(startIndex, startIndex + endIndex).trim();
        } else {
            int startIndex = condition.toUpperCase().indexOf("ID=") + 3;
            String substring = condition.substring(startIndex);
            int endIndex = substring.contains(" AND ") ? substring.indexOf(" AND ") : substring.length();
            idStr = condition.substring(startIndex, startIndex + endIndex).trim();
        }

        return Long.parseLong(idStr);
    }
}
